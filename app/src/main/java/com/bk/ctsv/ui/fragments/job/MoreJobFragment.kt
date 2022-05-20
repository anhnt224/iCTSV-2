package com.bk.ctsv.ui.fragments.job

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.databinding.FragmentMoreJobBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.models.entity.Job
import com.bk.ctsv.ui.adapter.OnItemClickListener
import com.bk.ctsv.ui.adapter.job.JobAdapter
import com.bk.ctsv.ui.viewmodels.job.MoreJobViewModel
import javax.inject.Inject

class MoreJobFragment : Fragment(), Injectable, OnItemClickListener<Job> {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: MoreJobViewModel
    private lateinit var binding : FragmentMoreJobBinding
    private lateinit var jobAdapter: JobAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        setupViewModel()
        binding = FragmentMoreJobBinding.inflate(inflater, container, false)
        binding.number = 1
        setupRecyclerView()
        binding.callback = object : RetryCallback {
            override fun retry() {
                viewModel.getMoreJob()
            }

        }

        subscribeUI()
        return binding.root
    }

    private fun setupRecyclerView(){
        jobAdapter = JobAdapter(listOf(), this)
        binding.recyclerView.apply {
            adapter = jobAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun subscribeUI(){
        with(viewModel){
            jobMediatorLiveData.observe(viewLifecycleOwner){
                binding.resource = it
                if (checkResource(it) && it.data != null){
                    if (it.data.isNotEmpty()){
                       binding.number = 1
                    }else{
                        binding.number = 0
                    }
                    jobAdapter.jobs = it.data
                    jobAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list_job, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.list -> {
                navigateToRegisted()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToRegisted(){
        val action = MoreJobFragmentDirections.actionMoreJobFragmentToListJobApplyFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this, factory).get(MoreJobViewModel::class.java)
    }

    override fun onClick(value: Job) {
        Navigation.findNavController(requireView())
            .navigate(MoreJobFragmentDirections.actionMoreJobFragmentToJobDetailFragment(value))
    }

}