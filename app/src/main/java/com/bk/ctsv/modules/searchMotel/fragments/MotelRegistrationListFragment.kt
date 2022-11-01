package com.bk.ctsv.modules.searchMotel.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.databinding.FragmentMotelRegistrationListBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.modules.searchMotel.adapter.MotelRegistrationAdapter
import com.bk.ctsv.modules.searchMotel.adapter.MotelRegistrationListener
import com.bk.ctsv.modules.searchMotel.model.MotelRegistration
import com.bk.ctsv.modules.searchMotel.model.MotelRegistrationStatus
import com.bk.ctsv.modules.searchMotel.viewModels.MotelRegistrationListViewModel
import javax.inject.Inject

class MotelRegistrationListFragment : Fragment(), Injectable, MotelRegistrationListener {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: MotelRegistrationListViewModel
    private lateinit var binding: FragmentMotelRegistrationListBinding
    private lateinit var adapter: MotelRegistrationAdapter

    private var motelRegistrationList: List<MotelRegistration> = listOf(
        MotelRegistration(
            locationName = "356A Giải phóng",
            address = "Hoàng Văn Thụ, Hoàng Mai",
            type = "Nhà trọ không chung chủ, chung cư mini",
            numberOfPeople = 2,
            minPrice = 2000000,
            maxPrice = 2500000,
            userRequests = "Vệ sinh khép kín, có điều hoà",
            liveWithOther = false,
            startDate = "2022-11-01 00:00:00",
            endDate = "2022-11-11 00:00:00",
            statusCode = 1
        ),
        MotelRegistration(
            locationName = "356A Giải phóng",
            address = "Hoàng Văn Thụ, Hoàng Mai",
            type = "Nhà trọ không chung chủ, chung cư mini",
            numberOfPeople = 2,
            minPrice = 2000000,
            maxPrice = 2500000,
            userRequests = "Vệ sinh khép kín, có điều hoà",
            liveWithOther = false,
            startDate = "2022-11-01 00:00:00",
            endDate = "2022-11-11 00:00:00",
            statusCode = 2
        ),
        MotelRegistration(
            locationName = "356A Giải phóng",
            address = "Hoàng Văn Thụ, Hoàng Mai",
            type = "Nhà trọ không chung chủ, chung cư mini",
            numberOfPeople = 2,
            minPrice = 2000000,
            maxPrice = 2500000,
            userRequests = "Vệ sinh khép kín, có điều hoà",
            liveWithOther = false,
            startDate = "2022-11-01 00:00:00",
            endDate = "2022-11-11 00:00:00",
            statusCode = 3
        ),
        MotelRegistration(
            locationName = "356A Giải phóng",
            address = "Hoàng Văn Thụ, Hoàng Mai",
            type = "Nhà trọ không chung chủ, chung cư mini",
            numberOfPeople = 2,
            minPrice = 2000000,
            maxPrice = 2500000,
            userRequests = "Vệ sinh khép kín, có điều hoà",
            liveWithOther = false,
            startDate = "2022-11-01 00:00:00",
            endDate = "2022-11-11 00:00:00",
            statusCode = 4
        ),
        MotelRegistration(
            locationName = "356A Giải phóng",
            address = "Hoàng Văn Thụ, Hoàng Mai",
            type = "Nhà trọ không chung chủ, chung cư mini",
            numberOfPeople = 2,
            minPrice = 2000000,
            maxPrice = 2500000,
            userRequests = "Vệ sinh khép kín, có điều hoà",
            liveWithOther = false,
            startDate = "2022-11-01 00:00:00",
            endDate = "2022-11-11 00:00:00",
            statusCode = 5
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        binding = FragmentMotelRegistrationListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory).get(MotelRegistrationListViewModel::class.java)
    }

    private fun setupRecyclerView() {
        adapter = MotelRegistrationAdapter(motelRegistrationList, this)
        binding.itemList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@MotelRegistrationListFragment.adapter
        }
    }

    override fun onItemClick(motelRegistration: MotelRegistration) {
        when (motelRegistration.getStatus()) {
            MotelRegistrationStatus.PROCESSING -> navigateToMotelRegistrationProcessing(
                motelRegistration
            )
            MotelRegistrationStatus.COMPLETION -> navigateToMotelRegistrationComplete(
                motelRegistration
            )
            else -> navigateToMotelRegistrationInfo(motelRegistration)
        }
    }

    private fun navigateToMotelRegistrationInfo(motelRegistration: MotelRegistration) {
        val action =
            MotelRegistrationListFragmentDirections.actionMotelRegistrationListFragmentToMotelRegistrationInfoFragment(
                motelRegistration
            )
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToMotelRegistrationProcessing(motelRegistration: MotelRegistration) {
        val action =
            MotelRegistrationListFragmentDirections.actionMotelRegistrationListFragmentToMotelRegistrationProcessingFragment(
                motelRegistration
            )
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToMotelRegistrationComplete(motelRegistration: MotelRegistration) {
        val action =
            MotelRegistrationListFragmentDirections.actionMotelRegistrationListFragmentToMotelRegistrationCompleteFragment(
                motelRegistration
            )
        Navigation.findNavController(requireView()).navigate(action)
    }

}