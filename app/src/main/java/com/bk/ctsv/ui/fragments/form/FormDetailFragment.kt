package com.bk.ctsv.ui.fragments.form

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.bk.ctsv.R
import com.bk.ctsv.databinding.FormDetailFragmentBinding
import com.bk.ctsv.ui.viewmodels.form.FormDetailViewModel

class FormDetailFragment : Fragment() {

    companion object {
        fun newInstance() = FormDetailFragment()
    }

    private lateinit var viewModel: FormDetailViewModel
    private lateinit var binding: FormDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.form_detail_fragment, container, false)
        val form =  FormDetailFragmentArgs.fromBundle(requireArguments()).form
        binding.form = form
        binding.buttonRegister.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(FormDetailFragmentDirections.actionFormDetailFragmentToRegisterFormFragment(form))
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FormDetailViewModel::class.java)
    }

}