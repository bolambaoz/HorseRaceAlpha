package com.horserace.ui.channels

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.horserace.data.repository.HorseRaceRepository
import com.horserace.utils.ApiException
import com.horserace.utils.Coroutines
import com.horserace.utils.NoInternetException
import com.horserace.utils.lazyDeferred

private val DEFAULT_GLIVE_LINK = "https://asia3we.com/"
val FROM_GLIVE = "glive"
class GalleryViewModel(
    private val repository: HorseRaceRepository
) : ViewModel() {

    var galleryListener: GalleryListener? = null
    var message: String? = null
    var isActive = repository.getIsActive

    val horseData by lazyDeferred {
        repository.getHorseData()
    }

    fun getGliveLink(channel: String, ip: String) : String{
        val baseURL = "https://3webasketball.com/ninety-six-group-api/public/latest/stream/${channel}/${ip}/geth5link"
        Coroutines.main {
            try {
                val gliveResponse = repository.getUrlGlive(baseURL)
                gliveResponse.H5LINKROW.let {
                    galleryListener?.onStarted(it.toString(), FROM_GLIVE)
                    return@main
                }

            }catch (e: ApiException){
                galleryListener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                galleryListener?.onFailure(e.message!!)
            }
        }

        return DEFAULT_GLIVE_LINK
    }

}