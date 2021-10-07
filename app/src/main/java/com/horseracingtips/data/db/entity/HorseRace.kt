package com.horseracingtips.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_HORSERACE_ID = 0

@Entity(tableName = "horse_race")
data class HorseRace(
    var message: String? = null,
    var isActive: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var uid: Int = CURRENT_HORSERACE_ID
}
