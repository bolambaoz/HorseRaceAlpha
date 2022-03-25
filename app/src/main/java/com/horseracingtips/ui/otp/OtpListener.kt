package com.horseracingtips.ui.otp

import com.horseracingtips.data.network.response.VerifyResponse

interface OtpListener {
    fun onSuccess(response: VerifyResponse)
    fun onFailure(message: String)
}