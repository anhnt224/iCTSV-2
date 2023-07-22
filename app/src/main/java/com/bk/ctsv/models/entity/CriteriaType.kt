package com.bk.ctsv.models.entity

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.google.gson.annotations.SerializedName

class CriteriaType (
    @SerializedName("CTId")
    var id: Int = 0,
    @SerializedName("CTName")
    var name: String = "",
    @SerializedName("CTPoint")
    var sPoint: Int = 0,
    @SerializedName("TCTPoint")
    var tPoint: Int = 0,
    @SerializedName("CTMaxPoint")
    var maxPoint: Int = 0,
    @SerializedName("CriteriaGroupDetailsLst")
    var criteriaGroups: List<CriteriaGroup>
): Parcelable{
    // Constructor for Parcelable implementation
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.createTypedArrayList(CriteriaGroup) ?: emptyList()
    )

    override fun describeContents(): Int {
        return 0
    }

    // Override writeToParcel to write object data to the Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(sPoint)
        parcel.writeInt(tPoint)
        parcel.writeInt(maxPoint)
        parcel.writeTypedList(criteriaGroups)
    }

    // Implementing Parcelable.Creator for creating the object from the Parcel
    companion object CREATOR : Parcelable.Creator<CriteriaType> {
        override fun createFromParcel(parcel: Parcel): CriteriaType {
            return CriteriaType(parcel)
        }

        override fun newArray(size: Int): Array<CriteriaType?> {
            return arrayOfNulls(size)
        }
    }
//

    fun getStudentPoint(): Int{
        var sPoint = 0
        criteriaGroups.forEach {criteriaGroup ->
            sPoint += criteriaGroup.getStudentPoint()
        }
        if(maxPoint in 1 until sPoint){
            sPoint = maxPoint
        }
        if(maxPoint in (sPoint + 1)..-1){
            sPoint = maxPoint
        }
        return sPoint
    }

    fun getTeacherScore(): Int{
        var score = 0
        criteriaGroups.forEach {criteriaGroup ->
            criteriaGroup.userCriteriaDetails.forEach {userCriteriaDetail ->
                score += userCriteriaDetail.tPoint
            }
        }
        return score
    }

    fun isActivityInvalid(activity: UserCriteriaActivity): Boolean{
        criteriaGroups.forEach {criteriaGroup ->
            criteriaGroup.userCriteriaDetails.forEach {userCriteriaDetail ->
                if (userCriteriaDetail.userCriteriaActivities.any {
                        it.aId == activity.aId
                    }){
                    return true
                }
            }
        }
        return false
    }
}