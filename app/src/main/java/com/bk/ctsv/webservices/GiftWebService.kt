package com.bk.ctsv.webservices

import com.bk.ctsv.models.res.MyCTSVCap
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface GiftWebService {
    @Multipart
    @POST("ATMGift/UploadImageGift")
    fun uploadImageGift(
        @Query("UserName") userName: String,
        @Query("TokenCode") token: String,
        @Query("GiftID") giftId: Int,
        @Query("TypeImage") typeImage: Int,
        @Part image: MultipartBody.Part
    ): Call<MyCTSVCap>
}