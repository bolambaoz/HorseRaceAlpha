package com.horseracingtips.data.network.response

import com.horseracingtips.data.db.entity.HorseVideo

data class HorseRaceResponse(
    var message: String?,
    var isActive: Boolean?,
    val video: List<HorseVideo>?
)
