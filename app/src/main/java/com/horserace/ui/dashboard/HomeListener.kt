package com.horserace.ui.dashboard

import com.horserace.data.network.response.HorseRaceResponse

interface HomeListener {
    fun onStarted(message: String, from : String)
    fun onSuccess(loginResponse: HorseRaceResponse)
    fun onFailure(message: String)
    fun onLoading()
}