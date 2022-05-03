package com.bk.ctsv.teacher.fragment.job

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
import com.bk.ctsv.databinding.TJobDetailFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.teacher.viewmodel.job.TJobDetailViewModel
import com.bk.ctsv.ui.fragments.job.JobDetailFragment
import com.bk.ctsv.ui.fragments.job.JobDetailFragmentArgs
import com.bk.ctsv.ui.fragments.job.JobDetailFragmentDirections
import com.bk.ctsv.ui.viewmodels.job.JobDetailViewModel

class TJobDetailFragment : Fragment() {

    companion object {
        fun newInstance() = JobDetailFragment()
    }

    private lateinit var viewModel: TJobDetailViewModel
    private lateinit var binding: TJobDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.t_job_detail_fragment, container, false)
        val job = JobDetailFragmentArgs.fromBundle(requireArguments()).job
        binding.job = job
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TJobDetailViewModel::class.java)
    }

}