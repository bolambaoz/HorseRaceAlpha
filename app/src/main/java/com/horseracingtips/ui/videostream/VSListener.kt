package com.horseracingtips.ui.videostream

import com.horseracingtips.data.network.response.HorseRaceResponse

interface VSListener  {
    fun onStarted(message: String, from : String)
    fun onSuccess(loginResponse: HorseRaceResponse)
    fun onFailure(message: String)
    fun onLoading()
}