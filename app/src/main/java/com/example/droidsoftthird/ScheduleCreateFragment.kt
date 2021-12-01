package com.example.droidsoftthird

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.droidsoftthird.databinding.FragmentGroupAddBinding
import com.example.droidsoftthird.databinding.FragmentScheduleCreateBinding
import com.example.droidsoftthird.dialogs.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

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
    private val viewModel:ScheduleCreateViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickAction()
    }

    private fun setupClickAction() {
        with(binding) {

            lifecycleOwner = viewLifecycleOwner

            includeScheduleCreateDate.itemScheduleCreate.setOnClickListener{
                val dialog = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.schedule_create_date_hint)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    //TODO .setCalendarConstraints()
                    .build().also {
                        it.addOnPositiveButtonClickListener {
                            val selectedDate = LocalDate.ofEpochDay(it)
                            //TODO 選択した日をViewModelに突っ込む。
                        }
                    }
                childFragmentManager.let { dialog.show(it, "schedule_date") }
            }

            includeScheduleCreateTime.itemScheduleCreate.setOnClickListener{
                val dialogForStartTime = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(10)
                    .setInputMode(INPUT_MODE_CLOCK)
                    .setTitleText("開始時間")
                    .build()
                val dialogForEndTime = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(10)
                    .setInputMode(INPUT_MODE_CLOCK)
                    .setTitleText("終了時間")
                    .build()
                childFragmentManager.let {
                    dialogForStartTime.show(it, "schedule_date_start")
                }
                dialogForStartTime.addOnPositiveButtonClickListener {
                    childFragmentManager.let { dialogForEndTime.show(it, "schedule_date_end") }
                }
                dialogForEndTime.addOnPositiveButtonClickListener {
                    Log.i("hour",dialogForStartTime.hour.toString())
                    Log.i("minute",dialogForStartTime.minute.toString())
                    Log.i("hour",dialogForEndTime.hour.toString())
                    Log.i("minute",dialogForEndTime.minute.toString())
                    //TODO 取得したデータをViewModelに送って加工する。
                }
            }
            fun showDialogForEndTime() {}
            /*includeScheduleCreateLocation.itemScheduleCreate.setOnClickListener()
            includeScheduleCreateGroup.itemScheduleCreate.setOnClickListener()*/
        }
    }



    /*override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_to_groupDetailBar_group_type -> {
                val dialog = GroupTypeDialogFragment()
                childFragmentManager?.let { dialog.show(it, "group_type") }
            }
            R.id.btn_to_groupDetailBar_activity_area -> {
                val dialog = ActivityAreaDialogFragment()
                childFragmentManager?.let { dialog.show(it, "activity_area") }
            }
            R.id.btn_to_groupDetailBar_facility_environment -> {
                val dialog = FacilityEnvironmentDialogFragment()
                childFragmentManager?.let { dialog.show(it, "facility_environment") }
            }
            R.id.btn_to_groupDetailBar_learning_frequency -> {
                val dialog = LeaningFrequencyDialogFragment()
                childFragmentManager?.let { dialog.show(it, "learning_frequency") }
            }
            R.id.btn_to_groupDetailBar_age_range -> {
                val dialog = AgeRangeDialogFragment()
                childFragmentManager?.let { dialog.show(it, "age_range") }
            }
            R.id.btn_to_groupDetailBar_number_persons -> {
                val dialog = NumberPersonsDialogFragment()
                childFragmentManager?.let { dialog.show(it, "number_persons") }
            }
            R.id.btn_to_groupDetailBar_gender_restriction -> {
                val sm: SwitchMaterial = binding.genderRestrictionSwitch
                sm.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.postIsChecked(isChecked)
                    if (isChecked) {
                        Toast.makeText(context, "性別設定をOnにしました。", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "性別設定をOffにしました。", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }*/

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
