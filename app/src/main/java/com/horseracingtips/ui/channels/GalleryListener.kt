package com.horseracingtips.ui.channels

import com.horseracingtips.data.network.response.HorseRaceResponse

interface GalleryListener {
    fun onStarted(message: String, from : String)
    fun onSuccess(loginResponse: HorseRaceResponse)
    fun onFailure(message: String)
    fun onLoading()
}