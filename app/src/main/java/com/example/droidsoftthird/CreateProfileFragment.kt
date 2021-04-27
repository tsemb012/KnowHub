package com.example.droidsoftthird

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.databinding.FragmentCreateProfileBinding
import com.example.droidsoftthird.dialogs.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentCreateProfileBinding
    private val viewModel: CreateProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentCreateProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner//lifecycleOwnerのつけ忘れに注意。LiveDataをViewに反映するために必要。
        binding.editProfileImage.setOnClickListener(this)
        binding.editBackgroundImage.setOnClickListener(this)
        binding.btnResidentialArea.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.edit_profile_image -> launchUploader(REQUEST_IMAGE_OPEN_USER)
            R.id.edit_background_image -> launchUploader(REQUEST_IMAGE_OPEN_BACKGROUND)
            R.id.btn_residential_area -> {
                val dialog = ResidentialAreaDialogFragment()
                childFragmentManager?.let { dialog.show(it, "residential_area") }
            }
        }
    }

    private fun launchUploader(request:Int) {
        Log.d(ContentValues.TAG, "launchUploader")
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(
            intent,
            request
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when{
                (requestCode == REQUEST_IMAGE_OPEN_USER && resultCode == Activity.RESULT_OK)
                -> { val fullPhotoUri: Uri = requireNotNull(data?.data, { "requireNotNull" })
                    viewModel.postUserImageUri(fullPhotoUri)}
                (requestCode == REQUEST_IMAGE_OPEN_BACKGROUND && resultCode == Activity.RESULT_OK)
                -> { val fullPhotoUri: Uri = requireNotNull(data?.data, { "requireNotNull" })
                    viewModel.postBackgroundImageUri(fullPhotoUri)}
        }
    }

    companion object {
        private const val REQUEST_IMAGE_OPEN_USER = 102
        private const val REQUEST_IMAGE_OPEN_BACKGROUND = 103
    }
}