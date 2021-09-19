package com.horserace.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.horserace.data.db.AppDatabase
import com.horserace.data.db.entity.HorseRace
import com.horserace.data.db.entity.HorseVideo
import com.horserace.data.db.entity.User
import com.horserace.data.network.MyApi
import com.horserace.data.network.SafeApiRequest
import com.horserace.data.network.response.AuthResponse
import com.horserace.data.network.response.HorseRaceResponse
import com.horserace.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class HorseRaceRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest()  {

    private val horseData = MutableLiveData<List<HorseVideo>>()

    init {
        horseData.observeForever{
            saveHorseData(it)
        }
    }

    private fun saveHorseData(horseData: List<HorseVideo>){
        Coroutines.io {
            db.getHorseDao().savaAllHorseVideo(horseData)
        }
    }

    suspend fun getHorseData(): LiveData<List<HorseVideo>>{
        return withContext(Dispatchers.IO){
            fetchAllHorseData()
            db.getHorseDao().getHorse()
        }
    }

    private suspend fun fetchAllHorseData() {
        if(isFetchNeeded()){
            val response = apiRequest { api.getAllHorseRace() }
            horseData.postValue(response.video!!)
        }
    }

    private fun isFetchNeeded() : Boolean {
        return true
    }

    suspend fun getAllHorseRace() : HorseRaceResponse {
        return apiRequest {
            api.getAllHorseRace()
        }
    }

   // suspend fun saveHorseRace(horseVideo: List<HorseVideo>) = db.getHorseDao().upsert(horseVideo)//.getUserDao().upsert(user)

   // fun getHorseRace() = db.getHorseDao().getHorse()//.getUserDao().getUser()


}