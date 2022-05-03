package com.bk.ctsv.models.entity

import com.google.firebase.database.ServerValue

class FakeGPSInfo (
    var studentName: String = "",
    var time: Any?,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var eventName: String = "",
    var email: String = ""
)