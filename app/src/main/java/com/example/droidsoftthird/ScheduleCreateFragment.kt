package com.example.droidsoftthird

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.example.droidsoftthird.databinding.FragmentScheduleCreateBinding
import com.example.droidsoftthird.model.presentation_model.ScheduleCreateUiModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException
import java.time.*

@AndroidEntryPoint
class ScheduleCreateFragment:Fragment(R.layout.fragment_schedule_create) {

    private val binding: FragmentScheduleCreateBinding by dataBinding()
    private val viewModel:ScheduleCreateViewModel by hiltNavGraphViewModels(R.id.schedule_graph)
    private val groupId by lazy { arguments?.getString("groupId") }
    private val isNavigatedFromChatGroup by lazy { arguments?.getBoolean("isNavigatedFromChatGroup") }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickActions()
        viewModel.initializeGroups()
        binding.includeScheduleCreateGroup.itemScheduleCreate.isEnabled = isNavigatedFromChatGroup == false
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.uiModel.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it.isLoading
            if (canInsertGroupIdFromPreviousScreen(it)) initializeGroup()
        }
    }

    private fun setupClickActions() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            setupDateDialog()
            setupTimeDialog()
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

    private fun FragmentScheduleCreateBinding.setupTimeDialog() {
        includeScheduleCreateTime.itemScheduleCreate.setOnClickListener {

            var startTime: ZonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
            val durationLayout = layoutInflater.inflate(R.layout.dialog_duration_picker, null)
            val hoursPicker = durationLayout.findViewById<NumberPicker>(R.id.hoursPicker).apply {
                minValue = 0
                maxValue = 12
            }
            val minutesPicker = durationLayout.findViewById<NumberPicker>(R.id.minutesPicker).apply {
                minValue = 0
                maxValue = 3
                displayedValues = arrayOf("00", "15", "30", "45")
            }

            val durationDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("何時間何分、開催しますか？")
                .setView(durationLayout)
                .setPositiveButton("OK") { _, _ ->
                    val durationHours = hoursPicker.value
                    val durationMinutes = minutesPicker.value * 15
                    val duration = Duration.ofHours(durationHours.toLong()).plusMinutes(durationMinutes.toLong())
                    viewModel?.postTimePeriod(startTime, duration) ?: IllegalStateException()
                }
                .setNegativeButton("キャンセル", null)
                .create()

            val dialogForStartTime = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setTitleText(R.string.schedule_create_time_start)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build()
            dialogForStartTime.addOnPositiveButtonClickListener {//TODO カラーを変更する。
                startTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                    .withHour(dialogForStartTime.hour)
                    .withMinute(dialogForStartTime.minute)
                durationDialog.show()
            }

            dialogForStartTime.show(childFragmentManager, "schedule_create_time_start")
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

    private fun initializeGroup() {
        groupId?.let { viewModel.initGroup(it) }
        if (isNavigatedFromChatGroup == true) binding.includeScheduleCreateGroup.itemScheduleCreateText.setTextColor(
            resources.getColor(R.color.gray, null)
        )
    }

    private fun canInsertGroupIdFromPreviousScreen(uiModel: ScheduleCreateUiModel) =
        !uiModel.isLoading && uiModel.groups?.isNotEmpty() == true && uiModel.selectedItems.selectedGroup == null
}
