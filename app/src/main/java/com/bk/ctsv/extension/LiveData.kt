package com.bk.ctsv.extension;

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notifyObserver(){
    this.value = this.value
}
