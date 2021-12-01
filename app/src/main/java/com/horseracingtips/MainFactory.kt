package com.horseracingtips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horseracingtips.data.repository.HorseRaceRepository

class MainFactory (
    private val repository: HorseRaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}