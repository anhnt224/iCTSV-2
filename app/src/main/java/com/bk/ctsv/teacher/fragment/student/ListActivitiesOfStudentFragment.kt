package com.bk.ctsv.teacher.fragment.student

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
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.databinding.ListActivitiesOfStudentFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.teacher.viewmodel.student.ListActivitiesOfStudentViewModel
import com.bk.ctsv.ui.adapter.activity.ActivityAdapter
import javax.inject.Inject

class ListActivitiesOfStudentFragment : Fragment(), Injectable, ActivityAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelFactory
    @Inject lateinit var appExecutors: AppExecutors
    private lateinit var viewModel: ListActivitiesOfStudentViewModel
    private lateinit var binding: ListActivitiesOfStudentFragmentBinding
    private lateinit var activityAdapter: ActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.list_activities_of_student_fragment, container, false)
        activityAdapter = ActivityAdapter(appExecutors, this)

        binding.recyclerView.apply {
            adapter = activityAdapter
            layoutManager = LinearLayoutManager(context)
        }

        val student = ListActivitiesOfStudentFragmentArgs.fromBundle(requireArguments()).student
        viewModel.getListActivities(student.id)

        binding.callback = object : RetryCallback {
            override fun retry() {
                viewModel.getListActivities(student.id)
            }
        }

        subscribeUI()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(ListActivitiesOfStudentViewModel::class.java)
    }

    private fun subscribeUI(){
        with(viewModel){
            activities.observe(viewLifecycleOwner){
                binding.status = it.status
                Log.d("_GET_ACTIVITY", it.data.toString())
                if(checkResource(it)){
                    activityAdapter.submitList(it.data)
                    activityAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onItemClick(activity: Activity) {
        //
    }

}