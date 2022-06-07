package com.bk.ctsv.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

fun AppCompatActivity.setupToolbarWithHomeButton(toolbar: Toolbar){
    this.setSupportActionBar(toolbar)
    this.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    this.supportActionBar!!.setDisplayShowHomeEnabled(true)
}