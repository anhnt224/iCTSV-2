package com.bk.ctsv.teacher.fragment.gift

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.databinding.TGiftFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.teacher.viewmodel.gift.TGiftViewModel
import com.bk.ctsv.ui.adapter.gift.GiftAdapter
import com.bk.ctsv.ui.adapter.gift.GiftRegisteredAdapter
import com.bk.ctsv.ui.fragments.gift.GiftFragment
import com.bk.ctsv.ui.fragments.gift.GiftFragmentDirections
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class TGiftFragment : Fragment(),
    Injectable,
    GiftAdapter.OnItemClickListener,
    GiftRegisteredAdapter.OnItemClickListener
{

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: TGiftViewModel
    private lateinit var binding: TGiftFragmentBinding
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var giftAdapter: GiftAdapter
    private lateinit var giftRegisteredAdapter: GiftRegisteredAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpViewModel()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.t_gift_fragment,
            container,
            false
        )
        binding.apply {
            backButton.setOnClickListener {
                Navigation.findNavController(requireView()).navigateUp()
            }
            tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position){
                        0 -> viewModel.setType(GiftFragment.GiftType.ALL)
                        1 -> viewModel.setType(GiftFragment.GiftType.RECEIVED)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
        setUpRecyclerView()
        subscribeUi()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(TGiftViewModel::class.java)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeUi(){
        with(viewModel){
            gifts.observe(viewLifecycleOwner){
                binding.loadPublicGiftStatus = it.status
                if (checkResource(it) && it.data != null){
                    giftAdapter.gifts = it.data
                    giftAdapter.notifyDataSetChanged()
                }
            }
            giftsRegistered.observe(viewLifecycleOwner){
                binding.loadRegisteredGiftStatus = it.status
                if (checkResource(it) && it.data != null){
                    giftRegisteredAdapter.gifts = it.data
                    giftRegisteredAdapter.notifyDataSetChanged()
                }
            }
            giftType.observe(viewLifecycleOwner){
                when(it){
                    GiftFragment.GiftType.ALL -> {
                        binding.apply {
                            tabLayout.selectTab(tabLayout.getTabAt(0))
                        }
                        showGiftList()
                    }
                    GiftFragment.GiftType.RECEIVED -> {
                        binding.apply {
                            tabLayout.selectTab(tabLayout.getTabAt(1))
                        }
                        showReceivedGiftList()
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(){
        giftAdapter = GiftAdapter(
            listOf(),
            requireActivity(),
            this,
            userName = sharedPrefsHelper.getUserName(),
            token = sharedPrefsHelper.getToken()
        )
        giftRegisteredAdapter = GiftRegisteredAdapter(
            listOf(),
            requireActivity(),
            userName = sharedPrefsHelper.getUserName(),
            token = sharedPrefsHelper.getToken(),
            onItemClickListener = this
        )

        binding.giftList.apply {
            adapter = giftAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.receivedGiftList.apply {
            adapter = giftRegisteredAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showGiftList(){
        binding.giftType = GiftFragment.GiftType.ALL
    }

    private fun showReceivedGiftList(){
        binding.giftType = GiftFragment.GiftType.RECEIVED
    }

    override fun onItemClick(gift: Gift) {
        navigateToGiftInfo(gift)
    }

    private fun navigateToGiftInfo(gift: Gift){
        val action = TGiftFragmentDirections.actionTGiftFragmentToTGiftInfoFragment(gift)
        Navigation.findNavController(requireView()).navigate(action)
    }
}