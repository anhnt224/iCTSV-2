package com.bk.ctsv.ui.fragments.gift

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.bk.ctsv.R
import com.bk.ctsv.common.Status
import com.bk.ctsv.databinding.ApplyJobFragmentBinding
import com.bk.ctsv.databinding.RegistGiftFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showDialog
import com.bk.ctsv.extension.showNotificationDialog
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.NotificationDialogType
import com.bk.ctsv.models.entity.UserAddress
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.models.entity.gift.GiftRegister
import com.bk.ctsv.ui.viewmodels.gift.ApplyGiftViewModel
import kotlinx.android.synthetic.main.create_gift_fragment.*
import kotlinx.android.synthetic.main.gift_info_fragment.*
import javax.inject.Inject

class ApplyGiftFragment : Fragment(), Injectable {

    companion object {
        var userAddress: UserAddress? = null
    }
    private lateinit var viewModel: ApplyGiftViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var binding: RegistGiftFragmentBinding
    private lateinit var gift: Gift

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpViewModel()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.regist_gift_fragment,
            container,
            false
        )
        fillInfo()
        binding.apply {
            applyButton.setOnClickListener {
                applyGift()
            }
            pickAddressLayout.setEndIconOnClickListener {
                val action = ApplyGiftFragmentDirections.actionRegistGiftFragmentToReceiverAddressFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
        if (userAddress != null){
            binding.pickAddressLayout.editText!!.setText(userAddress!!.address)
        }
        return binding.root
    }
    
    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(ApplyGiftViewModel::class.java)
    }

    private fun fillInfo(){
        gift = ApplyGiftFragmentArgs.fromBundle(requireArguments()).gift
        binding.gift = gift
        binding.apply {
            fullNameText.setText(sharedPrefsHelper.getFullName())
            emailText.setText(sharedPrefsHelper.getEmail())
        }
    }

    private fun applyGift(){
        val reason = binding.reasonText.text.toString()
        if (reason.isEmpty() || (gift.deliveryEnable == 1 && userAddress == null)){
            showToast("Vui lòng nhập đầy đủ thông tin")
            return
        }
        val giftRegister = GiftRegister(
            userCode = sharedPrefsHelper.getUserName(),
            giftId = gift.id,
            email = binding.emailText.text.toString(),
            reason = reason,
            address = userAddress?.address ?: "",
            addressId = userAddress?.id ?: 0
        )
        viewModel.applyGift(giftRegister)
        with(viewModel){
            applyGiftResp.observe(viewLifecycleOwner){
                binding.status = it.status
                if (checkResource(it)){
                    showNotificationDialog(
                        message = "Đăng kí quà tặng thành công",
                        type = NotificationDialogType.COMPLETE,
                        handleDone = {
                            popToListGifts()
                        },
                        cancelable = false
                    )
                }
                if (it.status != Status.LOADING){
                    applyGiftResp.removeObservers(viewLifecycleOwner)
                }
            }
        }
    }

    private fun popToListGifts(){
        val action = ApplyGiftFragmentDirections.actionRegistGiftFragmentToGiftReceiveFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
}