package com.horserace.ui.channels

import com.horserace.data.network.response.HorseRaceResponse

interface GalleryListener {
    fun onStarted(message: String)
    fun onSuccess(loginResponse: HorseRaceResponse)
    fun onFailure(message: String)
}