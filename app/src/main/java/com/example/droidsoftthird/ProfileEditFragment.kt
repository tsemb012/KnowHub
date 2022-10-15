package com.example.droidsoftthird

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.databinding.FragmentProfileEditBinding
import com.example.droidsoftthird.dialogs.AreaDialogFragment
import com.example.droidsoftthird.dialogs.SeekBarDialogFragment
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment :Fragment(R.layout.fragment_profile_edit) {

    companion object {
        private const val DEFAULT_AGE = 30
    }

    private val binding: FragmentProfileEditBinding by dataBinding()
    private val viewModel:ProfileEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.fetchUserDetail()
        setupListeners()
    }

    private fun setupListeners() {
        with (binding) {
            genderItem.setOnClickListener { showGenderDialog() }
            ageItem.setOnClickListener { showAgeDialog() }
            areaItem.setOnClickListener { showAreaDialog() }
            submitProfileBtn.setOnClickListener { viewModel.submitProfile() }
        }
    }

    private fun showGenderDialog() =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.profile_edit_select_gender_request))
            .setItems(
                    arrayOf(
                            getString(R.string.profile_edit_gender_male_item),
                            getString(R.string.profile_edit_gender_female_item),
                            getString(R.string.profile_edit_gender_no_answer_item),
                    )
            ) { _, which -> viewModel.postGender(UserDetail.Gender.values()[which]) }
            .show()

    private fun showAgeDialog() =
        SeekBarDialogFragment(
                title = getString(R.string.profile_input_age_dialog_title),
                initNumber = DEFAULT_AGE,
                unit = getString(R.string.age_unit),
                onPositiveClickListener = { viewModel.postAge(it) }
        ).show(childFragmentManager, "age")

    private fun showAreaDialog() =
        AreaDialogFragment(
                onExceptionListener = {},
                onConfirmListener = { viewModel.postArea(it) }
        ).show(childFragmentManager, "area")
}
