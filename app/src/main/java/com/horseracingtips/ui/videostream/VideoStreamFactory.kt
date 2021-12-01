package com.horseracingtips.ui.videostream

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horseracingtips.data.repository.HorseRaceRepository

class VideoStreamFactory (
    private val repository: HorseRaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VideoSteamViewModel(repository) as T
    }
}