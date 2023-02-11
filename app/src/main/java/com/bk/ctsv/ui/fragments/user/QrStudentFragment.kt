package com.bk.ctsv.ui.fragments.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bk.ctsv.R
import com.bk.ctsv.databinding.QrStudentFragmentBinding
import com.bk.ctsv.di.Injectable
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.ui.viewmodels.user.QrStudentViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.oned.Code128Writer
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URL
import javax.inject.Inject

class QrStudentFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = QrStudentFragment()
    }

    private lateinit var viewModel: QrStudentViewModel
    private lateinit var binding: QrStudentFragmentBinding
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.qr_student_fragment, container, false)
        binding.apply {
            textStudentName.text = sharedPrefsHelper.getFullName()
            textStudentID.text = sharedPrefsHelper.getUserName()
            imageBarCode.setImageBitmap(createBarcodeBitmap(sharedPrefsHelper.getUserName()))
            imageQR.setImageBitmap(generateQRCode(sharedPrefsHelper.getUserName()))
        }
        showStudentAvatar()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(QrStudentViewModel::class.java)
    }

    private fun createBarcodeBitmap(
        barcodeValue: String,
        widthPixels: Int = 1000,
        heightPixels: Int = 1000
    ): Bitmap {
        val bitMatrix = Code128Writer().encode(
            barcodeValue,
            BarcodeFormat.CODE_128,
            widthPixels,
            heightPixels
        )

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )
        return bitmap
    }

    private fun generateQRCode(text: String): Bitmap {
        val width = 1000
        val height = 1000
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d("TAG", "generateQRCode: ${e.message}")
        }
        return bitmap
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
                    binding.studentImage.setImageBitmap(decodedByte)
                }
            }
        })
    }

}