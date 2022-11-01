package com.bk.ctsv.modules.searchMotel.model

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.bk.ctsv.R
import com.bk.ctsv.extension.convertDateToStringDateTime
import com.bk.ctsv.extension.toCurrency
import com.bk.ctsv.extension.toDate
import java.io.Serializable

class MotelRegistration(
    var id: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var locationName: String = "",
    var range: Int = 500,
    var address: String = "",
    var type: String = "",
    var numberOfPeople: Int = 0,
    var maxPrice: Int = 0,
    var minPrice: Int = 0,
    var userRequests: String = "",
    var liveWithOther: Boolean = false,
    var startDate: String = "",
    var endDate: String = "",
    var statusCode: Int = 0,
    var createdTime: String = "2022-10-31 16:50:00",
    var fee: Int = 50000
): Serializable{
    fun getStatus(): MotelRegistrationStatus {
        return when (statusCode){
            1 -> MotelRegistrationStatus.NEW
            2 -> MotelRegistrationStatus.PENDING
            3 -> MotelRegistrationStatus.PROCESSING
            4 -> MotelRegistrationStatus.COMPLETION
            5 -> MotelRegistrationStatus.FAILURE
            -1 -> MotelRegistrationStatus.CANCELED
            -2 -> MotelRegistrationStatus.DELETED
            else -> MotelRegistrationStatus.OTHER
        }
    }

    fun getNumberOfPeopleStr(): String {
        return "$numberOfPeople người ở"
    }

    fun getNumberOfPeopleStr2(): String {
        return "$numberOfPeople"
    }

    fun getPriceStr(): String {
        return "Giá từ ${minPrice.toCurrency()} đến ${maxPrice.toCurrency()}"
    }

    fun getPriceStr2(): String {
        return "${minPrice.toCurrency()} - ${maxPrice.toCurrency()}"
    }

    fun getCreatedTimeStr(): String {
        return createdTime.toDate()?.convertDateToStringDateTime() ?: run {
            ""
        }
    }

    fun getLocationStr(): String {
        return if (range < 1000){
            "$locationName | ${range}m"
        }else{
            "$locationName | ${String.format("%.1f", range.toDouble() / 1000)}m"
        }
    }

    fun getLiveWithOtherStr(): String {
        return if (liveWithOther) "Có" else "Không"
    }

    fun getFeeTitleSpan(context: Context): SpannableStringBuilder{
        val feeStr = fee.toCurrency()
        val feeTitle =
            "Yêu cầu của bạn đã được xác nhận, bạn cần trả một khoản phí là $feeStr để chúng tôi hỗ trợ tìm trọ cho bạn."

        val startIndex = feeTitle.indexOf(feeStr)
        val length = feeStr.length
        val spanContent = SpannableStringBuilder(feeTitle)

        val bold = StyleSpan(Typeface.BOLD)
        val greenText =
            ForegroundColorSpan(ContextCompat.getColor(context,R.color.green500))
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