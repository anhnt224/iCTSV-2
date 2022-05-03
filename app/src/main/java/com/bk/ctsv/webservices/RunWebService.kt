package com.bk.ctsv.webservices

import androidx.lifecycle.LiveData
import com.bk.ctsv.models.req.SaveRunDataReq
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.models.res.run.GetRunResultsListResp
import retrofit2.http.*

interface RunWebService {
    @POST("Run/SentResultRunLst")
    fun saveRunData(
        @Body saveRunDataReq: SaveRunDataReq
    ): LiveData<ApiResponse<MyCTSVCap>>

    @FormUrlEncoded
    @POST("Run/GetResultRunLst")
    fun getRunResultList(
        @Field("UserName") userName: String,
        @Field("TokenCode") token: String,
        @Field("TimeStart") timeStart: String,
        @Field("TimeEnd") timeEnd: String
    ): LiveData<ApiResponse<GetRunResultsListResp>>
}