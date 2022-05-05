package com.bk.ctsv.ui.fragments.motel

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.databinding.SearchMotelFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.models.entity.Motel
import com.bk.ctsv.ui.adapter.MotelInfoAdapter
import com.bk.ctsv.ui.viewmodels.motel.SearchMotelViewModel
import javax.inject.Inject

class SearchMotelFragment : Fragment(),
    Injectable,
    MotelInfoAdapter.OnItemMotelInfoClickListener{

    private lateinit var viewModel: SearchMotelViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: SearchMotelFragmentBinding
    private var listMotel = arrayListOf<Motel>()
    private lateinit var motelInfoAdapter: MotelInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        binding = DataBindingUtil.inflate(inflater,
            R.layout.search_motel_fragment,
            container,
            false)

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

}