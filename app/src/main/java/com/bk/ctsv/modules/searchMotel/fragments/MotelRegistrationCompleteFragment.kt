package com.bk.ctsv.modules.searchMotel.fragments

import android.graphics.Typeface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.bk.ctsv.R
import com.bk.ctsv.databinding.FragmentMotelRegistrationCompleteBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.modules.searchMotel.model.MotelRegistration
import com.bk.ctsv.modules.searchMotel.viewModels.MotelRegistrationCompleteViewModel
import kotlinx.android.synthetic.main.list_item_home.*

class MotelRegistrationCompleteFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentMotelRegistrationCompleteBinding
    private lateinit var viewModel: MotelRegistrationCompleteViewModel
    private var numberOfMotel = 5

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMotelRegistrationCompleteBinding.inflate(inflater, container, false)

        val args = MotelRegistrationCompleteFragmentArgs.fromBundle(requireArguments())
        binding.apply {
            backButton.setOnClickListener {
                Navigation.findNavController(requireView()).navigateUp()
            }

            showButton.setOnClickListener {
                navigateToMotelRegistrationInfo(args.motelRegistration)
            }

            resultText.text = getSpanTitle(numberOfMotel)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MotelRegistrationCompleteViewModel::class.java)
    }

    private fun navigateToMotelRegistrationInfo(motelRegistration: MotelRegistration) {
        val action =
            MotelRegistrationCompleteFragmentDirections.actionMotelRegistrationCompleteFragmentToMotelRegistrationInfoFragment(
                motelRegistration
            )
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun getSpanTitle(numberOfMotel: Int): SpannableStringBuilder{
        val numberOfMotelStr = "$numberOfMotel phòng trọ"
        val title =
            "Yêu cầu của bạn đã được xác nhận, bạn cần trả một khoản phí là $numberOfMotelStr để chúng tôi hỗ trợ tìm trọ cho bạn."

        val startIndex = title.indexOf(numberOfMotelStr)
        val length = numberOfMotelStr.length
        val spanContent = SpannableStringBuilder(title)

        val bold = StyleSpan(Typeface.BOLD)
        val greenText =
            ForegroundColorSpan(ContextCompat.getColor(requireContext(),R.color.green500))
        spanContent.setSpan(
            bold,
            startIndex,
            startIndex + length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        spanContent.setSpan(
            greenText,
            startIndex,
            startIndex + length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        return spanContent
    }

}