package com.horseracingtips.ui.dashboard

import com.horseracingtips.data.network.response.HorseRaceResponse

interface HomeListener {
    fun onStarted(message: String, from : String)
    fun onSuccess(loginResponse: HorseRaceResponse)
    fun onFailure(message: String)
    fun onLoading()
}