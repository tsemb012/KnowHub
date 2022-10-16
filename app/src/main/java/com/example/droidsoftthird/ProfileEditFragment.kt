package com.example.droidsoftthird

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.databinding.FragmentProfileEditBinding
import com.example.droidsoftthird.dialogs.AreaDialogFragment
import com.example.droidsoftthird.dialogs.SeekBarDialogFragment
import com.example.droidsoftthird.model.domain_model.UserDetail
import com.example.droidsoftthird.model.fire_model.LoadState
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileEditFragment :Fragment(R.layout.fragment_profile_edit) {

    companion object {
        private const val DEFAULT_AGE = 30
        const val REQUEST_CODE = "REQUEST_CODE"
        const val REQUEST_CODE_USER_IMAGE = "REQUEST_CODE_USER_IMAGE"
        const val REQUEST_CODE_BACKGROUND_IMAGE = "REQUEST_CODE_BACKGROUND_IMAGE"
    }

    private val binding: FragmentProfileEditBinding by dataBinding()
    private val viewModel: ProfileEditViewModel by viewModels()
    private val launcher = registerLauncher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchUserDetail()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        bindLoadState()
        setupListeners()
    }

    private fun bindLoadState() { //TODO エラーハンドリングを共通化する。
        viewModel.uiModel.observe(viewLifecycleOwner) { uiModel ->
            when (uiModel.loadState) {
                is LoadState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is LoadState.Loaded<*> -> binding.progressBar.visibility = View.GONE
                is LoadState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), uiModel.loadState.error.message, Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            btnUserImage.setOnClickListener { launchUploader(REQUEST_CODE_USER_IMAGE) }
            btnBackgroundImage.setOnClickListener { launchUploader(REQUEST_CODE_BACKGROUND_IMAGE) }
            genderItem.setOnClickListener { showGenderDialog() }
            ageItem.setOnClickListener { showAgeDialog() }
            areaItem.setOnClickListener { showAreaDialog() }
            submitProfileBtn.setOnClickListener { viewModel.submitProfile() }
        }
    }

    private fun launchUploader(typeCode: String) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            putExtra(REQUEST_CODE, typeCode)
        }
        launcher.launch(intent)
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


    private fun registerLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val requestCode = it.data?.getStringExtra(REQUEST_CODE)
                val uri = it.data?.data
                if (uri != null) {
                    when (requestCode) {
                        REQUEST_CODE_USER_IMAGE -> viewModel.storeTemporalUserImage(uri)
                        REQUEST_CODE_BACKGROUND_IMAGE -> viewModel.storeTemporalBackgroundImage(uri)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "画像の取得に失敗しました。", Toast.LENGTH_SHORT).show()
            }
        }
}
