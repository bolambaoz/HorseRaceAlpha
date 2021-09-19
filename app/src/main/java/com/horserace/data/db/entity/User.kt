package com.horserace.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    var id: String? = null,
    var token: String? = null,
    var location: String? = null,
    var email: String? = null
){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}
