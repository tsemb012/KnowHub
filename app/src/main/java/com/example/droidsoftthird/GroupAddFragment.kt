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
import com.example.droidsoftthird.dialogs.*
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupAddFragment:Fragment(),View.OnClickListener {

    private lateinit var binding: FragmentGroupAddBinding
    private val viewModel:GroupAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_group_add, container, false
        )

        binding.addGroupViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner//lifecycleOwnerのつけ忘れに注意。LiveDataをViewに反映するために必要。

        binding.btnGroupImage.setOnClickListener(this)
        binding.btnToGroupDetailBarGroupType.setOnClickListener(this)
        binding.btnToGroupDetailBarActivityArea.setOnClickListener(this)
        binding.btnToGroupDetailBarFacilityEnvironment.setOnClickListener(this)
        binding.btnToGroupDetailBarLearningFrequency.setOnClickListener(this)
        binding.btnToGroupDetailBarAgeRange.setOnClickListener(this)
        binding.btnToGroupDetailBarNumberPersons.setOnClickListener(this)
        binding.btnToGroupDetailBarGenderRestriction.setOnClickListener(this)

        viewModel.activateProgressBar.observe(viewLifecycleOwner,EventObserver{
            binding.progressBar.visibility = View.VISIBLE
        })

        viewModel.navigationToHome.observe(viewLifecycleOwner,EventObserver{
            findNavController().navigate(
                GroupAddFragmentDirections.actionAddGroupFragmentToHomeFragment())
        })

        return binding.root
    }

    override fun onClick(v: View?) {//TODO 関心の分離のため、Eventクラスを用いてクリックイベントをViewModelに移行する。
        when(v!!.id){
            R.id.btn_group_image -> launchUploader()
            R.id.btn_to_groupDetailBar_group_type -> {
                val dialog = GroupTypeDialogFragment()
                childFragmentManager?.let { dialog.show(it, "group_type") }
            }
            R.id.btn_to_groupDetailBar_activity_area -> {
                val dialog = AreaDialogFragment (
                    onExceptionListener =
                        { TODO("オンラインを選択した時の処理を記述する。")},
                    onConfirmListener =
                        { area ->
                            viewModel.postPrefecture(area?.prefecture?.name ?: getString(R.string.non_selected))
                            viewModel.postCity(area.city?.name ?: getString(R.string.non_selected))
                            viewModel.postCodes(area.prefecture?.prefectureCode to area.city?.cityCode)
                        }
                )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK) {
            val fullPhotoUri: Uri = requireNotNull(data?.data, { "requireNotNull" })
            viewModel.postImageUri(fullPhotoUri)
            }
        }



    companion object {
        private const val REQUEST_IMAGE_OPEN = 101
    }

}
