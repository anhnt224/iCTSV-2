package com.bk.ctsv.teacher.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.databinding.THome2FragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.teacher.viewmodel.THome2ViewModel
import com.bk.ctsv.ui.adapter.activity.EventAdapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import java.lang.Exception
import javax.inject.Inject

class THome2Fragment : Fragment(), Injectable, EventAdapter.OnItemClickListener {

    private lateinit var viewModel: THome2ViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var binding: THome2FragmentBinding
    private lateinit var eventAdapter: EventAdapter
    private val remoteConfig = Firebase.remoteConfig

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpViewModel()
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.t_home2_fragment,
            container,
            false
        )
        binding.apply {
            titleTextView.text = "Chào ${sharedPrefsHelper.getFullName()}"
            titleWelcome.text = remoteConfig.getString("titleWelcome2")
            serviceLayout.setOnClickListener {
                showListForm()
            }
            activityLayout.setOnClickListener {
                showListActivities()
            }
            scholarShipLayout.setOnClickListener {
                showListScholarShips()
            }
            jobLayout.setOnClickListener {
                showListJobs()
            }
            studentManagement.setOnClickListener {
                showListStudent()
            }
            runLayout.setOnClickListener {
                showRunDashboard()
            }
            sendGiftLayout.setOnClickListener {
                navigateToGiftGiven()
            }
            noteLayout.setOnClickListener {
                openLink(remoteConfig.getString("note_link"))
            }
            giftLayout.setOnClickListener {
                navigateToGiftFragment()
            }
        }
        setUpRecyclerView(binding)
        subscribeUi()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(THome2ViewModel::class.java)
    }

    private fun setUpRecyclerView(binding: THome2FragmentBinding){
        eventAdapter = EventAdapter(listOf(), requireActivity(), this)
        binding.recyclerView.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeUi(){
        with(viewModel){
            activities.observe(viewLifecycleOwner){
                binding.getActivityStatus = it.status
                if (checkResource(it)){
                    eventAdapter.activities = it.data?: listOf()
                    eventAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun showListForm(){
        val action = THome2FragmentDirections.actionTHome2FragmentToTListFormFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun showListActivities(){
        val action = THome2FragmentDirections.actionTHome2FragmentToTListActivitiesFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun showListScholarShips(){
        val action = THome2FragmentDirections.actionTHome2FragmentToTListScholarShipsFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun showListJobs(){
        val action = THome2FragmentDirections.actionTHome2FragmentToTListJobsFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun showListStudent(){
        val action = THome2FragmentDirections.actionTHome2FragmentToListStudentFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun showRunDashboard(){
        val action = THome2FragmentDirections.actionTHome2FragmentToRunDashboardFragment2()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToGiftGiven(){
        val action = THome2FragmentDirections.actionTHome2FragmentToTGiftGivenFragment(true)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun openLink(link: String){
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }catch (e: Exception){
        }
    }

    private fun navigateToGiftFragment(){
        val action = THome2FragmentDirections.actionTHome2FragmentToTGiftFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun onItemClick(activity: Activity) {
        //
    }


}