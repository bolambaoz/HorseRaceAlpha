package com.horseracingtips.ui.otp

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.horseracingtips.data.repository.AuthRepository
import com.horseracingtips.utils.Coroutines

class OtpViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var otpListener: OtpListener? = null

    fun codeVerification(code: String, phoneTo: String){
        Coroutines.main {
            val response = repository.verification(code, phoneTo)

            Log.d(TAG, response.toString())

            if (response.isApproved != null){
                otpListener?.onSuccess(response)
            }else if (!response.isApproved){
                otpListener?.onFailure("Not valid code")
            }
        }
    }

}