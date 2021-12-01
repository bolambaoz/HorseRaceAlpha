package com.horseracingtips

import androidx.lifecycle.ViewModel
import com.horseracingtips.data.repository.HorseRaceRepository

class MainViewModel (
    private val repository: HorseRaceRepository
) : ViewModel() {

    val currentLanguage = repository.getLanguageNow()

    fun changeLanguage(lang: String) {
        repository.setupLanguage(lang)
    }

}