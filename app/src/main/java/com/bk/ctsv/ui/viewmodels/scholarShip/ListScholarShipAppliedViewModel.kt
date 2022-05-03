package com.bk.ctsv.ui.viewmodels.scholarShip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.ScholarShip
import com.bk.ctsv.repositories.ScholarShipRepository
import javax.inject.Inject

class ListScholarShipAppliedViewModel @Inject constructor(
    private val scholarShipRepository: ScholarShipRepository
) : ViewModel() {
    val listScholarShipsApplied: MediatorLiveData<Resource<List<ScholarShip>>> = MediatorLiveData()
    private lateinit var liveDataGetListScholarShipsApplied: LiveData<Resource<List<ScholarShip>>>
    fun getListScholarShips(){
        liveDataGetListScholarShipsApplied = scholarShipRepository.getListScholarShipsApplied(0, 1000)
        listScholarShipsApplied.removeSource(liveDataGetListScholarShipsApplied)
        listScholarShipsApplied.addSource(liveDataGetListScholarShipsApplied){
            listScholarShipsApplied.value = it
        }
    }
}