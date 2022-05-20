package com.bk.ctsv.ui.fragments.gift

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import com.bk.ctsv.R
import com.bk.ctsv.databinding.GiftInfoFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showActionDialog
import com.bk.ctsv.extension.showNotificationDialog
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.NotificationDialogType
import com.bk.ctsv.models.entity.gift.Gift
import com.bk.ctsv.ui.viewmodels.gift.GiftInfoViewModel
import com.bk.ctsv.utilities.API_UPLOAD_SERVICE_BASE_URL
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.gift_info_fragment.*
import javax.inject.Inject

class GiftInfoFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var viewModel: GiftInfoViewModel
    private lateinit var binding: GiftInfoFragmentBinding
    private lateinit var gift: Gift

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpViewModel()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.gift_info_fragment,
            container,
            false
        )
        gift = GiftInfoFragmentArgs.fromBundle(requireArguments()).gift
        fillInfo(gift)
        binding.apply {
            registerButton.setOnClickListener {
                navigateToRegisterGift(this@GiftInfoFragment.gift)
            }
            unRegisterButton.setOnClickListener {
                showActionDialog(
                    title = "Xác nhận huỷ đăng kí",
                    icon = R.drawable.ic_close,
                    message = "Bạn có chắc chắn huỷ đăng kí nhận phần quà này?",
                    positiveButtonTitle = "Huỷ",
                    handlePositiveButtonTap = {
                        unRegisterGift()
                    },
                    negativeButtonTitle = "Bỏ qua"
                )

            }
            banner.setOnClickListener {

            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToGiftFragment()
            }
        })
        return binding.root
    }

    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, factory).get(GiftInfoViewModel::class.java)
    }

    private fun navigateToRegisterGift(gift: Gift){
        val action = GiftInfoFragmentDirections.actionGiftInfoFragmentToRegistGiftFragment(gift)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun fillInfo(gift: Gift){
        binding.gift = gift
        val imageUrls = listOf(
            "${API_UPLOAD_SERVICE_BASE_URL}ATMGift/DownloadImageGift?" +
                    "UserName=${sharedPrefsHelper.getUserName()}&" +
                    "TokenCode=${sharedPrefsHelper.getToken()}&" +
                    "GiftID=${gift.id}&TypeImage=1",
            "${API_UPLOAD_SERVICE_BASE_URL}ATMGift/DownloadImageGift?" +
                    "UserName=${sharedPrefsHelper.getUserName()}&" +
                    "TokenCode=${sharedPrefsHelper.getToken()}&" +
                    "GiftID=${gift.id}&TypeImage=2",
            "${API_UPLOAD_SERVICE_BASE_URL}ATMGift/DownloadImageGift?" +
                    "UserName=${sharedPrefsHelper.getUserName()}&" +
                    "TokenCode=${sharedPrefsHelper.getToken()}&" +
                    "GiftID=${gift.id}&TypeImage=3"
        )

        val banner = binding.banner as Banner<String, BannerImageAdapter<String>>
        banner.apply {
            addBannerLifecycleObserver(viewLifecycleOwner)
            indicator = CircleIndicator(context)
            setAdapter(object: BannerImageAdapter<String>(imageUrls){
                override fun onBindView(
                    holder: BannerImageHolder,
                    data: String,
                    position: Int,
                    size: Int
                ) {
                    holder.imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    holder.imageView.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.mainBackground)
                    )
                    val shimmer = Shimmer.AlphaHighlightBuilder()
                        .setDuration(1800)
                        .setBaseAlpha(0.7f) //the alpha of the underlying children
                        .setHighlightAlpha(0.6f) // the shimmer alpha amount
                        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                        .setAutoStart(true)
                        .build()
                    val shimmerDrawable = ShimmerDrawable().apply {
                        setShimmer(shimmer)
                    }
                    Glide.with(requireActivity())
                        .load(data)
                        .centerInside()
                        .placeholder(shimmerDrawable)
                        .error(R.drawable.ic_gift_default)
                        .into(holder.imageView)
                }

            })
        }

        binding.apply {
            statusText.setBackgroundColor(
                ContextCompat.getColor(root.context, gift.getUStatus().bgColor)
            )
            statusText.setTextColor(
                ContextCompat.getColor(root.context, gift.getUStatus().titleColor)
            )
            tvQuan.text = gift.quantity.toString()
            tvQuanRegister.text = gift.registeredQuantity.toString()
        }
    }

    private fun unRegisterGift(){
        viewModel.cancelApplyGift(gift.id)
        with(viewModel){
            cancelApplyGift.observe(viewLifecycleOwner){
                binding.cancelApplyStatus = it.status
                if (checkResource(it)){
                    showNotificationDialog(
                        message = "Huỷ đăng kí quà tặng thành công",
                        cancelable = false,
                        type = NotificationDialogType.COMPLETE,
                        handleDone = {
                            navigateToGiftFragment()
                        }
                    )
                }
                if (!it.isLoading()){
                    cancelApplyGift.removeObservers(viewLifecycleOwner)
                }
            }
        }
    }

    private fun navigateToGiftFragment(){
        val action = GiftInfoFragmentDirections.actionGiftInfoFragmentToGiftFragment(false)
        Navigation.findNavController(requireView()).navigate(action)
    }

}