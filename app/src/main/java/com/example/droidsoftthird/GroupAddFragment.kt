package com.example.droidsoftthird

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.droidsoftthird.databinding.FragmentGroupAddBinding
import com.example.droidsoftthird.dialogs.*
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class
GroupAddFragment : Fragment() {
    private lateinit var binding: FragmentGroupAddBinding
    private val viewModel: GroupAddViewModel by viewModels()
    private val imagePickerLauncher = registerImagePickerLauncher()
    private val imageCropLauncher = registerImageCropLauncher()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group_add, container, false)
        binding.addGroupViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupClickListeners()

        viewModel.activateProgressBar.observe(viewLifecycleOwner, EventObserver {
            binding.progressBar.visibility = View.VISIBLE
        })
        viewModel.navigationToHome.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                GroupAddFragmentDirections.actionAddGroupFragmentToHomeFragment()
            )
        })

        return binding.root
    }

    private fun setupClickListeners() {
        binding.apply {
            btnGroupImage.setOnClickListener { launchUploader() }
            btnToGroupDetailBarActivityArea.setOnClickListener { showAreaDialog() }
            btnToGroupDetailBarFacilityEnvironment.setOnClickListener { showFacilityEnvironmentDialog() }
            btnToGroupDetailBarStyle.setOnClickListener { showStyleDialog() }
            btnToGroupDetailBarGroupType.setOnClickListener { showGroupTypeDialog() }
            btnToGroupDetailBarLearningFrequency.setOnClickListener { showLearningFrequencyDialog() }
            btnToGroupDetailBarAgeRange.setOnClickListener { showAgeRangeDialog() }
            btnToGroupDetailBarNumberPersons.setOnClickListener { showNumberPersonsDialog() }
            btnToGroupDetailBarGenderRestriction.setOnClickListener {
                viewModel.postIsChecked(binding.genderRestrictionSwitch.isChecked)
                showToast(if (binding.genderRestrictionSwitch.isChecked) "性別設定をOnにしました。" else "性別設定をOffにしました。")
            }
            binding.genderRestrictionSwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.postIsChecked(isChecked)
                showToast(if (isChecked) "性別設定をOnにしました。" else "性別設定をOffにしました。")
            }
        }
    }

    private fun showStyleDialog() {
        val dialog = StyleDialogFragment()
        childFragmentManager.let { dialog.show(it, "style") }
    }

    private fun showGroupTypeDialog() {
        val dialog = GroupTypeDialogFragment()
        childFragmentManager.let { dialog.show(it, "group_type") }
    }

    private fun showAreaDialog() {
        val online = Pair (0, 0)
        val dialog = AreaDialogFragment(
            onExceptionListener = {
                viewModel.postPrefecture(getString(R.string.online))
                viewModel.postCity(getString(R.string.no_set))
                viewModel.postCodes(online)
            },
            onConfirmListener = { area ->
                viewModel.postPrefecture(area.prefecture?.name ?: getString(R.string.non_selected))
                viewModel.postCity(area.city?.name ?: getString(R.string.no_set))
                viewModel.postCodes(area.prefecture?.prefectureCode to area.city?.cityCode)
            }
        )
        childFragmentManager.let { dialog.show(it, "activity_area") }
    }

    private fun showFacilityEnvironmentDialog() {
        val dialog = FacilityEnvironmentDialogFragment()
        childFragmentManager.let { dialog.show(it, "facility_environment") }
    }

    private fun showLearningFrequencyDialog() {
        val dialog = LeaningFrequencyDialogFragment()
        childFragmentManager.let { dialog.show(it, "learning_frequency") }
    }

    private fun showAgeRangeDialog() {
        val dialog = AgeRangeDialogFragment()
        childFragmentManager.let { dialog.show(it, "age_range") }
    }

    private fun showNumberPersonsDialog() {
        val dialog = NumberPersonsDialogFragment()
        childFragmentManager.let { dialog.show(it, "number_persons") }
    }

    private fun launchUploader() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }

        imagePickerLauncher.launch(intent)
    }

    private fun registerImagePickerLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val sourceUri = it.data?.data
                if (sourceUri != null) {
                    val destinationUri = Uri.fromFile(createTempFile("ucrop", ".jpg"))
                    val maxWidth = 1920
                    val maxHeight = 1080
                    val intent = UCrop.of(sourceUri, destinationUri)
                        .withAspectRatio(16F, 9F)
                        .withMaxResultSize(maxWidth, maxHeight)
                        .getIntent(requireActivity())
                    imageCropLauncher.launch(intent)
                } else {
                    showToast("画像の取得に失敗しました。")
                }
            }
        }

    private fun registerImageCropLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val resultUri = UCrop.getOutput(result.data!!)
                if (resultUri != null) {
                    viewModel.postImageUri(resultUri)
                }
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                showToast("画像の取得に失敗しました。")
            }
        }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
