package com.horserace.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

var CURRENT_NEWS_ID = 0

@Entity(tableName = "horse_news")
data class HorseNews(
    @PrimaryKey(autoGenerate = false)
    var id : String,
    var title: String? = null,
    var description: String? = null,
    var imageUrl: String? = null
)

//title: req.body.title,
//description: req.body.description,
//imageUrl: req.body.imageUrl