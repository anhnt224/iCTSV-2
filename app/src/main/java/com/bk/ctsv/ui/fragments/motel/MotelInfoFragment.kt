package com.bk.ctsv.ui.fragments.motel

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bk.ctsv.R
import com.bk.ctsv.ui.viewmodels.motel.MotelInfoViewModel

class MotelInfoFragment : Fragment() {

    companion object {
        fun newInstance() = MotelInfoFragment()
    }

    private lateinit var viewModel: MotelInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.motel_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MotelInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}