package com.example.droidsoftthird

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.datetime.timePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.example.droidsoftthird.databinding.FragmentScheduleCreateBinding
import com.example.droidsoftthird.model.domain_model.ApiGroup
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException
import java.time.*
import java.util.*

@AndroidEntryPoint
class ScheduleCreateFragment:Fragment(R.layout.fragment_schedule_create) {

    /**
     * 【方針】
     *      タッチアクションは、Fragmentで請け負う。
     *      ViewModelで請け負った場合、observeしてFragmentからダイアログを起動させることになるので手間。
     *      また、observeするために、リスナーごとにSubjectを作るのも非効率的だと思われる。
     *      ついては、FragmentでViewにListenerをセットして、Dialogを起動する。
     *      名前とコメントの記入欄だけViewModelに直接書き込めるようにする。
     *      画面遷移については、UiModelの中に組み込んで、Succeedで全画面に戻るようにする。
     * */

    private val binding: FragmentScheduleCreateBinding by dataBinding()
    private val viewModel:ScheduleCreateViewModel by hiltNavGraphViewModels(R.id.schedule_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickAction()
        viewModel.initializeGroups()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupClickAction() {
        with(binding) {

            lifecycleOwner = viewLifecycleOwner
            includeScheduleCreateDate.itemScheduleCreate.setOnClickListener{
                //setThemeを行うと不具合が発生するため、ContextからThemeの変更を行う。
                requireActivity().setTheme(R.style.AppTheme_MaterialDialogDate)

                val today = MaterialDatePicker.todayInUtcMilliseconds()
                val endMonth = LocalDateTime.now().plusMonths(10)
                    .atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
                    .toInstant().toEpochMilli()
                val constraints = CalendarConstraints.Builder()
                    .setStart(today).setEnd(endMonth).setValidator(DateValidatorPointForward.now())
                    .build()

                val dialog = MaterialDatePicker.Builder.datePicker()
                    .setSelection(today)
                    .setCalendarConstraints(constraints)
                    .build().apply {
                        addOnPositiveButtonClickListener {
                            val selectedDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                            viewModel?.setSelectedDate(selectedDate)
                            requireActivity().setTheme(R.style.AppTheme)
                        }
                        addOnCancelListener { requireActivity().setTheme(R.style.AppTheme) }
                    }
                childFragmentManager.let { dialog.show(it, "schedule_date") }
            }

            includeScheduleCreateTime.itemScheduleCreate.setOnClickListener{

                //Themeを直接変更できないので、ContextからThemeの変更を行う。
                requireActivity().setTheme(R.style.AppTheme_MaterialDialogTime)

                var startTime: Calendar = Calendar.getInstance()

                val dialogForEndTime = MaterialDialog(requireContext())
                    .title(R.string.schedule_create_time_end)
                    .timePicker { _, endTime ->
                    viewModel?.setTimePeriod(startTime, endTime) ?:IllegalStateException()
                    requireActivity().setTheme(R.style.AppTheme)
                    }
                    .onCancel { requireActivity().setTheme(R.style.AppTheme) }
                    .lifecycleOwner(this@ScheduleCreateFragment)

                val dialogForStartTime = MaterialDialog(requireContext())
                    .title(R.string.schedule_create_time_start)
                    .timePicker(startTime) { _, time -> startTime = time }
                    .positiveButton { dialogForEndTime.show() }
                    .onCancel { requireActivity().setTheme(R.style.AppTheme) }
                    .lifecycleOwner(this@ScheduleCreateFragment)

                dialogForStartTime.show()
            }

            includeScheduleCreateLocation.itemScheduleCreate.setOnClickListener {

                findNavController().navigate(R.id.action_scheduleCreateFragment_to_mapFragment)

                //childFragmentManager.let { LocationDialogFragment().show(it, "location") }
            }
            includeScheduleCreateGroup.itemScheduleCreate.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("グループを選択してください。")
                    .setItems((viewModel?.groupsLoadState?.value?.getValueOrNull() as List<ApiGroup>? )?.map { it.groupName }?.toTypedArray()) { _, which ->
                        viewModel?.setSelectedGroup(which)
                    }
                    .show()
            }
        }
    }

    private fun launchUploader() {
        Log.d(ContentValues.TAG, "launchUploader")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)}
        startActivityForResult(
            intent,
            REQUEST_IMAGE_OPEN
        )
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK) {
            val fullPhotoUri: Uri = requireNotNull(data?.data, { "requireNotNull" })
            viewModel.postImageUri(fullPhotoUri)
        }
    }*/



    companion object {
        private const val REQUEST_IMAGE_OPEN = 101
    }

}
