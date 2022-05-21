package com.bk.ctsv.teacher.fragment.motel

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bk.ctsv.databinding.FragmentTMotelInfoBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.di.ViewModelFactory
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.ImageMotel2
import com.bk.ctsv.models.entity.Motel
import com.bk.ctsv.teacher.viewmodel.motel.TMotelInfoViewModel
import com.bk.ctsv.ui.adapter.user.MotelImageAdapter
import javax.inject.Inject

class TMotelInfoFragment : Fragment(), Injectable, MotelImageAdapter.OnItemClickListener {


    private lateinit var viewModel: TMotelInfoViewModel
    private lateinit var binding : FragmentTMotelInfoBinding
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var motel: Motel
    private lateinit var motelImageAdapter: MotelImageAdapter
    private var motelImageList = listOf<ImageMotel2>()
    private val snapHelper: SnapHelper = LinearSnapHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        binding = FragmentTMotelInfoBinding.inflate(inflater, container, false)
        motel = TMotelInfoFragmentArgs.fromBundle(requireArguments()).motel
        viewModel.getListMotel(motel.id)
        Log.v("_MotelInfoFragment", "${motel}")
        subscribeUI()
        setUpRecyclerView()
        binding.apply {
            textViewHostMotel.text = motel.managerName
            textViewContactMotel.text = motel.managerContact
            textViewAddressDetail.text = motel.managerContact
            textViewDescriptionMotel.text = motel.description
            RatingMotel.rating = motel.rate.toFloat()/20
            textViewComment.text = motel.comment
            copy.setOnClickListener {
                copy()
            }
        }

        return binding.root
    }

    private fun copy(){
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val textToCopy = binding.textViewContactMotel.text
        val clip = ClipData.newPlainText("Code Order",textToCopy)
        showToast("Đã sao chép")
        clipboard.setPrimaryClip(clip)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun subscribeUI(){
        with(viewModel){
            motelImageList.observe(viewLifecycleOwner){
                Log.v("_MotelInfoFragment", "${it.data}")
                if (checkResource(it) && it.data != null){
                    motelImageAdapter.motelImageList = it.data
                    motelImageAdapter.notifyDataSetChanged()
                }
                Log.v("_MotelInfoFragment", "motelImageList: ${motelImageAdapter.motelImageList}")
                motelImageAdapter.notifyDataSetChanged()
            }
        }
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
        viewModel = ViewModelProvider(this, factory).get(TMotelInfoViewModel::class.java)
    }

    override fun onClickImage(imageMotel2: ImageMotel2) {

    }

}