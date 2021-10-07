package com.horseracingtips.ui.dashboard

import androidx.lifecycle.ViewModel
import com.horseracingtips.data.repository.HorseRaceRepository
import com.horseracingtips.utils.lazyDeferred

class HomeViewModel(
    private val repository: HorseRaceRepository
) : ViewModel() {


    val horseNewsData by lazyDeferred {
        repository.getHorseNewData()
    }
    var homeListener: HomeListener? = null

}