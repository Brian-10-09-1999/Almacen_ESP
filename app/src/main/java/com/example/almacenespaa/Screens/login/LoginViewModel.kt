package com.example.zitrocrm.screens.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacenespaa.Network.ServiceApi.RetrofitHelper
import com.example.zitrocrm.repository.SharedPrefence
import com.example.zitrocrm.screens.login.components.alert
import com.example.zitrocrm.screens.login.components.alertdialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel (
) : ViewModel() {
    val isSuccessLoading = mutableStateOf(value = false)
    val imageErrorAuth = mutableStateOf(value = false)
    val checkInicio = mutableStateOf(false)

    fun login(user: String, pwd: String, context: Context) {
        val dataStorePreferenceRepository = SharedPrefence(context)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                alertdialog(1,"¡Esperando respuesta del servidor!")
                delay(1200L)
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.LoginApi(user,pwd)
                dataStorePreferenceRepository.clearSharedPreference()
                if (responseService.isSuccessful) {
                    responseService.body().let {
                        if(it!!.usuID!!>0){
                            isSuccessLoading.value = true
                            Log.d("Logging", "LOGIN SUCCESS"+responseService.body().toString())
                            dataStorePreferenceRepository.saveinicio(checkInicio.value)
                            dataStorePreferenceRepository.saveUserInfo(user,pwd,responseService.body()!!)
                        }
                    }
                } else {
                    alertdialog(3,"")
                    delay(1500L)
                }
            } catch (e: Exception) {
                Log.d("Logging", "Error Authentication", e)
                alertdialog(3,"")
                delay(5000)
            }
            alertdialog(0,"")
        }
    }
}
