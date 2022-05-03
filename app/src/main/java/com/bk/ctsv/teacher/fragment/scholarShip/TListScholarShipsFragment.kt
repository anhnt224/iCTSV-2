package com.bk.ctsv.teacher.fragment.scholarShip

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.common.Status
import com.bk.ctsv.databinding.TListScholarShipsFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.handleTokenInvalid
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.ScholarShip
import com.bk.ctsv.teacher.viewmodel.scholarShip.TListScholarShipsViewModel
import com.bk.ctsv.ui.adapter.OnItemClickListener
import com.bk.ctsv.ui.adapter.ScholarShipAdapter
import com.bk.ctsv.ui.fragments.scholarShip.ListScholarShipsFragment
import com.bk.ctsv.ui.fragments.scholarShip.ListScholarShipsFragmentDirections
import javax.inject.Inject

class TListScholarShipsFragment : Fragment(), Injectable, OnItemClickListener<ScholarShip> {

    companion object {
        fun newInstance() = ListScholarShipsFragment()
    }

    private lateinit var viewModel: TListScholarShipsViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: TListScholarShipsFragmentBinding
    private lateinit var scholarShipAdapter: ScholarShipAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.t_list_scholar_ships_fragment, container, false)
        setUpViewModel()
        scholarShipAdapter = ScholarShipAdapter(arrayListOf(), this)
        binding.apply {
            recyclerview.apply {
                adapter = scholarShipAdapter
                layoutManager = LinearLayoutManager(context)
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getListScholarShips(0, 1000)
                swipeRefreshLayout.isRefreshing = false
            }

            callback = object : RetryCallback {
                override fun retry() {
                    viewModel.getListScholarShips(0, 1)
                }
            }

        }
        subscribeUI()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(TListScholarShipsViewModel::class.java)
    }
    private fun subscribeUI(){
        with(viewModel){
            listScholarShips.observe(viewLifecycleOwner){resource ->
                binding.resource = resource
                when(resource.status){
                    Status.SUCCESS_NETWORK -> {
                        if(resource.data != null){
                            scholarShipAdapter.scholarShips = resource.data
                            scholarShipAdapter.notifyDataSetChanged()
                        }
                    }
                    Status.ERROR_TOKEN -> {
                        handleTokenInvalid()
                    }
                    Status.ERROR -> {
                        showToast(resource.respText?:"")
                    }
                    else -> {

                    }
                }
            }
        }
    }

    override fun onClick(value: ScholarShip) {
        Navigation.findNavController(requireView()).navigate(TListScholarShipsFragmentDirections.actionTListScholarShipsFragmentToTScholarShipDetailFragment(value))
    }

}