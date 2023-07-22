package com.bk.ctsv.models.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "UserCriteriaActivity")
class UserCriteriaActivity(
    @PrimaryKey
    @ColumnInfo(name = "UCAId") @SerializedName("UCAId")
    var ucaId : Int = 0,
    @ColumnInfo(name = "CId") @SerializedName("CId")
    var cId : Int = 0,
    @ColumnInfo(name = "AId") @SerializedName("AId")
    var aId : Int = 0,
    @ColumnInfo(name = "CreateDate") @SerializedName("CreateDate")
    var createDate : Date? = null,
    @ColumnInfo(name = "CreateUser") @SerializedName("CreateUser")
    var createUser : String? = "",
    @ColumnInfo(name = "UCAStatus") @SerializedName("UCAStatus")
    var ucaStatus : Int? = 0,
    @SerializedName("StartTime")
    var startTime: Date? = null,
    @SerializedName("FinishTime")
    var finishTime: Date? = null,
    @SerializedName("AName")
    var name: String = ""


): Parcelable {
    // Constructor for Parcelable implementation
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readSerializable() as? Date,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readSerializable() as? Date,
        parcel.readSerializable() as? Date,
        parcel.readString() ?: ""
    )

    constructor(activity: Activity): this(aId = activity.id, name = activity.name ?: "")

    override fun describeContents(): Int {
        return 0
    }

    // Override writeToParcel to write object data to the Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ucaId)
        parcel.writeInt(cId)
        parcel.writeInt(aId)
        parcel.writeSerializable(createDate)
        parcel.writeString(createUser)
        parcel.writeValue(ucaStatus)
        parcel.writeSerializable(startTime)
        parcel.writeSerializable(finishTime)
        parcel.writeString(name)
    }

    // Implementing Parcelable.Creator for creating the object from the Parcel
    companion object CREATOR : Parcelable.Creator<UserCriteriaActivity> {
        override fun createFromParcel(parcel: Parcel): UserCriteriaActivity {
            return UserCriteriaActivity(parcel)
        }

        override fun newArray(size: Int): Array<UserCriteriaActivity?> {
            return arrayOfNulls(size)
        }
    }

}