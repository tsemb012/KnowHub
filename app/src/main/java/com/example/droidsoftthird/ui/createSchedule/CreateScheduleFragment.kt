package com.example.droidsoftthird.ui.createSchedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.R
import com.example.droidsoftthird.databinding.CreateScheduleFragmentBinding
import com.example.droidsoftthird.dialogs.GroupTypeDialogFragment
import com.example.droidsoftthird.dialogs.ResidentialAreaDialogFragment
import com.example.droidsoftthird.dialogs.TimePickerStartFragment
import com.example.droidsoftthird.ui.createProfile.CreateProfileFragment
import com.mcsoft.timerangepickerdialog.RangeTimePickerDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateScheduleFragment : Fragment(), RangeTimePickerDialog.ISelectedTime{

    private lateinit var binding: CreateScheduleFragmentBinding
    private val viewModel: CreateScheduleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = CreateScheduleFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner//lifecycleOwnerのつけ忘れに注意。LiveDataをViewに反映するために必要。

        //TODO calendarViewライブラリを使用する際に 、includeDateのテキストと連動させる。
        //TODO calendarViewライブラリとViewModelを連動させて、ViewModelの結果をLayoutに反映させる。
        //TODO ApprovedScheduleを参照する。
        //viewModl.xxxx = calendarview.yyyyy

        binding.includeTime.clickListener = View.OnClickListener{
            val dialog = TimePickerStartFragment()
            childFragmentManager?.let { dialog.show(it, "time_picker") }
        }

        binding.includeLocation.clickListener = View.OnClickListener{
            val dialog = GroupTypeDialogFragment()
            childFragmentManager?.let { dialog.show(it, "group_type") }
        }

        binding.includeGroup.clickListener = View.OnClickListener{
            val dialog = GroupTypeDialogFragment()
            childFragmentManager?.let { dialog.show(it, "group_type") }
        }

            return binding.root
        }

    override fun onSelectedTime(p0: Int, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }
}

    override fun onClick(v: View?) {//TODO 関心の分離のため、Eventクラスを用いてクリックイベントをViewModelに移行する。
        when (v!!.id) {
            R.id.edit_profile_image -> launchUploader(CreateProfileFragment.REQUEST_IMAGE_OPEN_USER)
            R.id.edit_background_image -> launchUploader(CreateProfileFragment.REQUEST_IMAGE_OPEN_BACKGROUND)
            R.id.btn_residential_area -> {
                val dialog = ResidentialAreaDialogFragment()
                childFragmentManager?.let { dialog.show(it, "residential_area") }
            }
        }






}


