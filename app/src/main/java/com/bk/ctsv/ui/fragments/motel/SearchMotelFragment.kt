package com.bk.ctsv.ui.fragments.motel

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bk.ctsv.R
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.ui.viewmodels.motel.SearchMotelViewModel
import javax.inject.Inject

class SearchMotelFragment : Fragment(), Injectable {

    private lateinit var viewModel: SearchMotelViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        return inflater.inflate(R.layout.search_motel_fragment, container, false)
    }


    private fun setupViewModel(){
        viewModel = ViewModelProvider(this, factory).get(SearchMotelViewModel::class.java)
    }

}