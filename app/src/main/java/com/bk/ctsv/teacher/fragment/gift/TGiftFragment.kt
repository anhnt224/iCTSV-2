package com.bk.ctsv.teacher.fragment.gift

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
        setHasOptionsMenu(true)
        setUpViewModel()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.t_gift_fragment,
            container,
            false
        )

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
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list_job, menu)
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

    private fun  navigateToGiftReceived(){
        val action = TGiftFragmentDirections.actionTGiftFragmentToTGiftReceivedFragment()
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

    }


    override fun onItemClick(gift: Gift) {
        navigateToGiftInfo(gift)
    }

    private fun navigateToGiftInfo(gift: Gift){
        val action = TGiftFragmentDirections.actionTGiftFragmentToTGiftInfoFragment(gift)
        Navigation.findNavController(requireView()).navigate(action)
    }
}