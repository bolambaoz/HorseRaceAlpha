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
private val _GLIVE_LINK = "https://3webasketball.com/3we-glive-api/public/latest/stream/"
private val _GLIVE_FORMAT = "geth5link"

class GalleryViewModel(
    private val repository: HorseRaceRepository
) : ViewModel() {

    var galleryListener: GalleryListener? = null
    var isActive = repository.getIsActive

    val horseData by lazyDeferred {
        repository.getHorseData()
    }

    val horseIsActive by lazyDeferred {
        repository.horseDataIsLive
    }

    fun getGliveLink(channel: String, ip: String) : String{
        val baseURL = "${_GLIVE_LINK}${channel}/${ip}/${_GLIVE_FORMAT}"
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