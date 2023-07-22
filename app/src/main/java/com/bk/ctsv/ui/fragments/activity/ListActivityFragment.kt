package com.bk.ctsv.ui.fragments.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.MainActivity
import com.bk.ctsv.R
import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.common.RetryCallback
import com.bk.ctsv.databinding.ListActivityFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.extension.checkResource
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.models.entity.Student
import com.bk.ctsv.ui.adapter.activity.ActivityAdapter
import com.bk.ctsv.ui.viewmodels.activity.ListActivityViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.zxing.integration.android.IntentIntegrator
import javax.inject.Inject


class ListActivityFragment : Fragment(), ActivityAdapter.OnItemClickListener, Injectable {
    private lateinit var viewModel: ListActivityViewModel
    private lateinit var binding: ListActivityFragmentBinding
    private lateinit var activityAdapter: ActivityAdapter
    @Inject lateinit var appExecutors: AppExecutors
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private var type: ListActivityViewModel.Type? = null
    private var activities: ArrayList<Activity> = arrayListOf()
    private lateinit var searchView: SearchView
    @Inject lateinit var sharedPrefHelper: SharedPrefsHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpViewModel()
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.list_activity_fragment, container, false)
        showTabbar()

        activityAdapter = ActivityAdapter(appExecutors, this)
        binding.apply {
            recyclerview.apply {
                adapter = activityAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    activityAdapter.submitList(null)
                    activityAdapter.notifyDataSetChanged()
                    searchView.onActionViewCollapsed()
                    when(tab?.position){
                        0 -> {
                            viewModel.setType(ListActivityViewModel.Type.ALL)
                        }
                        else -> {
                            viewModel.setType(ListActivityViewModel.Type.JOINED)
                        }
                    }
                }
            })

            callback = object : RetryCallback{
                override fun retry() {
                    if(type != null){
                        viewModel.setType(type!!)
                    }
                }

            }
        }
        subscribeUI()
        return binding.root
    }
    
    private fun setUpViewModel(){
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListActivityViewModel::class.java)
    }
    
    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeUI(){
        with(viewModel){
            activities.observe(viewLifecycleOwner){resource ->
                binding.resource = resource
                if(checkResource(resource) && resource.data != null){
                    this@ListActivityFragment.activities = arrayListOf()
                    this@ListActivityFragment.activities.addAll(resource.data)
                    activityAdapter.submitList(this@ListActivityFragment.activities.toList())
                    activityAdapter.notifyDataSetChanged()
                }
            }
            getType().observe(viewLifecycleOwner){type ->
                this@ListActivityFragment.type = type
                when(type){
                    ListActivityViewModel.Type.ALL -> {
                        binding.tabLayout.getTabAt(0)?.select()
                    }

                    ListActivityViewModel.Type.JOINED -> {
                        binding.tabLayout.getTabAt(1)?.select()
                    }
                }
            }
        }
    }

    private fun showTabbar(){
        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).supportActionBar?.title = "Danh sách hoạt động"
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list_activity, menu)

        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            @SuppressLint("DefaultLocale")
            override fun onQueryTextSubmit(searchText: String?): Boolean {
                val filteredActivity = activities.filter { activity ->
                    activity.name!!.toLowerCase().contains(searchText!!.toLowerCase())
                }
                activityAdapter.submitList(filteredActivity)
                activityAdapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

        val closeButton = searchView.findViewById<ImageView>(R.id.search_close_btn)
        closeButton.setOnClickListener {
            activityAdapter.submitList(activities.toList())
            activityAdapter.notifyDataSetChanged()
            searchView.onActionViewCollapsed()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.scanQR -> {
                startQRcodeActitivity()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onItemClick(activity: Activity) {
        Navigation.findNavController(requireView()).
        navigate(ListActivityFragmentDirections.actionListActivityFragmentToActivityDetailByUserUnitFragment(activity.id))
    }

    private fun startQRcodeActitivity() {
        Log.d("_SCAN", "QR")
        IntentIntegrator.forSupportFragment(this).apply {
            setBeepEnabled(true)
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            setPrompt("Quét mã QR hoạt động, thẻ sinh viên")
            setCameraId(0)
            setOrientationLocked(true)
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        Log.d("_SCAN", "QR")
        if (result != null) {
            if (result.contents != null){
                handleQrCode(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleQrCode(qrValue: String) {
        if (qrValue.contains("https://ctsv.hust.edu.vn/") && qrValue.contains("/card/")){
            val arr = qrValue.split("/")
            val studentId = arr.firstOrNull {
                it.length == 8 && it.dropLast(6) == "20"
            }
            if (studentId == null) {
                showToast("Mã QR không hợp lệ")
            }else if (studentId != sharedPrefHelper.getUserName()){
                showToast("Bạn không thể xem thông tin của người khác")
            }else{
                showStudentInfoDialog(studentId)
            }
        }else{
            try {
                val aID = qrValue.toInt()
                Navigation.findNavController(requireView()).
                navigate(ListActivityFragmentDirections.actionListActivityFragmentToActivityDetailByUserUnitFragment(aID))
            }catch (e: Exception){
                showToast("Hoạt động này không tồn tại")
            }
        }
    }

    private fun showStudentInfoDialog(studentId: String){
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Bạn có muốn xem phiếu thông tin tình hình học tập mà giáo viên gửi về cho phụ huynh không?!")
            .setTitle("Xem thông tin kết quả học tập")
            .setPositiveButton("Xem thông tin"){_, _ ->
                val token = sharedPrefHelper.getToken()
                showStudentInfo(studentId, token)
            }.setNegativeButton("Đóng", null)
            .show()
    }

    private fun showStudentInfo(studentId: String, urlToken: String){
        val url = getStudentInfoUrl(studentId, urlToken)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        if (browserIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(browserIntent)
        }
    }

    private fun getStudentInfoUrl(studentId: String, urlToken: String): String {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val baseUrl = remoteConfig.getString("student_info_url")
        return "$baseUrl$studentId/$urlToken"
    }

}