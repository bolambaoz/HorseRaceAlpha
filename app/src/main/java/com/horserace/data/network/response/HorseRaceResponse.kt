package com.horserace.data.network.response

import com.horserace.data.db.entity.HorseRace
import com.horserace.data.db.entity.HorseVideo
import com.horserace.data.db.entity.User

data class HorseRaceResponse(
    var message: String?,
    var isActive: Boolean?,
    val video: List<HorseVideo>?
)
