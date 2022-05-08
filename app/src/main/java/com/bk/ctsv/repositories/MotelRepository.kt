package com.bk.ctsv.repositories

import com.bk.ctsv.common.AppExecutors
import com.bk.ctsv.helper.SharedPrefsHelper
import com.bk.ctsv.webservices.WebService
import javax.inject.Inject

class MotelRepository @Inject constructor(
    private val webservice: WebService,
    private val appExecutors: AppExecutors,
    private val sharedPrefsHelper: SharedPrefsHelper
) {

}