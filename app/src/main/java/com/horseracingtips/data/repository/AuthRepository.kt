package com.horseracingtips.data.repository

import com.horseracingtips.data.db.AppDatabase
import com.horseracingtips.data.network.MyApi
import com.horseracingtips.data.network.SafeApiRequest
import com.horseracingtips.data.network.response.RegisterResponse
import com.horseracingtips.data.network.response.VerifyResponse

class AuthRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun phoneValidate(
        phone: String
    ) : RegisterResponse {
        return apiRequest {
            api.userRegister(phone,"sms")
        }
    }

    suspend fun verification(
        code: String,
        to: String
    ) : VerifyResponse {
        return apiRequest {
            api.userVerification(code,to)
        }
    }
}