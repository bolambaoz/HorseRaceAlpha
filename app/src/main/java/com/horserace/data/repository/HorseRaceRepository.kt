package com.horserace.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.horserace.data.db.AppDatabase
import com.horserace.data.db.entity.HorseVideo
import com.horserace.data.db.preferrences.PreferenceProvider
import com.horserace.data.network.MyApi
import com.horserace.data.network.SafeApiRequest
import com.horserace.data.network.response.GliveResponse
import com.horserace.data.network.response.HorseRaceResponse
import com.horserace.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private val MINIMUM_INTERVAL = 6
class HorseRaceRepository(
    private val api: MyApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest()  {

    private val horseData = MutableLiveData<List<HorseVideo>>()
    val getIsActive = prefs.getIsActive()

    init {
        horseData.observeForever{
            saveHorseData(it)
        }
    }

    private fun isFetchNeeded(savedAt: LocalDateTime): Boolean {
        return ChronoUnit.HOURS.between(savedAt, LocalDateTime.now()) > MINIMUM_INTERVAL
    }

    private fun saveHorseData(horseData: List<HorseVideo>){
        Coroutines.io {
            prefs.savelastSavedAt(LocalDateTime.now().toString())

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
        val lastSavedAt = prefs.getLastSavedAt()

        if(lastSavedAt == null || isFetchNeeded(LocalDateTime.parse(lastSavedAt))){
            val response = apiRequest { api.getAllHorseRace() }
            prefs.saveIsActive(response.isActive!!)
            horseData.postValue(response.video!!)
        }
    }

    suspend fun getUrlGlive(url: String) : GliveResponse {
        return apiRequest {
            api.getGliveVideo(url)
        }
    }

}