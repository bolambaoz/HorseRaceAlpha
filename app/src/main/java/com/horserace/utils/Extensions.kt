package com.horserace.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.GsonBuilder
import com.horserace.R
import kotlinx.android.synthetic.main.popup_ads_dialog.*
import okhttp3.*
import java.io.IOException

fun Context.getIpAddres() : String{
    val wifiManager = applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
    val ip: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
    return ip
}

fun Context.popUpAds(context: Context, url: String){
    val dialog = Dialog(
        context,
        android.R.style.Theme_Material_Light_NoActionBar_Fullscreen
    )
    dialog.setContentView(R.layout.popup_ads_dialog)
    dialog.setCancelable(false)

    dialog.webview.apply {
        this.loadUrl(url)
        settings.javaScriptEnabled = true
    }
    dialog.img_exit.setOnClickListener{
        dialog.webview.removeAllViews()
        dialog.webview.destroy()
        dialog.webview.clearCache(true)
        dialog.webview.clearHistory()
        dialog.dismiss()
    }

    dialog.btn_clickhere.setOnClickListener{
        toThreeLink(context)
    }
    dialog.show()
}


private fun Context.toThreeLink(context: Context){
    val url = "https://asia3we.com/"
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(url)
    ContextCompat.startActivity(context, openURL, null)
}