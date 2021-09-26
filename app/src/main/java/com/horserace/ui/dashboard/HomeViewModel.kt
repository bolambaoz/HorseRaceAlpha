package com.horserace.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.horserace.data.repository.HorseRaceRepository
import com.horserace.ui.channels.*
import com.horserace.utils.ApiException
import com.horserace.utils.Coroutines
import com.horserace.utils.NoInternetException
import com.horserace.utils.lazyDeferred

class HomeViewModel(
    private val repository: HorseRaceRepository
) : ViewModel() {


    val horseNewsData by lazyDeferred {
        repository.getHorseNewData()
    }
    var homeListener: HomeListener? = null

}