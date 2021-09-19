package com.horserace.ui.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horserace.data.repository.HorseRaceRepository

class GalleryViewFactory(
    private val repository: HorseRaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(repository) as T
    }
}