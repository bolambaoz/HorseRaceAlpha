package com.horseracingtips.ui.registration

import com.horseracingtips.data.network.response.RegisterResponse

interface RegisterListener {
    fun onStarted(message: String)
    fun onSuccess(response: RegisterResponse)
    fun onFailure(message: String)
}