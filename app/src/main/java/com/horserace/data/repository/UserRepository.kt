package com.horserace.data.repository

import com.horserace.data.db.AppDatabase
import com.horserace.data.db.entity.User
import com.horserace.data.network.MyApi
import com.horserace.data.network.SafeApiRequest
import com.horserace.data.network.response.AuthResponse

class UserRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

//    suspend fun userLogin(phone: String, password: String) : AuthResponse {
//        return  apiRequest {
//            //api.userLogin(phone,password)
//        }
//    }
//
//    suspend fun userSignup(
//        name: String,
//        password: String,
//        phone: String,
//        location: String,
//        email: String
//    ) : AuthResponse {
//        return apiRequest {
//          //  api.userSignup(name,password, phone, location, email)
//        }
//    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getUser()

}