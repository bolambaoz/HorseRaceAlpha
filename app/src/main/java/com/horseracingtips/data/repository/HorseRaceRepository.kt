package com.horseracingtips.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.horseracingtips.data.db.AppDatabase
import com.horseracingtips.data.db.entity.HorseNews
import com.horseracingtips.data.db.entity.HorseVideo
import com.horseracingtips.data.db.preferrences.PreferenceProvider
import com.horseracingtips.data.network.MyApi
import com.horseracingtips.data.network.SafeApiRequest
import com.horseracingtips.data.network.response.GliveResponse
import com.horseracingtips.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

//private val MINIMUM_INTERVAL = 6
class HorseRaceRepository(
    private val api: MyApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest()  {

    private val horseData = MutableLiveData<List<HorseVideo>>()
    var horseDataIsLive = MutableLiveData<Boolean>()

    private val horseNewsData = MutableLiveData<List<HorseNews>>()
    var getIsActive = prefs.getIsActive()
//    var prefsLanguage = prefs.getLanguage()

    private var _lang = MutableLiveData<String>()

    init {
        horseNewsData.observeForever{
            saveHorseNewsData(it)
        }
        horseData.observeForever{
            saveHorseData(it)
        }
    }

    fun setupLanguage(lang: String){
        prefs.saveLanguage(lang)
    }

    fun getLanguageNow() : LiveData<String> {
        _lang.postValue(prefs.getLanguage().toString())
        return  _lang
//        _lang.postValue("en")
//        return try {
//            prefs.getLanguage()
//        }catch (e : Exception) {
//            _lang
//        }
    }

//    private fun isFetchNeeded(savedAt: LocalDateTime): Boolean {
//        return ChronoUnit.HOURS.between(savedAt, LocalDateTime.now()) > MINIMUM_INTERVAL
//    }

    private fun saveHorseData(horseData: List<HorseVideo>){
        Coroutines.io {
//            prefs.savelastSavedAt(LocalDateTime.now().toString())
            db.getHorseDao().deleteAllChannels()
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
//        val lastSavedAt = prefs.getLastSavedAt()
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

//    private fun updateHorseNewsData(horseData: List<HorseNews>){
//        Coroutines.io {
////            prefs.savelastSavedAt(LocalDateTime.now().toString())
////            db.getHorseHewsDao().updateUsers(horseData)//(horseData)
//        }
//    }

    suspend fun getUrlGlive(url: String) : GliveResponse {
        return apiRequest {
            api.getGliveVideo(url)
        }
    }

}