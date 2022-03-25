package com.horseracingtips.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.horseracingtips.ENGLISH
import com.horseracingtips.LANGUAGE_KEY
import java.time.LocalDate
import java.util.*
import java.util.regex.Pattern

const val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"


fun isValidEmaillId(email: String?): Boolean {
    return Pattern.compile(EMAIL_PATTERN).matcher(email).matches()
}

fun convertEmailToValidString(email: String?): String {
    val emailValue = email?.replace("@","")
    val emailValueFinal = emailValue?.replace(".", "")
    return emailValueFinal.toString().lowercase()
}

fun languageSetup(context: Context?): String? {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val currentLocale = currentLanguage(context)
    return if (!prefs.contains(LANGUAGE_KEY)) ENGLISH else prefs.getString(
        LANGUAGE_KEY,
        currentLocale.toString()
    )
}

fun currentLanguage(context: Context?): Locale? {
    return context?.let { getLocale(it.resources) }
}

fun currentDate(): String{
    val date: LocalDate = LocalDate.now()
    return date.toString()
}