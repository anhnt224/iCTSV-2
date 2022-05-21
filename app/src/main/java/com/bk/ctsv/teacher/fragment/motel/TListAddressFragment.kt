package com.bk.ctsv.teacher.fragment.motel

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.databinding.FragmentTListAddressBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.UserAddress
import com.bk.ctsv.teacher.viewmodel.motel.TListAddressViewModel
import com.bk.ctsv.ui.adapter.user.AddressAdapter
import com.bk.ctsv.ui.fragments.user.ListAddressFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class TListAddressFragment : Fragment(), Injectable, AddressAdapter.OnItemButtonClickListener {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var binding : FragmentTListAddressBinding
    private lateinit var viewModel: TListAddressViewModel
    private lateinit var addressAdapter: AddressAdapter
    private var addressSelected: UserAddress? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        setHasOptionsMenu(true)
        binding = FragmentTListAddressBinding.inflate(inflater, container, false)
        addressAdapter = AddressAdapter(arrayListOf(), this)
        viewModel.getListAddress()
        binding.apply {
            recyclerview.apply {
                adapter = addressAdapter
                layoutManager = LinearLayoutManager(context)
            }
            callback = object : RetryCallback {
                override fun retry() {
                    viewModel.getListAddress()
                }
            }
        }
        subscribeUI()
        return binding.root
    }


    private fun setupViewModel(){
        viewModel = ViewModelProvider(this, factory).get(TListAddressViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list_address, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.add -> {
                Navigation.findNavController(requireView())
                    .navigate(TListAddressFragmentDirections.actionTListAddressFragmentToTAddNewAddressFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeUI(){
        with(viewModel){
            addresses.observe(viewLifecycleOwner){resource ->
                binding.resource = resource
                if(checkResource(resource) && resource.data != null){
                    addressAdapter.userAddresses = ArrayList(resource.data)
                    addressAdapter.notifyDataSetChanged()
                }
            }

            deleteAddress.observe(viewLifecycleOwner){
                if(checkResource(it)){
                    showToast("Xoá địa chỉ thành công")
                    if(addressSelected != null){
                        addressAdapter.userAddresses.remove(addressSelected!!)
                        addressAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onButtonDelClick(address: UserAddress) {
        addressSelected = address
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Bạn muốn xoá địa chỉ này khỏi danh sách?")
            .setPositiveButton("Xoá"){_,_ ->
                viewModel.deleteAddress(address)
            }.setNegativeButton("Huỷ", null)
            .show()
    }

}