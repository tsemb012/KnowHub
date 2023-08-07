package com.tsemb.droidsoftthird

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.tsemb.droidsoftthird.R
import com.tsemb.droidsoftthird.databinding.FragmentProfileEditBinding
import com.tsemb.droidsoftthird.dialogs.AreaDialogFragment
import com.tsemb.droidsoftthird.dialogs.BirthdayDialogFragment
import com.tsemb.droidsoftthird.model.domain_model.Area
import com.tsemb.droidsoftthird.model.domain_model.UserDetail
import com.tsemb.droidsoftthird.model.presentation_model.LoadState
import com.wada811.databinding.dataBinding
import com.yalantis.ucrop.UCrop

abstract class ProfileSubmitFragment : Fragment(R.layout.fragment_profile_edit) {

    companion object {
        private const val DEFAULT_AGE = 30
        const val REQUEST_CODE = "REQUEST_CODE"
        const val REQUEST_CODE_USER_IMAGE = "REQUEST_CODE_USER_IMAGE"
    }

    abstract val viewModel: ProfileSubmitViewModel
    protected val binding: FragmentProfileEditBinding by dataBinding()
    private val imagePickerLauncher = registerImagePickerLauncher()
    private val imageCropLauncher = registerImageCropLauncher()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        bindLoadState()
        setupListeners()
    }

    private fun bindLoadState() {
        viewModel.uiModel.observe(viewLifecycleOwner) { uiModel ->
            when (uiModel.loadState) {
                is LoadState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is LoadState.Loaded<*> -> {
                    binding.progressBar.visibility = View.GONE
                    navigateTo()
                }
                is LoadState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        uiModel.loadState.error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Unit
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            userImageBtn.setOnClickListener { launchImagePicker(REQUEST_CODE_USER_IMAGE) }
            genderItem.itemProfileEdit.setOnClickListener { showGenderDialog() }
            ageItem.itemProfileEdit.setOnClickListener { showAgeDialog() }
            areaItem.itemProfileEdit.setOnClickListener { showAreaDialog() }
        }
        setupSubmitListeners()
    }

    abstract fun navigateTo()

    abstract fun setupSubmitListeners()

    private fun launchImagePicker(typeCode: String) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            putExtra(REQUEST_CODE, typeCode)
        }
        imagePickerLauncher.launch(intent)
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
        BirthdayDialogFragment(
            onPositiveClickListener = { viewModel.postBirthday(it) }
        ).show(childFragmentManager, "birthday")

    private fun showAreaDialog() =
        AreaDialogFragment(
            onExceptionListener = { viewModel.postArea(Area(null, null)) },
            onConfirmListener = { viewModel.postArea(it) },
            titleInt = R.string.residential_area,
            isPrivateTop = true
        ).show(childFragmentManager, "area")

    private fun registerImagePickerLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val sourceUri = it.data?.data
                if (sourceUri != null) {
                    val destinationUri = Uri.fromFile(createTempFile("ucrop", ".jpg"))
                    val maxHeight = 1080
                    val maxWidth = 1080
                    val intent = UCrop.of(sourceUri, destinationUri)
                        .withAspectRatio(9F, 9F)
                        .withMaxResultSize(maxWidth, maxHeight)
                        .getIntent(requireActivity())
                    imageCropLauncher.launch(intent)
                } else {
                    Toast.makeText(requireContext(), "画像の取得に失敗しました。", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun registerImageCropLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val resultUri = UCrop.getOutput(result.data!!)
                if (resultUri != null) {
                    viewModel.storeTemporalUserImage(resultUri)
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                Toast.makeText(requireContext(), "画像の取得に失敗しました。", Toast.LENGTH_SHORT).show()
            }
        }
}