package com.bk.ctsv.ui.fragments.motel

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.databinding.SearchMotelFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.models.entity.Motel
import com.bk.ctsv.ui.adapter.MotelInfoAdapter
import com.bk.ctsv.ui.fragments.user.AddNewAddressFragment
import com.bk.ctsv.ui.viewmodels.motel.SearchMotelViewModel
import com.bk.ctsv.utilities.DEFAULT_LATLNG
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class SearchMotelFragment : Fragment(),
    Injectable,
    MotelInfoAdapter.OnItemMotelInfoClickListener,
    OnMapReadyCallback{

    private lateinit var viewModel: SearchMotelViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: SearchMotelFragmentBinding
    private var listMotel = arrayListOf<Motel>()
    private lateinit var motelInfoAdapter: MotelInfoAdapter
    private lateinit var googleMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        binding = DataBindingUtil.inflate(inflater,
            R.layout.search_motel_fragment,
            container,
            false)

        binding.map.getMapAsync(this)
        binding.apply {
            map.onCreate(savedInstanceState)
            map.onResume()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity().baseContext)

        binding.apply {
            constraintMotelInfoShow.visibility = View.GONE
            viewShowMotelInfo.setOnClickListener {
                constraintMotelInfoShow.visibility = View.VISIBLE
                viewShowMotelInfo.visibility = View.GONE
            }
            constraintMotelInfoShow.setOnClickListener {
                constraintMotelInfoShow.visibility = View.GONE
                viewShowMotelInfo.visibility = View.VISIBLE
            }
        }


        setUpRecyclerViewMotelInfo()

        return binding.root
    }

    private fun setUpRecyclerViewMotelInfo() {
        motelInfoAdapter = MotelInfoAdapter(listMotel , this)
        binding.recyclerViewMotelInfo.apply {
            adapter = motelInfoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }


    private fun setupViewModel(){
        viewModel = ViewModelProvider(this, factory).get(SearchMotelViewModel::class.java)
    }

    override fun navigateToMotelInfoDetailFragment() {
        TODO("Not yet implemented")
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        googleMap.uiSettings.isZoomControlsEnabled = true
        setUpMap()

        googleMap.setOnMapClickListener {latLng ->
            googleMap.clear()
            AddNewAddressFragment.newLatLng = latLng
            fillLocationInfo(latLng)
            googleMap.addMarker(MarkerOptions().position(latLng))
        }

        if (
            binding.map.findViewById<View>("1".toInt()) != null
        ) {
            // Get the button view
            val locationButton = (binding.map.findViewById<View>("1".toInt())
                .parent as View).findViewById<View>("2".toInt())
            // and next place it, on bottom right (as Google Maps app)
            val layoutParams =
                locationButton.layoutParams as RelativeLayout.LayoutParams
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 30, 30)
        }
    }

    private fun setUpMap(){
        googleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener{
            if (it != null){
                lastLocation = it
                val currentLatLong = LatLng(it.latitude, it.longitude)
                placeMarkerOnMap(currentLatLong)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        googleMap.addMarker(markerOptions)
    }

    @SuppressLint("SetTextI18n")
    private fun fillLocationInfo(latLng: LatLng){
        if(latLng == DEFAULT_LATLNG){
            return
        }
        binding.apply {
            binding.textViewAddress.isEnabled = false
            binding.textViewAddress.setText("* Toạ độ: %.4f°B - %.4f°Đ\", latLng.latitude, latLng.longitude")
            //locationText.text = String.format("* Toạ độ: %.4f°B - %.4f°Đ", latLng.latitude, latLng.longitude)
        }

        val addresses: List<Address>
        val geocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )
            if(addresses.isNotEmpty()){
                val address: String = addresses[0]
                    .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                /*binding.textViewAddress.isEnabled = false
                binding.textViewAddress.setText("$address")*/
            }
        }catch (e: Exception){

        }
    }


}