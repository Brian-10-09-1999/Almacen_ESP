package com.example.almacenespaa.Screens.movimientos.Movimiento

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacenespaa.Network.ServiceApi.RetrofitHelper
import com.example.zitrocrm.repository.SharedPrefence
import com.example.zitrocrm.screens.login.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MovimientosViewModel (
): ViewModel(){
    val codigox = mutableStateOf("")
    val QRtipo = mutableStateOf("")
    val isGranel = mutableStateOf("")
    val alerta = mutableStateOf("")
    val datos = mutableStateOf("")

    fun ValidaQrMovimiento (codigo:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                clear()
                alertdialog(1,"¡Esperando respuesta del servidor!")
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.ValidaQrMov(codigo)
                if(responseService.isSuccessful){
                    Log.d("Success", "Success ValidaQrMovimiento"+responseService.body().toString())
                    if(responseService.body()!!.alerta=="OK" ){
                        codigox.value = codigo
                        QRtipo.value = responseService.body()!!.QRtipo.toString()
                        isGranel.value  =  responseService.body()!!.isGranel.toString()
                        alerta.value = responseService.body()!!.alerta.toString()
                        datos.value =  responseService.body()!!.datos.toString()
                        alertdialog(2,"Ingresa el destino de la ${responseService.body()!!.QRtipo}")
                        delay(4000)
                        if (responseService.body()!!.isGranel=="1"){
                            alertdialog(2,"El producto es a granel: \n ${responseService.body()!!.isGranel}")
                            delay(4000)
                        }
                    }else{
                        alertdialog(2,"Comprueba tu informacion\nEl Qr ingresado no válido")
                        delay(4000)
                    }
                }
            }catch (e:Exception){
                Log.d("ValidaQrMovimiento", "Error ValidaQrMovimiento", e)
                alertdialog(3,"")
                delay(5000)
            }
            alertdialog(0,"")
        }
    }

    fun PostMovimiento(ubicacionx:String,context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val dataStorePreferenceRepository = SharedPrefence(context)
            try {
                alertdialog(1,"¡Esperando respuesta del servidor!")
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.EnvioMovimeinto(
                    codx = codigox.value,
                    ubicacionx = ubicacionx,
                    cantidadx= "",
                    usuarioIDx = dataStorePreferenceRepository.getUsuID().toString(),
                    granelx = isGranel.value
                )
                if(responseService.isSuccessful){
                    alertdialog(2,"${responseService.body()!!.QRubicacion}")
                    delay(4000)
                }
            }catch (e:Exception){
                Log.d("PostMovimiento", "Error PostMovimiento", e)
            }
            alertdialog(0,"")
        }
    }

    fun clear(){
        QRtipo.value = ""
        isGranel.value  = ""
        alerta.value = ""
        datos.value =  ""
        codigox.value = ""
    }
}