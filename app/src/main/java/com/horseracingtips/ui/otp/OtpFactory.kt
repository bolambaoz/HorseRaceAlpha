package com.horseracingtips.ui.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.horseracingtips.data.repository.AuthRepository

class OtpFactory(
    private val repository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OtpViewModel(repository) as T
    }
}