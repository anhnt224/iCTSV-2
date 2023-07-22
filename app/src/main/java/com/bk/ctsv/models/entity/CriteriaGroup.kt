package com.bk.ctsv.models.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class CriteriaGroup (
    @SerializedName("CGId")
    var id: Int = 0,
    @SerializedName("CGName")
    var name: String = "",
    @SerializedName("CGPoint")
    var sPoint: Int = 0,
    @SerializedName("TCGPoint")
    var tPoint: Int = 0,
    @SerializedName("CGMaxPoint")
    var maxPoint: Int = 0,
    @SerializedName("UserCriteriaDetailsLst")
    var userCriteriaDetails: List<UserCriteriaDetail>
): Parcelable{

    // Constructor for Parcelable implementation
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.createTypedArrayList(UserCriteriaDetail) ?: emptyList()
    )

    // Override writeToParcel to write object data to the Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(sPoint)
        parcel.writeInt(tPoint)
        parcel.writeInt(maxPoint)
        parcel.writeTypedList(userCriteriaDetails)
    }

    companion object CREATOR : Parcelable.Creator<CriteriaGroup> {
        override fun createFromParcel(parcel: Parcel): CriteriaGroup {
            return CriteriaGroup(parcel)
        }

        override fun newArray(size: Int): Array<CriteriaGroup?> {
            return arrayOfNulls(size)
        }
    }

    fun getStudentPoint(): Int{
        var sPoint = 0
        userCriteriaDetails.forEach {
            sPoint += it.sPoint
        }
        if(maxPoint in 1 until sPoint){
            sPoint = maxPoint
        }
        if(maxPoint in (sPoint + 1)..-1){
            sPoint = maxPoint
        }
        return sPoint
    }

    override fun describeContents(): Int {
        return 0
    }
}