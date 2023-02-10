package com.bk.ctsv

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bk.ctsv.databinding.ActivityMainBinding
import com.bk.ctsv.extension.showToast
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.models.entity.Version
import com.bk.ctsv.ui.fragments.DrawerLocker
import com.bk.ctsv.ui.fragments.Home2FragmentDirections
import com.bk.ctsv.ui.fragments.HomeFragmentDirections
import com.bk.ctsv.ui.fragments.activity.ListActivityFragmentDirections
import com.bk.ctsv.ui.fragments.help.HelpFragmentDirections
import com.bk.ctsv.ui.fragments.user.AccountFragmentDirections
import com.bk.ctsv.ui.fragments.user.MessageListFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.GsonBuilder
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.lang.Exception
import javax.inject.Inject

class MainActivity : AppCompatActivity(), DrawerLocker, HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> =
        dispatchingAndroidInjector

    private val remoteConfig = Firebase.remoteConfig

    @SuppressLint("PackageManagerGetSignatures")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpRemoteConfig()
        fetchRemoteConfig()
        checkVersion()

        FirebaseCrashlytics.getInstance().setUserId(sharedPrefsHelper.getUserName())

        navController = findNavController(this, R.id.home_fragment)
        setupBottomNavMenu(navController)
        setupWithNavController(navigation, navController)
        binding.fab.setOnClickListener {
            startQRcodeActitivity()
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        navigation?.let {
            setupWithNavController(it, navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                supportActionBar?.title = destination.label
                when (destination.id) {
                    R.id.homeFragment, R.id.accountFragment, R.id.helpFragment, R.id.messageListFragment, R.id.home2Fragment -> {
                        supportActionBar?.hide()
                        showNavigationBar()
                    }

                    R.id.tutorFragment, R.id.motelRegistrationProcessingFragment, R.id.motelRegistrationCompleteFragment -> {
                        supportActionBar?.hide()
                        hideNavigationBar()
                    }

                    else -> {
                        supportActionBar?.show()
                        hideNavigationBar()
                    }
                }
            }
        }

        binding.navigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.scanQR -> {
                    startQRcodeActitivity()
                }
            }
            false
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showNavigationBar() {
        binding.navigation.visibility = View.VISIBLE
        binding.coordinatorLayout.visibility = View.VISIBLE
        binding.bottom.visibility = View.VISIBLE
        supportActionBar?.setShowHideAnimationEnabled(false)
    }

    @SuppressLint("RestrictedApi")
    private fun hideNavigationBar() {
        binding.coordinatorLayout.visibility = View.GONE
        binding.navigation.visibility = View.GONE
        binding.bottom.visibility = View.GONE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setShowHideAnimationEnabled(false)
    }

    override fun setDrawerLocked(shouldLock: Boolean) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpRemoteConfig() {
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    private fun fetchRemoteConfig() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    remoteConfig.activate()
                }
            }
    }

    private fun checkVersion() {
        try {
            val version = GsonBuilder().create()
                .fromJson(remoteConfig.getString("android_version"), Version::class.java)
            if (BuildConfig.VERSION_CODE < version.version) {
                val dialog = MaterialAlertDialogBuilder(this)
                    .setTitle("Bản cập nhật mới!")
                    .setMessage("Đã có bản cập nhật ${version.versionName}, bạn có muốn cập nhật?")
                    .setPositiveButton("Cập nhật") { _, _ ->
                        update(version.link)
                    }
                if (!version.require) {
                    dialog.setNegativeButton("Huỷ", null)
                }
                dialog.show()
            }
        } catch (e: Exception) {

        }
    }

    private fun update(link: String) {
        try {
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun startQRcodeActitivity() {
        Log.d("_SCAN", "QR")
        IntentIntegrator(this).apply {
            setBeepEnabled(true)
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            setPrompt("Quét mã QR hoạt động")
            setCameraId(0)
            setOrientationLocked(true)
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
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
            }else if (studentId != sharedPrefsHelper.getUserName()){
                showToast("Bạn không thể xem thông tin của người khác")
            }else{
                showStudentInfoDialog(studentId)
            }
        }else{
            showActivityInfo(qrValue)
        }
    }

    private fun showActivityInfo(qrValue: String){
        try {
            val aID = qrValue.toInt()
            super.onPostResume()
            when (navController.currentDestination?.id) {
                R.id.home2Fragment -> {
                    navController.navigate(
                        Home2FragmentDirections.actionHome2FragmentToActivityDetailByUserUnitFragment(
                            aID
                        )
                    )
                }
                R.id.homeFragment -> {
                    navController.navigate(
                        HomeFragmentDirections.actionHomeFragmentToActivityDetailByUserUnitFragment(
                            aID
                        )
                    )
                }

                R.id.accountFragment -> {
                    navController.navigate(
                        AccountFragmentDirections.actionAccountFragmentToActivityDetailByUserUnitFragment(
                            aID
                        )
                    )
                }

                R.id.messageListFragment -> {
                    navController.navigate(
                        MessageListFragmentDirections.actionMessageListFragmentToActivityDetailByUserUnitFragment(
                            aID
                        )
                    )
                }

                R.id.helpFragment -> {
                    navController.navigate(
                        HelpFragmentDirections.actionHelpFragmentToActivityDetailByUserUnitFragment(
                            aID
                        )
                    )
                }
            }
        } catch (e: Exception) {
            showToast("Hoạt động này không tồn tại")
        }
    }

    private fun showStudentInfoDialog(studentId: String){
        MaterialAlertDialogBuilder(this)
            .setMessage("Bạn có muốn xem phiếu thông tin tình hình học tập mà giáo viên gửi về cho phụ huynh không?!")
            .setTitle("Xem thông tin kết quả học tập")
            .setPositiveButton("Xem thông tin"){_, _ ->
                val token = sharedPrefsHelper.getToken()
                showStudentInfo(studentId, token)
            }.setNegativeButton("Đóng", null)
            .show()
    }

    private fun showStudentInfo(studentId: String, urlToken: String){
        val url = getStudentInfoUrl(studentId, urlToken)
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        if (browserIntent.resolveActivity(this.packageManager) != null) {
            startActivity(browserIntent)
        }
    }

    private fun getStudentInfoUrl(studentId: String, urlToken: String): String {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val baseUrl = remoteConfig.getString("student_info_url")
        return "$baseUrl$studentId/$urlToken"
    }
}

