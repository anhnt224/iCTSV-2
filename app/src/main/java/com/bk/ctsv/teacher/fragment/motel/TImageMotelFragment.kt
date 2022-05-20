package com.bk.ctsv.teacher.fragment.motel

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bk.ctsv.R
import com.bk.ctsv.teacher.viewmodel.motel.TImageMotelViewModel

class TImageMotelFragment : Fragment() {

    companion object {
        fun newInstance() = TImageMotelFragment()
    }

    private lateinit var viewModel: TImageMotelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_t_image_motel, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TImageMotelViewModel::class.java)
        // TODO: Use the ViewModel
    }

}