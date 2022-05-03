package com.bk.ctsv.ui.fragments.job

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.bk.ctsv.R
import com.bk.ctsv.databinding.JobDetailFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.ui.viewmodels.job.JobDetailViewModel

class JobDetailFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = JobDetailFragment()
    }

    private lateinit var viewModel: JobDetailViewModel
    private lateinit var binding: JobDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.job_detail_fragment, container, false)
        val job = JobDetailFragmentArgs.fromBundle(requireArguments()).job
        binding.job = job
        binding.buttonApply.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(JobDetailFragmentDirections.actionJobDetailFragmentToApplyJobFragment(job.id, job.title, job.companyName))
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(JobDetailViewModel::class.java)
    }

}