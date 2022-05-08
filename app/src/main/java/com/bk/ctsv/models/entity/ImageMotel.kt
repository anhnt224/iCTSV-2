package com.bk.ctsv.models.entity

import androidx.room.Entity

@Entity(tableName = "ImageMotel",primaryKeys = ["idMotel", "time"])
class ImageMotel (
        var idMotel: Int = -1,
        var type: Int? = null,
        var image: String? = "",
        var status: Int? = null,
        var file : String? = "",
        var time: String = ""
        ){

//        fun getNameStatus():String{
//                when (status){
//                        0 -> return  "Luu Offline"
//                        1 -> return "Tai len Thanh Cong"
//                        2 -> return "Da xoa"
//                }
//        }
}