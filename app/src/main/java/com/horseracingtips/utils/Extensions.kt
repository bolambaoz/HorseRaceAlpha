package com.horseracingtips.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.horseracingtips.R
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

//class ContextUtils(base: Context) : ContextWrapper(base) {
//
//    companion object {
//
//        fun updateLocale(c: Context, localeToSwitchTo: Locale): ContextWrapper {
//            var context = c
//            val resources: Resources = context.resources
//            val configuration: Configuration = resources.configuration
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                val localeList = LocaleList(localeToSwitchTo)
//                LocaleList.setDefault(localeList)
//                configuration.setLocales(localeList)
//            } else {
//                configuration.locale = localeToSwitchTo
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
//                context = context.createConfigurationContext(configuration)
//            } else {
//                resources.updateConfiguration(configuration, resources.displayMetrics)
//            }
//            return ContextUtils(context)
//        }
//    }
//}


//fun Context.popUpAds(context: Context, url: String){
//    val horseUrl = "https://live.3wehorse.com/"
//    val dialog = Dialog(
//        context,
//        android.R.style.Theme_Material_Light_NoActionBar_Fullscreen
//    )
//    dialog.setContentView(R.layout.popup_ads_dialog)
//    dialog.setCancelable(false)
//
//    dialog.webview.apply {
//        this.loadUrl(url)
//        settings.javaScriptEnabled = true
//    }
//    dialog.img_exit.setOnClickListener{
//        dialog.webview.removeAllViews()
//        dialog.webview.destroy()
//        dialog.webview.clearCache(true)
//        dialog.webview.clearHistory()
//        dialog.dismiss()
//    }
//
//    dialog.show()
//}

fun Context.toThreeLink(context: Context, link: String){
    val url = link
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(url)
    ContextCompat.startActivity(context, openURL, null)
}

