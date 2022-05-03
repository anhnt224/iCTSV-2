package com.bk.ctsv.ui.viewmodels.scholarShip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.res.MyCTSVCap
import com.bk.ctsv.repositories.ScholarShipRepository
import javax.inject.Inject

class ScholarShipDetailViewModel @Inject constructor(
    private val scholarShipRepository: ScholarShipRepository
) : ViewModel() {
    private lateinit var liveDataApplyScholarShip: LiveData<Resource<MyCTSVCap>>
    val applyScholarShip: MediatorLiveData<Resource<MyCTSVCap>> = MediatorLiveData()

    fun applyScholarShip(scholarShipID: Int){
        liveDataApplyScholarShip = scholarShipRepository.applyScholarShip(scholarShipID)
        applyScholarShip.removeSource(liveDataApplyScholarShip)
        applyScholarShip.addSource(liveDataApplyScholarShip){
            applyScholarShip.value = it
        }
    }
}