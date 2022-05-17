package com.bk.ctsv.ui.fragments.motel

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.databinding.SearchMotelFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.models.entity.Motel
import com.bk.ctsv.ui.adapter.MotelInfoAdapter
import com.bk.ctsv.ui.viewmodels.motel.SearchMotelViewModel
import com.bk.ctsv.utilities.DEFAULT_LATLNG
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
    private var listMotel = listOf<Motel>()
    private lateinit var motelInfoAdapter: MotelInfoAdapter
    private lateinit var googleMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOG = "_SearchMotelFragment"
    private var radius = 1000.0

    @SuppressLint("NotifyDataSetChanged")
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

        subscribeUI()

        setUpRecyclerViewMotelInfo()

        setAndShowBottomBar()

        return binding.root
    }

    private fun setAndShowBottomBar() {
        binding.apply {
            constraintMotelInfoShow.visibility = View.GONE
            viewShowMotelInfo.setOnClickListener {
                constraintMotelInfoShow.visibility = View.VISIBLE
                viewShowMotelInfo.visibility = View.GONE
                val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_to_top)
                binding.viewListMotelInfo.startAnimation(anim)
            }

            constraintMotelInfoShow.setOnClickListener {
                val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.top_to_bottom)
                binding.viewListMotelInfo.startAnimation(anim)
                anim.setAnimationListener(object : Animation.AnimationListener {
                    @SuppressLint("ShowToast")
                    override fun onAnimationStart(animation: Animation?) {
                        Toast.makeText(requireContext(), "Toast", Toast.LENGTH_SHORT)
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        constraintMotelInfoShow.visibility = View.GONE
                        viewShowMotelInfo.visibility = View.VISIBLE
                    }

                    @SuppressLint("ShowToast")
                    override fun onAnimationRepeat(animation: Animation?) {
                        Toast.makeText(requireContext(), "Toast", Toast.LENGTH_SHORT)
                    }

                })

            }


        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpDistanceDialog(requireContext: Context, latLng: LatLng) {
        val builder = AlertDialog.Builder(requireContext)
        builder.setTitle("Chọn bán kính")

        val distance = arrayOf("1 km", "2km", "3km", "5km")
        builder.setItems(distance){ dialog, which ->
            when (which){
                0 -> {
                    radius = 1000.0
                    binding.showSelectDistance1.text = "1km"
                    binding.showSelectDistance2.text = "1km"
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions()
                        .position(latLng))
                        .setIcon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_my_position))
                    drawCircle(latLng, radius)
                }
                1 -> {
                    radius = 2000.0
                    binding.showSelectDistance1.text = "2km"
                    binding.showSelectDistance2.text = "2km"
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions()
                        .position(latLng))
                        .setIcon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_my_position))
                    drawCircle(latLng, radius)
                }
                2 -> {
                    radius = 3000.0
                    binding.showSelectDistance1.text = "3km"
                    binding.showSelectDistance2.text = "3km"
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions()
                        .position(latLng))
                        .setIcon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_my_position))
                    drawCircle(latLng, radius)
                }
                3 -> {
                    radius = 5000.0
                    binding.showSelectDistance1.text = "5km"
                    binding.showSelectDistance2.text = "5km"
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions()
                        .position(latLng))
                        .setIcon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_my_position))
                    drawCircle(latLng, radius)
                }
            }
            viewModel.getListMotel(latLng.latitude, latLng.longitude, radius.toInt())
            subscribeUI()
        }

        val dialog = builder.create()
        dialog.show()
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

    override fun navigateToMotelInfoDetailFragment(motel: Motel) {
        val action = SearchMotelFragmentDirections.actionSearchMotelFragmentToMotelInfoFragment(motel)
        Navigation.findNavController(requireView()).navigate(action)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        googleMap.uiSettings.isZoomControlsEnabled = true
        setUpMap()

        googleMap.setOnMapClickListener {latLng ->
            googleMap.clear()
            fillLocationInfo(latLng)
            drawCircle(latLng, radius)
            googleMap.addMarker(MarkerOptions()
                .position(latLng))
                .setIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_my_position))
            val rd = radius.toInt()
            viewModel.getListMotel(latLng.latitude, latLng.longitude, rd)
            binding.apply {
                showSelectDistance1.setOnClickListener {
                    setUpDistanceDialog(requireContext(), latLng)
                }

                showSelectDistance2.setOnClickListener {
                    setUpDistanceDialog(requireContext(), latLng)
                }
            }
        }

        /*if (
            binding.map.findViewById<View>("1".toInt()) != null
        ) {
            // Get the button view
            *//*val locationButton = (binding.map.findViewById<View>("1".toInt())
                .parent as View).findViewById<View>("2".toInt())
            // and next place it, on bottom right (as Google Maps app)
            val layoutParams =
                locationButton.layoutParams as RelativeLayout.LayoutParams
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 30, 30)*//*
        }*/
    }

    private fun addMotelMarker(motelList: List<Motel>, ggMap: GoogleMap) {
        for (motel in motelList){
            val latLng = LatLng(motel.latitude, motel.longitude)
            ggMap.addMarker(MarkerOptions()
                .position(latLng))
                .setIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_motel))
        }
    }

    private fun drawCircle(latLng: LatLng, rad: Double) {
        googleMap.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(rad)
                .strokeWidth(2f)
                .strokeColor(Color.argb(100, 183, 32, 39))
                .fillColor(Color.argb(15, 183, 32, 39))
        )
    }

    private fun setUpMap(){
        googleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener{
            if (it != null){
                lastLocation = it
                val currentLatLong = LatLng(it.latitude, it.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun fillLocationInfo(latLng: LatLng){
        if(latLng == DEFAULT_LATLNG){
            return
        }

        binding.apply {
            binding.textViewAddress.isEnabled = false
            binding.textViewAddress
                .setText(String.
                format("* Toạ độ: %.4f°B - %.4f°Đ", latLng.latitude, latLng.longitude))
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
            }
        }catch (e: Exception){

        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    fun subscribeUI(){
        with(viewModel){
            motelList.observe(viewLifecycleOwner){
                if (checkResource(it) && it.data != null){
                    binding.getActivityStatus = it.status
                    motelInfoAdapter.listMotel = it.data
                    addMotelMarker(it.data, googleMap)
                    binding.apply {
                        textViewSizeOfListMotel.text = "Có ${it.data.size} nhà trọ ở gần bạn"
                        textViewNumberOfListMotel.text = "Có ${it.data.size} nhà trọ ở gần bạn"
                    }
                    motelInfoAdapter.notifyDataSetChanged()
                }
                Log.v("_SearchMotelFragment", "${motelInfoAdapter.listMotel}")

            }
        }
    }


}