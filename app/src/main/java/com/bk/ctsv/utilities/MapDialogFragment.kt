package com.bk.ctsv.utilities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bk.ctsv.R
import com.bk.ctsv.databinding.MapDialogBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

class MapDialogFragment: DialogFragment(), OnMapReadyCallback {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: MapDialogBinding = DataBindingUtil.inflate(inflater, R.layout.map_dialog, container, false)
        return binding.root
    }

    override fun onMapReady(p0: GoogleMap?) {

    }
}