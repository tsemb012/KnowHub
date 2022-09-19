package com.example.droidsoftthird.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import com.example.droidsoftthird.GroupAddViewModel
import com.example.droidsoftthird.R
import com.example.droidsoftthird.ScheduleCreateViewModel
import com.example.droidsoftthird.databinding.DialogLocationBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.slider.RangeSlider
import com.wada811.databinding.dataBinding

class LocationDialogFragment: DialogFragment(), OnMapReadyCallback {

    private val viewModel: ScheduleCreateViewModel by viewModels({requireParentFragment()})

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = layoutInflater.inflate(R.layout.dialog_location, null, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return AlertDialog.Builder(requireActivity())
                .setTitle("地図(仮)")
                .setPositiveButton(R.string.done) {_, _ -> }
                .setNeutralButton(R.string.cancel) { _, _ -> }
                .setView(view)
                .create()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .title("Marker")
        )
    }
}
