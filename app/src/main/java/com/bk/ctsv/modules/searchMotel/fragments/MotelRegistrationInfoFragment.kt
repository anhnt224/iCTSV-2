package com.bk.ctsv.modules.searchMotel.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bk.ctsv.databinding.FragmentMotelRegistrationInfoBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.modules.searchMotel.model.MotelRegistration
import com.bk.ctsv.modules.searchMotel.model.MotelRegistrationStatus
import com.bk.ctsv.modules.searchMotel.viewModels.MotelRegistrationInfoViewModel

class MotelRegistrationInfoFragment : Fragment(), Injectable {

    private lateinit var viewModel: MotelRegistrationInfoViewModel
    private lateinit var binding: FragmentMotelRegistrationInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMotelRegistrationInfoBinding.inflate(inflater, container, false)

        val args = MotelRegistrationInfoFragmentArgs.fromBundle(requireArguments())
        fillInfo(args.motelRegistration)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MotelRegistrationInfoViewModel::class.java)
    }

    private fun fillInfo(motelRegistration: MotelRegistration) {
        val status = motelRegistration.getStatus()
        binding.motelRegistrationStatus = status
        binding.motelRegistration = motelRegistration

        binding.apply {
            statusCard.setCardBackgroundColor(
                ContextCompat.getColor(root.context, status.statusBackground)
            )
            statusText.setTextColor(
                ContextCompat.getColor(root.context, status.statusTitleColor)
            )
        }

        if (status != MotelRegistrationStatus.PENDING) {
            binding.feeLayout.visibility = View.GONE
            return
        }else{
            binding.feeLayout.visibility = View.VISIBLE
        }

        binding.feeTitle.text = motelRegistration.getFeeTitleSpan(requireContext())
    }

}