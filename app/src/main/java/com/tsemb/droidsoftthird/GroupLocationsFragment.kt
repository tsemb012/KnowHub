package com.tsemb.droidsoftthird

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tsemb.droidsoftthird.composable.map.OSMBottomSheetScreen
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration

@AndroidEntryPoint
class GroupLocationsFragment:Fragment() {

    private val viewModel: GroupLocationsViewModel by viewModels()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.getCountByArea()
            } else {

            }
        }
        requestLocationPermission()
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osmdroid", 0))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                OSMBottomSheetScreen(
                    viewModel,
                    this@GroupLocationsFragment,
                    ::navigateToGroupDetail,
                    ::navigateToGroupAdd
                )
            }
        }
    }

    fun navigateToGroupDetail(groupId: String) {
        this.findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToGroupDetailFragment(groupId)
        )
    }

    private fun navigateToGroupAdd() {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToAddGroupFragment()
        )
    }

    private fun requestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.getCountByArea()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {

            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}
