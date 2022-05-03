package com.bk.ctsv.ui.fragments.help

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bk.ctsv.R
import com.bk.ctsv.databinding.HelpFragmentBinding
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.models.entity.Help
import com.bk.ctsv.models.entity.Tutorial
import com.bk.ctsv.ui.adapter.OnItemClickListener
import com.bk.ctsv.ui.adapter.help.HelpAdapter
import com.bk.ctsv.ui.viewmodels.help.HelpViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class HelpFragment : Fragment(), OnItemClickListener<Tutorial> {

    companion object {
        fun newInstance() = HelpFragment()
    }

    private lateinit var viewModel: HelpViewModel
    private var remoteConfig = Firebase.remoteConfig
    private lateinit var binding: HelpFragmentBinding
    var helps: List<Help> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        setUpRemoteConfig()
//        fetchRemoteConfig()

        val helpsStr = remoteConfig.getString("helps")
        if(helpsStr.isNotEmpty() && GsonBuilder().create().fromJson(helpsStr, Array<Help>::class.java) != null){
            helps = GsonBuilder().create().fromJson(helpsStr, Array<Help>::class.java).toList()
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.help_fragment, container, false)
        val helpAdapter = HelpAdapter(helps, this)
        binding.recyclerView.apply {
            adapter = helpAdapter
            layoutManager = LinearLayoutManager(context)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HelpViewModel::class.java)
    }

//    private fun setUpRemoteConfig(){
//        val configSettings = remoteConfigSettings {
//            minimumFetchIntervalInSeconds = 0
//        }
//        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
//    }
//
//    private fun fetchRemoteConfig(){
//        remoteConfig.fetchAndActivate()
//            .addOnCompleteListener {task ->
//                if(task.isSuccessful){
//                    remoteConfig.activate()
//                }
//            }
//    }

    override fun onClick(value: Tutorial) {
        openLink(value.link)
    }

    private fun openLink(link: String){
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }catch (e: Exception){

        }
    }

}