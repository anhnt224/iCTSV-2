package com.bk.ctsv.ui.fragments.gift

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.databinding.FragmentGiftReceiveBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.ui.adapter.gift.GiftRegisteredAdapter
import com.bk.ctsv.ui.viewmodels.gift.GiftReceiveViewModel
import javax.inject.Inject

class GiftReceiveFragment : Fragment(), Injectable, GiftRegisteredAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var viewModel: GiftReceiveViewModel
    private lateinit var binding : FragmentGiftReceiveBinding
    private lateinit var giftReceiverAdapter : GiftRegisteredAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        binding = FragmentGiftReceiveBinding.inflate(inflater, container, false)
        giftReceiverAdapter = GiftRegisteredAdapter(
            listOf(),
            requireActivity(),
            sharedPrefsHelper.getUserName(),
            sharedPrefsHelper.getToken(),
            this
        )
        binding.number = 1
        binding.recyclerView.apply {
            adapter = giftReceiverAdapter
            layoutManager = LinearLayoutManager(context)
        }


        subscribeUI()
        return binding.root
    }

    private fun subscribeUI(){
        with(viewModel){
            giftsRegistered.observe(viewLifecycleOwner){
                if (checkResource(it) && it.data != null){
                    if (it.data.isEmpty()){
                        binding.number = 0
                    }else{
                        binding.number = 1
                    }
                    giftReceiverAdapter.gifts = it.data
                    giftReceiverAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this, factory).get(GiftReceiveViewModel::class.java)
    }

    override fun onItemClick(gift: Gift) {
        val action = GiftReceiveFragmentDirections.actionGiftReceiveFragmentToGiftInfoFragment(gift)
        Navigation.findNavController(requireView()).navigate(action)
    }


}