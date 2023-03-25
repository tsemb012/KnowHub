package com.example.droidsoftthird

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.datetime.timePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.example.droidsoftthird.databinding.FragmentScheduleCreateBinding
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
        setupClickActions()
        viewModel.initializeGroups()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupClickActions() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            setupDateDialog()
            setupPeriodDialog()
            setupMapNav()
            setupGroupDialog()
            setupSwitch()
        }
    }

    private fun FragmentScheduleCreateBinding.setupGroupDialog() {
        includeScheduleCreateGroup.itemScheduleCreate.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("グループを選択してください。")
                .setItems(viewModel?.groupArray?: arrayOf()) { _, which ->
                    viewModel?.postSelectedGroup(which)
                }
                .show()
        }
    }

    private fun FragmentScheduleCreateBinding.setupPeriodDialog() {
        includeScheduleCreateTime.itemScheduleCreate.setOnClickListener {

            //Themeを直接変更できないので、ContextからThemeの変更を行う。
            requireActivity().setTheme(R.style.AppTheme_MaterialDialogTime)

            var startTime: Calendar = Calendar.getInstance()

            val dialogForEndTime = MaterialDialog(requireContext())
                .title(R.string.schedule_create_time_end)
                .timePicker { _, endTime ->
                    viewModel?.postTimePeriod(startTime, endTime) ?: IllegalStateException()
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
    }

    private fun FragmentScheduleCreateBinding.setupDateDialog() {
        includeScheduleCreateDate.itemScheduleCreate.setOnClickListener {
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
                        val selectedDate =
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        viewModel?.postSelectedDate(selectedDate)
                        requireActivity().setTheme(R.style.AppTheme)
                    }
                    addOnCancelListener { requireActivity().setTheme(R.style.AppTheme) }
                }
            childFragmentManager.let { dialog.show(it, "schedule_date") }
        }
    }

    private fun FragmentScheduleCreateBinding.setupMapNav() {
        includeScheduleCreateLocation.itemScheduleCreate.setOnClickListener {
            findNavController().navigate(R.id.action_scheduleCreateFragment_to_mapFragment)
        }
    }

    private fun FragmentScheduleCreateBinding.setupSwitch() {
        isOnline.setOnClickListener {
            isOnlineSwitch.toggle()
            viewModel?.postIsOnline(isOnlineSwitch.isChecked)
            if (isOnlineSwitch.isChecked) {
                includeScheduleCreateLocation.itemScheduleCreateText.text = "オンライン"
                includeScheduleCreateLocation.itemScheduleCreate.isEnabled = false
                includeScheduleCreateLocation.itemScheduleCreateText.setTextColor(resources.getColor(R.color.primary_text_grey))
            } else {
                includeScheduleCreateLocation.itemScheduleCreateText.text = viewModel?.uiModel?.value?.uiPlace
                includeScheduleCreateLocation.itemScheduleCreate.isEnabled = true
                includeScheduleCreateLocation.itemScheduleCreateText.setTextColor(resources.getColor(R.color.primary_dark))
            }
        }
    }
}
