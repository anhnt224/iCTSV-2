package com.bk.ctsv.teacher.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.LogoutCallback
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.databinding.TListNotificationsFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.logoutClick
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.teacher.viewmodel.TListNotificationsViewModel
import com.bk.ctsv.ui.adapter.user.MessageAdapter
import com.bk.ctsv.utilities.autoCleared
import javax.inject.Inject

class TListNotificationsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    @Inject
    lateinit var appExecutors: AppExecutors
    private var binding by autoCleared<TListNotificationsFragmentBinding>()

    private lateinit var adapter : MessageAdapter
    private lateinit var mViewmodel: TListNotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.t_list_notifications_fragment, container, false)
        setupViewModel()

        adapter = MessageAdapter(appExecutors,
            itemClick = {})

        binding.apply {
            lifecycleOwner = this@TListNotificationsFragment

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            listMessageRecycler.layoutManager = layoutManager
            listMessageRecycler.adapter = adapter

            retryCallback = object : RetryCallback {
                override fun retry() {
                    mViewmodel.getListMessage()
                }
            }

            logoutCallback = object : LogoutCallback {
                override fun logout() {
                    logoutClick(sharedPrefsHelper)
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                mViewmodel.getListMessage()
                swipeRefreshLayout.isRefreshing = false
            }
        }
        subscribeUi()
        return binding.root
    }

    private fun setupViewModel() {
        mViewmodel = ViewModelProviders.of(this, viewModelFactory).get(TListNotificationsViewModel::class.java)
    }

    private fun subscribeUi() {
        with(mViewmodel){
            messages.observe(viewLifecycleOwner, Observer { resource ->
                Log.d("_MESSAGE", resource.toString())
                binding.messagesResource = resource
                if(checkResource(resource) && resource.data != null){
                    binding.sizeOfMessage = resource.data.size
                    adapter.submitList(resource.data)
                }
            })
        }
    }
}