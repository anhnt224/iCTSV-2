package com.bk.ctsv.teacher.fragment.scholarShip

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bk.ctsv.R
import com.bk.ctsv.databinding.TScholarShipDetailFragmentBinding
import com.bk.ctsv.models.entity.ScholarShip
import com.bk.ctsv.teacher.viewmodel.scholarShip.TScholarShipDetailViewModel
import com.bk.ctsv.ui.fragments.scholarShip.ScholarShipDetailFragmentArgs

class TScholarShipDetailFragment : Fragment() {
    private lateinit var viewModel: TScholarShipDetailViewModel
    private lateinit var binding: TScholarShipDetailFragmentBinding
    var scholarShip: ScholarShip? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.t_scholar_ship_detail_fragment, container, false)
        scholarShip = ScholarShipDetailFragmentArgs.fromBundle(requireArguments()).scholarShip
        binding.scholarShip = scholarShip
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this).get(TScholarShipDetailViewModel::class.java)
    }
}