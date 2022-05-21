package com.bk.ctsv.teacher.fragment.gift

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.databinding.FragmentTReceiverAddressBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.models.entity.UserAddress
import com.bk.ctsv.teacher.viewmodel.gift.TReceiverAddressViewModel
import com.bk.ctsv.ui.adapter.OnItemClickListener
import com.bk.ctsv.ui.adapter.form.AddressPickerAdapter
import com.bk.ctsv.ui.fragments.form.ChooseDeliveryTypeFragment
import com.bk.ctsv.ui.fragments.gift.ApplyGiftFragment
import javax.inject.Inject

class TReceiverAddressFragment : Fragment(), Injectable, OnItemClickListener<UserAddress> {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: TReceiverAddressViewModel
    private lateinit var addressPickerAdapter: AddressPickerAdapter
    private lateinit var binding : FragmentTReceiverAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        binding = FragmentTReceiverAddressBinding.inflate(inflater, container, false)
        addressPickerAdapter = AddressPickerAdapter(listOf(), ChooseDeliveryTypeFragment.userAddress, this)
        binding.recyclerView.apply {
            adapter = addressPickerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.getListAddress()
        binding.callback = object : RetryCallback {
            override fun retry() {
                viewModel.getListAddress()
            }
        }
        binding.pickAddressButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
        subscribeUI()

        return binding.root
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeUI(){
        with(viewModel) {
            addresses.observe(viewLifecycleOwner){
                binding.status = it.status
                if(checkResource(it) && it.data != null){
                    addressPickerAdapter.userAddressList = it.data
                    addressPickerAdapter.userAddressSelected = TApplyGiftFragment.userAddress
                    addressPickerAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(value: UserAddress) {
        addressPickerAdapter.userAddressSelected = value
        addressPickerAdapter.notifyDataSetChanged()
        TApplyGiftFragment.userAddress = value
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this, factory).get(TReceiverAddressViewModel::class.java)
    }

}