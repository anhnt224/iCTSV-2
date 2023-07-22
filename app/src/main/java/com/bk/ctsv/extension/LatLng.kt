package com.bk.ctsv.extension

import com.google.android.gms.maps.model.LatLng

fun LatLng.getLocationStr(): String {
    return String.format("%.4f°B - %.4f°Đ", this.latitude, this.longitude)
}