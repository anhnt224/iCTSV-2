package com.bk.ctsv.teacher.fragment.job

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
import com.bk.ctsv.databinding.TListJobsFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.handleTokenInvalid
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.Job
import com.bk.ctsv.models.entity.JobType
import com.bk.ctsv.teacher.viewmodel.job.TListJobsViewModel
import com.bk.ctsv.ui.adapter.OnItemClickListener
import com.bk.ctsv.ui.adapter.job.JobAdapter
import com.bk.ctsv.ui.fragments.job.ListJobsFragmentDirections
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class TListJobsFragment : Fragment(), Injectable, OnItemClickListener<Job> {
    private lateinit var viewModel: TListJobsViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var binding: TListJobsFragmentBinding
    private lateinit var jobAdapter: JobAdapter
    private var type: JobType? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.t_list_jobs_fragment, container, false)
        setUpViewModel()
        jobAdapter = JobAdapter(listOf(), this)
        binding.apply {
            recyclerview.apply {
                adapter = jobAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    jobAdapter.jobs = listOf()
                    jobAdapter.notifyDataSetChanged()
                    when(tab?.position){
                        0 -> {
                            viewModel.setJobType(JobType.HOT)
                        }
                        1 -> {
                            viewModel.setJobType(JobType.NEW)
                        }
                        2 -> {
                            viewModel.setJobType(JobType.INTERSHIP)
                        }
                    }

                }

            })

            callback = object : RetryCallback {
                override fun retry() {
                    if(type != null){
                        viewModel.setJobType(type!!)
                    }
                }

            }
        }
        subscribeUI()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(TListJobsViewModel::class.java)
    }

    private fun subscribeUI(){
        with(viewModel){
            jobs.observe(viewLifecycleOwner){resource ->
                binding.resource = resource
                when(resource.status){
                    Status.SUCCESS_NETWORK -> {
                        if(resource.data != null){
                            jobAdapter.jobs = resource.data
                            jobAdapter.notifyDataSetChanged()
                        }
                    }
                    Status.ERROR -> {
                        showToast(resource.respText.toString())
                    }
                    Status.ERROR_TOKEN -> {
                        handleTokenInvalid()
                    }
                    else -> {}
                }
            }

            getJobType().observe(viewLifecycleOwner){
                type = it
                when(it){
                    JobType.HOT -> {
                        binding.tabLayout.getTabAt(0)?.select()
                    }
                    JobType.NEW -> {
                        binding.tabLayout.getTabAt(1)?.select()
                    }
                    JobType.INTERSHIP -> {
                        binding.tabLayout.getTabAt(2)?.select()
                    }
                    JobType.PARTTIME -> {
                        binding.tabLayout.getTabAt(3)?.select()
                    }
                }
            }
        }
    }

    override fun onClick(value: Job) {
        Navigation.findNavController(requireView()).navigate(TListJobsFragmentDirections.actionTListJobsFragmentToTJobDetailFragment(value))
    }

}