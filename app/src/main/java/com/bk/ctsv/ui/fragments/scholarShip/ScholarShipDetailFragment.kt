package com.bk.ctsv.ui.fragments.scholarShip

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.bk.ctsv.R
import com.bk.ctsv.common.Status
import com.bk.ctsv.databinding.ScholarShipDetailFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.handleTokenInvalid
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.ScholarShip
import com.bk.ctsv.ui.viewmodels.scholarShip.ScholarShipDetailViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class ScholarShipDetailFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = ScholarShipDetailFragment()
    }

    private lateinit var viewModel: ScholarShipDetailViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var binding: ScholarShipDetailFragmentBinding
    var scholarShip: ScholarShip? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpViewModel()
        binding = DataBindingUtil.inflate(inflater, R.layout.scholar_ship_detail_fragment, container, false)
        scholarShip = ScholarShipDetailFragmentArgs.fromBundle(requireArguments()).scholarShip
        binding.scholarShip = scholarShip
        binding.buttonApply.setOnClickListener {
            if(scholarShip != null){
                handleApplyScholarShip(scholarShip!!)
            }
        }
        subscribeUI()
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(ScholarShipDetailViewModel::class.java)
    }
    private fun subscribeUI(){
        with(viewModel){
            applyScholarShip.observe(viewLifecycleOwner){resource ->
                binding.resource = resource
                when(resource.status){
                    Status.SUCCESS_NETWORK -> {
                        showToast("Đăng kí học bổng thành công")
                        binding.layoutAction.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        showToast(resource.respText.toString())
                    }
                    Status.ERROR_TOKEN -> {
                        handleTokenInvalid()
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun handleApplyScholarShip(scholarShip: ScholarShip){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Đăng kí học bổng")
            .setMessage("Xác nhận đăng kí học bổng: \"${scholarShip.title}}\"")
            .setPositiveButton("Đăng kí"){_, _ ->
                viewModel.applyScholarShip(scholarShip.id)
            }.setNegativeButton("Hủy"){_, _ ->

            }.show()
    }

}