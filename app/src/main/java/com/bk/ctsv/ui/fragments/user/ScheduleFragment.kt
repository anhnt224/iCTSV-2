package com.bk.ctsv.ui.fragments.user


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.MainActivity

import com.bk.ctsv.R
import com.bk.ctsv.binding.FragmentDataBindingComponent
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.LogoutCallback
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.common.Status
import com.bk.ctsv.databinding.FragmentScheduleBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.extension.logoutClick
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.ScheduleStudent
import com.bk.ctsv.models.list.ScheduleList
import com.bk.ctsv.ui.adapter.user.SchedulesSection
import com.bk.ctsv.ui.viewmodels.user.ScheduleViewModel
import com.bk.ctsv.utilities.AUTO
import com.bk.ctsv.utilities.autoCleared
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class ScheduleFragment : androidx.fragment.app.Fragment(), Injectable {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var sharedPrefsHelper: SharedPrefsHelper
    @Inject lateinit var appExecutors: AppExecutors
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private lateinit var sectionedAdapter : SectionedRecyclerViewAdapter
    private var binding by autoCleared<FragmentScheduleBinding>()
    private lateinit var viewmodel: ScheduleViewModel


    override fun onResume() {
        super.onResume()

        if (activity is MainActivity){
            activity!!.navigation.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_schedule, container, false)
        sectionedAdapter = SectionedRecyclerViewAdapter()
        setupViewModel()
        binding.apply {
            setLifecycleOwner(this@ScheduleFragment)
            subscribeUi()

            retryCallback = object : RetryCallback {
                override fun retry() {
                    viewmodel.retry()
                }
            }

            logoutCallback = object : LogoutCallback {
                override fun logout() {
                    logoutClick(sharedPrefsHelper)
                }
            }



            recyclerview.setLayoutManager(LinearLayoutManager(context))
            recyclerview.setAdapter(sectionedAdapter)

        }
        return binding.root
    }

    private fun setupViewModel() {
        viewmodel = ViewModelProviders.of(this, viewModelFactory).get(ScheduleViewModel::class.java)
        viewmodel.setParameter(AUTO)

    }

    private fun subscribeUi() {
        with(viewmodel){
            scheduleStudents.observe(viewLifecycleOwner, Observer { resource ->

                if (resource.data != null){
                    binding.sizeOfScheduleStudent = resource.data.size
                    sectionedAdapter.removeAllSections()

                    val contactsMap = ScheduleList(resource.data).toMap()
                    for (entry in contactsMap.entries) {
                        if (entry.value.size > 0) {
                            sectionedAdapter.addSection(SchedulesSection(entry.key!!, entry.value,
                                {
                                    //   showToast(it.id)
                                }))
                        }
                    }
                    sectionedAdapter.notifyDataSetChanged()


                }
            })
            getWeekNumber().observe(viewLifecycleOwner, Observer {
                if (it!= null){
                    binding.txtWeekNumber.setText("Tuần ${it.weekNumber}")
                }

            })

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
