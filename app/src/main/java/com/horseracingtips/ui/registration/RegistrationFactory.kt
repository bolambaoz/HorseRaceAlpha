package com.horseracingtips.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horseracingtips.data.repository.AuthRepository

class RegistrationFactory(
    private val repository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegistrationViewModel(repository) as T
    }
}