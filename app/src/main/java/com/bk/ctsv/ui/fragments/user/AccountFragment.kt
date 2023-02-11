package com.bk.ctsv.ui.fragments.user

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.bk.ctsv.BuildConfig
import com.bk.ctsv.MainActivity
import com.bk.ctsv.R
import com.bk.ctsv.databinding.AccountFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.extension.logoutClick
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.ui.viewmodels.user.AccountViewModel
import com.bk.ctsv.utilities.runOnIoThread
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IPublicClientApplication
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalException
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import javax.inject.Inject

class AccountFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = AccountFragment()
    }

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: AccountFragmentBinding
    private val remoteConfig = Firebase.remoteConfig
    private var mSingleAccountApp: ISingleAccountPublicClientApplication? = null
    private var mAccount: IAccount? = null


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.account_fragment, container, false)
        (activity as MainActivity).supportActionBar?.hide()

        binding.apply {
            textVersion.text = "Phiên bản ${BuildConfig.VERSION_NAME}"
            textFullName.text = sharedPrefsHelper.getFullName()
            textStudentID.text = sharedPrefsHelper.getUserName()

            viewLogout.setOnClickListener {
                logout()
            }
            viewQRStudent.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(AccountFragmentDirections.actionAccountFragmentToQrStudentFragment())
            }
            viewUserInfo.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(AccountFragmentDirections.actionAccountFragmentToUserInfoFragment())
            }
            viewChangePassword.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(AccountFragmentDirections.actionAccountFragmentToChangePasswordFragment())
            }
            viewBook.setOnClickListener {
                openLink(remoteConfig.getString("book"))
            }
            viewFeedback.setOnClickListener {
                openLink(remoteConfig.getString("feedback"))
            }
            viewError.setOnClickListener {
                openLink(remoteConfig.getString("error"))
            }
            viewAbout.setOnClickListener {
                openLink(remoteConfig.getString("aboutCTSV"))
            }
            viewAddress.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(AccountFragmentDirections.actionAccountFragmentToListAddressFragment())
                //Navigation.findNavController(requireView()).navigate(AccountFragmentDirections.actionAccountFragmentToImageMotelFragment(2))
            }
            viewNotes.setOnClickListener {
                Navigation.findNavController(requireView())
                    .navigate(AccountFragmentDirections.actionAccountFragmentToNotesFragment())
            }
            runningView.setOnClickListener {
                openRunningFragment()
            }
        }

        PublicClientApplication.createSingleAccountPublicClientApplication(
            requireContext(),
            R.raw.auth_config_single_account,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication?) {
                    mSingleAccountApp = application
                    loadAccount()
                }

                override fun onError(exception: MsalException?) {
                    Log.d("_LOGIN_WITH_MS", "$exception")
                }
            })
        showStudentAvatar()

        return binding.root
    }

    private fun openRunningFragment() {
        Navigation.findNavController(requireView())
            .navigate(AccountFragmentDirections.actionAccountFragmentToRunDashboardFragment())

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
    }

    private fun logout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Bạn có chắc chắn muốn đăng xuất không?")
            .setNegativeButton("Đăng xuất") { _, _ ->
                runOnIoThread {
                    logoutMicrosoftAccount()
                }
                logoutClick(sharedPrefsHelper)
            }.setPositiveButton("Hủy") { _, _ ->

            }.show()
    }

    private fun logoutMicrosoftAccount() {
        if (mAccount == null) {
            return
        }

        /**
         * Removes the signed-in account and cached tokens from this app
         */
        mSingleAccountApp!!.signOut()
    }

    /**
     * Load current account in caches
     */
    private fun loadAccount() {
        if (mSingleAccountApp == null) {
            return
        }

        mSingleAccountApp!!.getCurrentAccountAsync(object :
            ISingleAccountPublicClientApplication.CurrentAccountCallback {
            override fun onAccountLoaded(activeAccount: IAccount?) {
                mAccount = activeAccount
            }

            override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {

            }

            override fun onError(exception: MsalException) {

            }

        })
    }

    private fun openLink(link: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    private fun showStudentAvatar() {
        val userName = sharedPrefsHelper.getUserName()
        val url =
            URL("https://bkapis.hust.edu.vn/api/getimagebystudentid?mssv=$userName&mssv=$userName")
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body()?.string()
                requireActivity().runOnUiThread {
                    val decodedString =
                        Base64.decode(responseBody?.replace("\"", ""), Base64.DEFAULT)
                    val decodedByte =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    binding.imageStudent.setImageBitmap(decodedByte)
                }
            }
        })
    }
}