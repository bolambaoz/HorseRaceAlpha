package com.horseracingtips.data.network

import com.horseracingtips.data.db.entity.HorseNews
import com.horseracingtips.data.network.response.*
import okhttp3.OkHttpClient
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

    @GET("horse-channels")
    suspend fun getAllHorseRace() : Response<HorseRaceResponse>

    @GET("horse-news")
    suspend fun getAllHorseNews() : Response<List<HorseNews>>

    @GET
    suspend fun getGliveVideo(
        @Url url: String
    ) : Response<GliveResponse>


    @GET("otp")
    suspend fun userRegister(
        @Query("phoneNumber") phone: String,
        @Query("channel") channel: String
    ) : Response<RegisterResponse>

    @GET("otp/verify")
    suspend fun userVerification(
        @Query("code") code: String,
        @Query("to") to: String
    ) : Response<VerifyResponse>

    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : MyApi{

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return  Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://sleepy-turing-6de1dd.netlify.app/.netlify/functions/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}