package com.bk.ctsv.teacher.fragment.motel

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bk.ctsv.R
import com.bk.ctsv.teacher.viewmodel.motel.AddMotelInfoViewModel

class TAddMotelInfoFragment : Fragment() {

    private lateinit var viewModel: AddMotelInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_motel_info2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddMotelInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}