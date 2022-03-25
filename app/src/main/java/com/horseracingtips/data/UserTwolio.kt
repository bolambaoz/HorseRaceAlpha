package com.horseracingtips.data

import android.os.Parcelable
import androidx.room.Entity
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import java.util.*

var CURRENT_NEWS_ID = 0

@Entity(tableName = "horse_news")
@IgnoreExtraProperties
@Parcelize
data class UserTwolio(
    val username: String,
    val password: String,
    val countryCode: String,
    val phone: String,
    val email: String,
    val date: String
) : Parcelable