package com.horserace.utils

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import androidx.appcompat.app.AppCompatActivity

fun Context.getIpAddres() : String{
    val wifiManager = applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
    val ip: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
    return ip
}