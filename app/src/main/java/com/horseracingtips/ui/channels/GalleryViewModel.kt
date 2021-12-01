package com.horseracingtips.ui.channels

import androidx.lifecycle.ViewModel
import com.horseracingtips.data.repository.HorseRaceRepository
import com.horseracingtips.utils.ApiException
import com.horseracingtips.utils.Coroutines
import com.horseracingtips.utils.NoInternetException
import com.horseracingtips.utils.lazyDeferred

val DEFAULT_GLIVE_LINK = "https://asia3we.com/"
val FROM_GLIVE = "glive"
val _GLIVE_LINK = "https://3webasketball.com/3we-glive-api/public/latest/stream/"
val _GLIVE_FORMAT = "geth5link"//"getflvlink"//

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