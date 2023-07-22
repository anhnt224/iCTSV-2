package com.bk.ctsv.models.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class UserCriteriaDetail (
    @SerializedName("UCId")
    var id: Int = 0,
    @SerializedName("CId")
    var cID: Int = 0,
    @SerializedName("CName")
    var name: String = "",
    @SerializedName("UCDes")
    var description: String = "",
    @SerializedName("UCPoint")
    var sPoint: Int = 0,
    @SerializedName("TCPoint")
    var tPoint: Int = 0,
    @SerializedName("CMaxPoint")
    var maxPoint: Int = 0,
    @SerializedName("Proof")
    var proof: Int = 0,
    @SerializedName("ProofText")
    var proofText: Int = 0,
    @SerializedName("AssessUser")
    var assessUser: String = "",
    @SerializedName("UserCriteriaActivityLst")
    var userCriteriaActivities: ArrayList<UserCriteriaActivity>
): Parcelable{

    // Constructor for Parcelable implementation
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.createTypedArrayList(UserCriteriaActivity) ?: arrayListOf()
    )

    override fun describeContents(): Int {
        return 0
    }

    // Override writeToParcel to write object data to the Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(cID)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(sPoint)
        parcel.writeInt(tPoint)
        parcel.writeInt(maxPoint)
        parcel.writeInt(proof)
        parcel.writeInt(proofText)
        parcel.writeString(assessUser)
        parcel.writeTypedList(userCriteriaActivities)
    }

    // Implementing Parcelable.Creator for creating the object from the Parcel
    companion object CREATOR : Parcelable.Creator<UserCriteriaDetail> {
        override fun createFromParcel(parcel: Parcel): UserCriteriaDetail {
            return UserCriteriaDetail(parcel)
        }

        override fun newArray(size: Int): Array<UserCriteriaDetail?> {
            return arrayOfNulls(size)
        }
    }


    //
    fun needProof(): Boolean{
        return proof == 1
    }

    fun hasProof(): Boolean{
        return userCriteriaActivities.isNotEmpty()
    }

    fun isProofText(): Boolean{
        return proofText == 1
    }

    fun getNumberProof(): String {
        if(userCriteriaActivities.isNotEmpty()){
            return "Minh chứng hoạt động: ${userCriteriaActivities.size}MC"
        }
        return "Minh chứng hoạt động"
    }

    fun getProofStr(): String{
        if(description == ""){
            return "Khác(SV tự khai)"
        }
        return "Khác: $description"
    }

    fun updateSPoint(){
        sPoint = if(userCriteriaActivities.size == 0 && description == ""){
            0
        }else{
            maxPoint
        }
    }

    fun canMark(): Boolean{
        return hasProof() && sPoint > 0
    }

    fun getTeacherScore(): String{
        return "$tPoint/$maxPoint"
    }

    fun isMaxTeacherScore(): Boolean {
        return tPoint >= maxPoint
    }

    fun isEmptyTeacherScore(): Boolean {
        return tPoint <= 0
    }

}