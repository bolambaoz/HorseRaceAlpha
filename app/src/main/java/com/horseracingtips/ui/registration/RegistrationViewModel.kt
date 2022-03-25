package com.horseracingtips.ui.registration

import androidx.lifecycle.ViewModel
import com.horseracingtips.data.network.response.RegisterResponse
import com.horseracingtips.data.repository.AuthRepository
import com.horseracingtips.utils.Coroutines

class RegistrationViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var registerListener: RegisterListener? = null

    fun validatePhone(phone: String){
       Coroutines.main {
           val response = repository.phoneValidate(phone)
           registerListener?.onStarted("Starting")

//           val response = RegisterResponse(
//               true,
//               "",
//               ""
//           )

           if (response.isSuccess != null){
               registerListener?.onSuccess(response)
           }else{
               registerListener?.onFailure("Not a valid number")
           }
       }
    }
}