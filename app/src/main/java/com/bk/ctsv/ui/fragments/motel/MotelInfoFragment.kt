package com.bk.ctsv.ui.fragments.motel

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bk.ctsv.R
import com.bk.ctsv.databinding.MotelInfoFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.ImageMotel2
import com.bk.ctsv.models.entity.Motel
import com.bk.ctsv.ui.adapter.user.MotelImageAdapter
import com.bk.ctsv.ui.viewmodels.motel.MotelInfoViewModel
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import javax.inject.Inject

class MotelInfoFragment : Fragment()
    , Injectable, MotelImageAdapter.OnItemClickListener {

    private lateinit var viewModel: MotelInfoViewModel
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var binding: MotelInfoFragmentBinding
    private lateinit var motel: Motel
    private lateinit var motelImageAdapter: MotelImageAdapter
    private var motelImageList = listOf<ImageMotel2>()
    private val snapHelper: SnapHelper = LinearSnapHelper()

    companion object {
        fun newInstance() = MotelInfoFragment()
    }


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.motel_info_fragment,
            container,
            false
        )
        setupViewModel()
        motel = MotelInfoFragmentArgs.fromBundle(requireArguments()).motel
        viewModel.getListMotel(motel.id)
        Log.v("_MotelInfoFragment", "${motel.id}")
        subscribeUI()
        setUpRecyclerView()
        binding.apply {
            textViewHostMotel.text = motel.managerName
            textViewContactMotel.text = motel.managerContact
            textViewAddressDetail.text = motel.address
            textViewDescriptionMotel.text = motel.description
            RatingMotel.rating = motel.rate.toFloat()/20
            textViewComment.text = motel.comment

            copy.setOnClickListener {
                copy()
            }

            textViewContactMotel.setOnClickListener {
                if (textViewContactMotel.text.isNotEmpty()){
                    telHotline(textViewContactMotel.text.toString())
                }
            }
        }

        return binding.root
    }

    private fun telHotline(phone: String){
        try{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:${phone}"))
            startActivity(intent)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun copy(){
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val textToCopy = binding.textViewContactMotel.text
        val clip = ClipData.newPlainText("Code Order",textToCopy)
        showToast("Đã sao chép")
        clipboard.setPrimaryClip(clip)
    }

    private fun setUpRecyclerView() {
        motelImageAdapter = MotelImageAdapter(motelImageList, requireActivity(), this)
        binding.recyclerViewListImageMotel.apply {
            adapter = motelImageAdapter
            layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL,
                false)
        }
        snapHelper.attachToRecyclerView(binding.recyclerViewListImageMotel)
        Log.v("_MotelInfoFragment", "motelImageList after setup: ${motelImageAdapter.motelImageList}")
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this, factory).get(MotelInfoViewModel::class.java)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun subscribeUI(){
        with(viewModel){
            motelImageList.observe(viewLifecycleOwner){
                Log.v("_MotelInfoFragment", "${it.data}")
                if (checkResource(it) && it.data != null){
                   motelImageAdapter.motelImageList = it.data
                }
                Log.v("_MotelInfoFragment", "motelImageList: ${motelImageAdapter.motelImageList}")
                motelImageAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun seePictureZoom(image: ImageMotel2){
        binding.imageLayoutZoom.visibility = View.VISIBLE
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
        val url = "https://ctsv.hust.edu.vn/api-t/${image.urlImage}"
        Glide.with(requireActivity())
            .load(url)
            .placeholder(shimmerDrawable)
            .error(R.drawable.ic_gift_default)
            .into(binding.imageViewZoom)
        binding.closeButton.setOnClickListener {
            binding.imageLayoutZoom.visibility = View.GONE
        }
    }

    override fun onClickImage(imageMotel2: ImageMotel2) {
        seePictureZoom(imageMotel2)
    }
}