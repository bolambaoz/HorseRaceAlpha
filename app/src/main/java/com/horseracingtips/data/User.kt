package com.horseracingtips.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val username: String? = null,
    val password: String? = null,
    val countryCode: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val date: String? = null
)


