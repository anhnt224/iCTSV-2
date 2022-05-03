package com.bk.ctsv.ui.viewmodels.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bk.ctsv.models.entity.Activity
import com.bk.ctsv.repositories.activity.ActivityRepository
import javax.inject.Inject

class ListActivityViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {
    private var _type = MutableLiveData<Type>()

    init {
        setType(Type.ALL)
    }

    var activities: LiveData<com.bk.ctsv.common.Resource<List<Activity>>> = Transformations.switchMap(_type){ type ->
        if (type == Type.ALL){
            activityRepository.getActivityByUserUnit()
        }else{
            activityRepository.getActivityByUser("", "", 1000, 0)
        }
    }

    fun setType(type: Type){
        _type.value = type
    }

    fun getType(): LiveData<Type>{
        return _type
    }

    enum class Type {
        ALL, JOINED
    }
}