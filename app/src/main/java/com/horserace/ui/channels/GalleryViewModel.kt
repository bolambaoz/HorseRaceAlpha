package com.horserace.ui.channels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.horserace.data.repository.HorseRaceRepository
import com.horserace.utils.ApiException
import com.horserace.utils.Coroutines
import com.horserace.utils.NoInternetException

class GalleryViewModel(
    private val repository: HorseRaceRepository
) : ViewModel() {

    var galleryListener: GalleryListener? = null
    var message: String? = null

    val horseData by lazy {
//        repository.getHorseData()
    }

    private val _text = MutableLiveData<String>().apply {
        value = message
    }
    val text: LiveData<String> = _text

}