package com.bk.ctsv.ui.viewmodels.running

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.run.RunResult
import com.bk.ctsv.repositories.RunRepository
import javax.inject.Inject

class RunningDataViewModel @Inject constructor(
    private val runRepository: RunRepository
) : ViewModel() {

}