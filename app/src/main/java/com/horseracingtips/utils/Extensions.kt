package com.horseracingtips.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.horseracingtips.R
import com.horseracingtips.databinding.PopupDialogBinding
import java.util.*

fun Context.snackBar(msg: String, activity: Activity, save: Boolean = true){
    Snackbar.make(activity.findViewById(R.id.details_text), msg, Snackbar.LENGTH_LONG).apply {
        if (save){
            this.setAction("Ok"){}
        }
    }.show()
}

fun Context.getIpAddres() : String{
    val wifiManager = applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
    val ip: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
    return ip
}

fun getLocale(res: Resources): Locale {
    val config = res.configuration
    return if (Build.VERSION.SDK_INT >= 24) config.locales.get(0) else config.locale
}

fun Context.changeLocale(language:String): Context {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = this.resources.configuration
    config.setLocale(locale)
    return createConfigurationContext(config)
}

fun Context.popUpAds(context: Context, url: String){
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(URL_3WE)

    val binding: PopupDialogBinding = PopupDialogBinding.inflate(
        LayoutInflater.from(context),
        null,
        false
    )

    val dialog = Dialog(context)
    dialog.setContentView(binding.root)
    dialog.setCancelable(false)
    dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
    binding.apply {

        imgExit.setOnClickListener {
            dialog.dismiss()
        }

        btnClickhere.setOnClickListener {
            startActivity(openURL)
        }
    }

    dialog.show();
}

fun Context.toThreeLink(context: Context, link: String){
    val url = link
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(url)
    ContextCompat.startActivity(context, openURL, null)
}

