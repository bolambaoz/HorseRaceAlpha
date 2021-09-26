package com.horserace.data.network

import com.horserace.data.db.entity.HorseNews
import com.horserace.data.network.response.AuthResponse
import com.horserace.data.network.response.GliveResponse
import com.horserace.data.network.response.HorseRaceResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MyApi {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun userLogin(
        @Field("phone") phone: String,
        @Field("password") password: String
    ) : Response<AuthResponse>

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun userSignup(
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("location") location: String,
        @Field("email") email: String
    ) : Response<AuthResponse>
//
//    @Headers(
//        "Content-Type: application/json",
//        "auth-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MTNlYmY1MjdkMWIyYWQ5MmU5MGVjOWUiLCJpYXQiOjE2MzE1MDQyMzJ9.TiMuspVVz1CwnTbx2EKTgrTiTAWXUvFGtRUF6COCeWUp"
//    )
//    @GET("users/{username}")
//    open fun getUser(): Call<ResponseBody>

    @GET("/horse-race")
    suspend fun getAllHorseRace() : Response<HorseRaceResponse>

    @GET("/horse-news")
    suspend fun getAllHorseNews() : Response<List<HorseNews>>

    @GET
    suspend fun getGliveVideo(
        @Url url: String
    ) : Response<GliveResponse>

    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : MyApi{

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return  Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://vlog-threewe-apinodejs.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}