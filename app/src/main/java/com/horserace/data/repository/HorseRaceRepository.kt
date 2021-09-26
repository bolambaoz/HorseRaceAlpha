package com.horserace.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.horserace.data.db.AppDatabase
import com.horserace.data.db.entity.HorseNews
import com.horserace.data.db.entity.HorseVideo
import com.horserace.data.db.preferrences.PreferenceProvider
import com.horserace.data.network.MyApi
import com.horserace.data.network.SafeApiRequest
import com.horserace.data.network.response.GliveResponse
import com.horserace.data.network.response.HorseRaceResponse
import com.horserace.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private val MINIMUM_INTERVAL = 6
class HorseRaceRepository(
    private val api: MyApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest()  {

    private val horseData = MutableLiveData<List<HorseVideo>>()
    var horseDataIsLive = MutableLiveData<Boolean>()

    private val horseNewsData = MutableLiveData<List<HorseNews>>()
    var getIsActive = prefs.getIsActive()

    init {
        horseNewsData.observeForever{
            saveHorseNewsData(it)
        }
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
//        if(lastSavedAt == null || isFetchNeeded(LocalDateTime.parse(lastSavedAt))){
            val response = apiRequest { api.getAllHorseRace() }
            prefs.saveIsActive(response.isActive!!)
            horseDataIsLive.postValue(response.isActive!!)
            horseData.postValue(response.video!!)
//        }
    }

    //HORSE NEWS METHODS
    suspend fun getHorseNewData(): LiveData<List<HorseNews>> {
         return withContext(Dispatchers.IO){
            fetchAllHorseNews()
            db.getHorseHewsDao().getHorseNews()
        }
    }
    private suspend fun fetchAllHorseNews() {
        val response = apiRequest { api.getAllHorseNews() }
        horseNewsData.postValue(response)
    }

    private fun saveHorseNewsData(horseData: List<HorseNews>){
        Coroutines.io {
            db.getHorseHewsDao().savaAllHorseNews(horseData)
        }
    }

    private fun updateHorseNewsData(horseData: List<HorseNews>){
        Coroutines.io {
//            prefs.savelastSavedAt(LocalDateTime.now().toString())
//            db.getHorseHewsDao().updateUsers(horseData)//(horseData)
        }
    }

    suspend fun getUrlGlive(url: String) : GliveResponse {
        return apiRequest {
            api.getGliveVideo(url)
        }
    }

}