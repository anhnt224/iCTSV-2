package com.bk.ctsv.modules.searchMotel.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.databinding.FragmentMotelRegistrationListBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
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

    companion object {
        var reloadData = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        binding = FragmentMotelRegistrationListBinding.inflate(inflater, container, false)
        setupRecyclerView()

        if (reloadData){
            reloadData = false
            viewModel.getMotelRegistrationList()
        }

        binding.apply {
            fab.setOnClickListener {
                navigateToRegisterToFindMotel()
            }

            callback = object: RetryCallback {
                override fun retry() {
                    viewModel.getMotelRegistrationList()
                }
            }
        }
        subscribeUI()
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, factory).get(MotelRegistrationListViewModel::class.java)
    }

    private fun subscribeUI(){
        with(viewModel){
            motelRegistrationList.observe(viewLifecycleOwner){
                binding.status = it.status
                if (checkResource(it) && it.data != null){
                    adapter.submitList(it.data)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = MotelRegistrationAdapter(listOf(), this)
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

    private fun navigateToRegisterToFindMotel() {
        RegisterToFindMotelFragment.selectedLocation = null
        RegisterToFindMotelFragment.address = ""
        val action =
            MotelRegistrationListFragmentDirections.actionMotelRegistrationListFragmentToRegisterToFindMotelFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
}