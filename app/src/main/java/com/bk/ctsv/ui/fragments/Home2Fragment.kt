package com.bk.ctsv.ui.fragments

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
import com.bk.ctsv.databinding.Home2FragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkLocationPermission
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.models.entity.Semester
import com.bk.ctsv.ui.adapter.activity.EventAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.android.synthetic.main.home2_fragment.*
import java.lang.Exception
import javax.inject.Inject

class Home2Fragment : Fragment(), Injectable, EventAdapter.OnItemClickListener {

    private lateinit var viewModel: Home2ViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var binding: Home2FragmentBinding
    private lateinit var eventAdapter: EventAdapter
    private val remoteConfig = Firebase.remoteConfig
    private var semesters: List<Semester> = listOf()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpViewModel()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home2_fragment,
            container,
            false
        )
        binding.apply {
            titleTextView.text = "Chào ${sharedPrefsHelper.getFullName()}"
            titleWelcome.text = remoteConfig.getString("titleWelcome2")
            markLayout.setOnClickListener {
                chooseSemester()
            }
            criteriaLayout.setOnClickListener {
                navigateToCriteriaFragment()
            }
            activityLayout.setOnClickListener {
                navigateToActivityFragment()
            }
            serviceLayout.setOnClickListener {
                navigateToListFormFragment()
            }
            jobLayout.setOnClickListener {
                navigateToListJobsFragment()
            }
            scholarShipLayout.setOnClickListener {
                navigateToListScholarShips()
            }
            runLayout.setOnClickListener {
                navigateToRunDashboard()
            }
            addressLayout.setOnClickListener {
                navigateToListAddressFragment()
            }
            noteLayout.setOnClickListener {
                openLink(remoteConfig.getString("note_link"))
            }
            retryButton.setOnClickListener {
                viewModel.getPublicActivities()
            }
            giftLayout.setOnClickListener {
                navigateToGift()
            }
            sendGiftLayout.setOnClickListener {
                navigateGivenGift()
            }
            searchMotelLayout.setOnClickListener {
                if(checkLocationPermission()){
                    navigateToSearchMotel()
                }
            }
        }
        viewModel.getListSemester()
        setUpRecyclerView(binding)
        subscribeUi()
        return binding.root
    }

    private fun navigateToSearchMotel() {
        val action = Home2FragmentDirections.actionHome2FragmentToSearchMotelFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(Home2ViewModel::class.java)
    }

    private fun setUpRecyclerView(binding: Home2FragmentBinding){
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
            semesters.observe(viewLifecycleOwner){
                if (it.isSuccess()){
                    this@Home2Fragment.semesters = it.data?: listOf()
                }
            }
        }
    }

    private fun chooseSemester(){
        val semesterStr = semesters.map {
            it.name
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Chọn kì học")
            .setItems(semesterStr.toTypedArray()) { _, which ->
                navigateToMarkFragment(semesters[which], semesters)
            }
            .setNegativeButton("Hủy"){_, _ ->

            }.show()
    }

    private fun navigateToMarkFragment(semester: Semester, semesters: List<Semester>){
        val action = Home2FragmentDirections.actionHome2FragmentToCriteriaFragment(
            semester, semesters.toTypedArray()
        )
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToCriteriaFragment(){
        val action = Home2FragmentDirections.actionHome2FragmentToTrainingPointFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToActivityFragment(){
        val action = Home2FragmentDirections.actionHome2FragmentToListActivityFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToListFormFragment(){
        val action = Home2FragmentDirections.actionHome2FragmentToListFormsFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToListJobsFragment(){
        val action = Home2FragmentDirections.actionHome2FragmentToListJobsFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToListScholarShips(){
        val action = Home2FragmentDirections.actionHome2FragmentToListScholarShipsFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToListAddressFragment(){
        val action = Home2FragmentDirections.actionHome2FragmentToListAddressFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToRunDashboard(){
        val action = Home2FragmentDirections.actionHome2FragmentToRunDashboardFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateToGift(){
        val action = Home2FragmentDirections.actionHome2FragmentToGiftFragment(true)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun navigateGivenGift(){
        val action = Home2FragmentDirections.actionHome2FragmentToGiftGivenFragment(true)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun openLink(link: String){
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }catch (e: Exception){
        }
    }

    override fun onItemClick(activity: Activity) {
        val action = Home2FragmentDirections.actionHome2FragmentToActivityDetailByUserUnitFragment(activity.id)
        Navigation.findNavController(requireView()).navigate(action)
    }
}