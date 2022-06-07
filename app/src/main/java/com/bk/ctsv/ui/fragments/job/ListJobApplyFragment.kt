package com.bk.ctsv.ui.fragments.job

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.common.Status
import com.bk.ctsv.databinding.ListJobApplyFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.handleTokenInvalid
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.Job
import com.bk.ctsv.ui.adapter.OnItemClickListener
import com.bk.ctsv.ui.adapter.job.JobApplyAdapter
import com.bk.ctsv.ui.viewmodels.job.ListJobApplyViewModel
import javax.inject.Inject

class ListJobApplyFragment : Fragment(), Injectable, OnItemClickListener<Job>{

    companion object {
        fun newInstance() = ListJobApplyFragment()
    }

    private lateinit var viewModel: ListJobApplyViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var binding: ListJobApplyFragmentBinding
    private lateinit var jobApplyAdapter: JobApplyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.list_job_apply_fragment, container, false)
        jobApplyAdapter = JobApplyAdapter(listOf(), this)
        binding.apply {
            number = 1
            recyclerview.apply {
                adapter = jobApplyAdapter
                layoutManager = LinearLayoutManager(context)
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getListJobApply()
                swipeRefreshLayout.isRefreshing = false
            }

            callback = object : RetryCallback {
                override fun retry() {
                    viewModel.getListJobApply()
                }

            }
        }
        viewModel.getListJobApply()
        subscribeUI()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(ListJobApplyViewModel::class.java)
    }

    private fun subscribeUI(){
        with(viewModel){
            listJobsApply.observe(viewLifecycleOwner){resource ->
                binding.resource = resource
                when(resource.status){
                    Status.SUCCESS_NETWORK -> {
                        if(resource.data != null){
                            if (resource.data.isEmpty()){
                                binding.number = 0
                            }else{
                                binding.number = 1
                            }
                            jobApplyAdapter.jobs = resource.data
                            jobApplyAdapter.notifyDataSetChanged()
                        }
                    }
                    Status.ERROR -> {
                        showToast(resource.respText.toString())
                    }
                    Status.ERROR_TOKEN -> {
                        handleTokenInvalid()
                    }
                }
            }
        }
    }

    override fun onClick(value: Job) {

    }

}