package com.bk.ctsv.teacher.viewmodel.scholarShip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bk.ctsv.common.Resource
import com.bk.ctsv.models.entity.ScholarShip
import com.bk.ctsv.repositories.ScholarShipRepository
import javax.inject.Inject

class TListScholarShipsViewModel @Inject constructor(
    private val scholarShipRepository: ScholarShipRepository
) : ViewModel() {
    private lateinit var liveDataGetListScholarShip: LiveData<Resource<List<ScholarShip>>>
    val listScholarShips: MediatorLiveData<Resource<List<ScholarShip>>> = MediatorLiveData()

    init {
        getListScholarShips(0, 1000)
    }

    fun getListScholarShips(page: Int, row: Int){
        liveDataGetListScholarShip = scholarShipRepository.getListScholarShips(page, row)
        listScholarShips.removeSource(liveDataGetListScholarShip)
        listScholarShips.addSource(liveDataGetListScholarShip){
            listScholarShips.value = it
        }
    }
}