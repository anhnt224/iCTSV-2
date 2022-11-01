package com.bk.ctsv.modules.searchMotel.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bk.ctsv.databinding.FragmentMotelRegistrationProcessingBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.modules.searchMotel.model.MotelRegistration
import com.bk.ctsv.modules.searchMotel.viewModels.MotelRegistrationProcessingViewModel

class MotelRegistrationProcessingFragment : Fragment(), Injectable {

    private lateinit var viewModel: MotelRegistrationProcessingViewModel
    private lateinit var binding: FragmentMotelRegistrationProcessingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMotelRegistrationProcessingBinding.inflate(inflater, container, false)

        val args = MotelRegistrationProcessingFragmentArgs.fromBundle(requireArguments())

        binding.apply {
            backButton.setOnClickListener {
                Navigation.findNavController(requireView()).navigateUp()
            }

            showButton.setOnClickListener {
                navigateToMotelRegistrationInfo(args.motelRegistration)
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MotelRegistrationProcessingViewModel::class.java)
    }

    private fun navigateToMotelRegistrationInfo(motelRegistration: MotelRegistration) {
        val action =
            MotelRegistrationProcessingFragmentDirections.actionMotelRegistrationProcessingFragmentToMotelRegistrationInfoFragment(
                motelRegistration
            )
        Navigation.findNavController(requireView()).navigate(action)
    }


}