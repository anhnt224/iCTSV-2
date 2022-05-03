package com.bk.ctsv.teacher.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.bk.ctsv.R
import com.bk.ctsv.databinding.THomeFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.teacher.viewmodel.THomeViewModel
import javax.inject.Inject

class THomeFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = THomeFragment()
    }

    private lateinit var viewModel: THomeViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    @Inject lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var binding: THomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.t_home_fragment, container, false)
        binding.apply {
            textFullName.text = sharedPrefsHelper.getFullName()
            viewForm.setOnClickListener {
                showListForm()
            }

            viewActivity.setOnClickListener {
                showListActivities()
            }

            viewScholarShip.setOnClickListener {
                showListScholarShips()
            }

            viewJob.setOnClickListener {
                showListJobs()
            }

            viewCriteria.setOnClickListener {
                showListStudent()
            }
        }

        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(THomeViewModel::class.java)
    }

    private fun showListForm(){
        Navigation.findNavController(requireView()).navigate(THomeFragmentDirections.actionTHomeFragmentToTListFormFragment())
    }

    private fun showListActivities(){
        Navigation.findNavController(requireView()).navigate(THomeFragmentDirections.actionTHomeFragmentToTListActivitiesFragment2())
    }

    private fun showListScholarShips(){
        Navigation.findNavController(requireView()).navigate(THomeFragmentDirections.actionTHomeFragmentToTListScholarShipsFragment())
    }

    private fun showListJobs(){
        Navigation.findNavController(requireView()).navigate(THomeFragmentDirections.actionTHomeFragmentToTListJobsFragment())
    }

    private fun showListStudent(){
        Navigation.findNavController(requireView()).navigate(THomeFragmentDirections.actionTHomeFragmentToListStudentFragment())
    }

}