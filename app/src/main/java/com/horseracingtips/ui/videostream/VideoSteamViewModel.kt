package com.horseracingtips.ui.videostream

import androidx.lifecycle.ViewModel
import com.horseracingtips.data.repository.HorseRaceRepository
import com.horseracingtips.ui.channels.*
import com.horseracingtips.utils.ApiException
import com.horseracingtips.utils.Coroutines
import com.horseracingtips.utils.NoInternetException

class VideoSteamViewModel(
    private val repository: HorseRaceRepository
) : ViewModel() {

    var vsListener: VSListener? = null
    var linkVideo = "1014"

    fun getGliveLink( ip: String){
        val baseURL = "$_GLIVE_LINK${linkVideo}/${ip}/$_GLIVE_FORMAT"
        Coroutines.main {
            try {
                val gliveResponse = repository.getUrlGlive(baseURL)
                gliveResponse.H5LINKROW.let {
                    vsListener?.onStarted(it.toString(), FROM_GLIVE)
                    return@main
                }

            }catch (e: ApiException){
                vsListener?.onFailure(e.message!!)
            }catch (e: NoInternetException){
                vsListener?.onFailure(e.message!!)
            }
        }
    }

    fun loadLink(){
        vsListener?.onStarted(linkVideo, FROM_GLIVE)
    }

}