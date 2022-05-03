package com.bk.ctsv.ui.fragments.user


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.bk.ctsv.R
import com.bk.ctsv.common.Status
import com.bk.ctsv.databinding.FragmentCheckOtpLostPasswordBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.ui.viewmodels.user.CheckOTPLostPasswordViewModel
import com.bk.ctsv.utilities.autoCleared



import javax.inject.Inject


class CheckOTPLostPasswordFragment : androidx.fragment.app.Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CheckOTPLostPasswordViewModel
    private var binding by autoCleared<FragmentCheckOtpLostPasswordBinding>()

    private var userCode = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userCode = CheckOTPLostPasswordFragmentArgs.fromBundle(arguments!!).userCode
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_check_otp_lost_password, container, false)
        setupViewModel()
//        setupToolbar()
        subscribeUi()

        binding.apply {
            setLifecycleOwner(this@CheckOTPLostPasswordFragment)

            btnResetPassword.setOnClickListener {
                resetPassword()
            }
        }
        return binding.root
    }


    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CheckOTPLostPasswordViewModel::class.java)
    }

    private fun subscribeUi() {
        with(viewModel){
            checkOTPLostPasswordResource.observe(viewLifecycleOwner, Observer { resource ->
                binding.checkOTPResource = resource
                if (resource.data != null && isSuccess){
                    if (resource.status == Status.SUCCESS_NETWORK){
                        showToast("Reset mật khẩu thành công, kiểm tra email")
                        isSuccess = false
                        activity!!.onBackPressed()
                    }
                    if (resource.status == Status.ERROR){
                        showToast(resource.respText!!)
                        isSuccess = false
                    }
                }
            })
        }
    }

    fun resetPassword() {
        val otp = binding.edtOTP.text.toString()
        if (otp.equals("")){
            showToast("Nhập OTP")
            return
        }
        viewModel.checkOTPLostPassword(userCode,otp)

    }

}
