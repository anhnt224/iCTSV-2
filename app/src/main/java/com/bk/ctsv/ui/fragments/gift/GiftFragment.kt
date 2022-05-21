package com.bk.ctsv.ui.fragments.gift

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.MainActivity
import com.bk.ctsv.R
import com.bk.ctsv.databinding.GiftFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.ui.adapter.gift.GiftAdapter
import com.bk.ctsv.ui.viewmodels.gift.GiftViewModel
import javax.inject.Inject

class GiftFragment : Fragment(), Injectable, GiftAdapter.OnItemClickListener{

    private lateinit var viewModel: GiftViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var binding: GiftFragmentBinding
    private lateinit var giftAdapter: GiftAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpViewModel()
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.gift_fragment, container, false)

        setUpRecyclerView()
        subscribeUi()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(GiftViewModel::class.java)
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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_form, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.list -> {
                navigateToGiftReceived()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToGiftReceived(){
        val action = GiftFragmentDirections.actionGiftFragmentToGiftReceiveFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun setUpRecyclerView(){
        giftAdapter = GiftAdapter(
            listOf(),
            requireActivity(),
            this,
            userName = sharedPrefsHelper.getUserName(),
            token = sharedPrefsHelper.getToken()
        )

        binding.giftList.apply {
            adapter = giftAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    override fun onItemClick(gift: Gift) {
        navigateToGiftInfo(gift)
    }

    private fun navigateToGiftInfo(gift: Gift){
        val action = GiftFragmentDirections.actionGiftFragmentToGiftInfoFragment(gift)
        Navigation.findNavController(requireView()).navigate(action)
    }
}