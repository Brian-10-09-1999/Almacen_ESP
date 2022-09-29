package com.example.almacenespaa.Screens.surtir_material

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacenespaa.Network.Models.ComponentFamily.RefaccionesItem
import com.example.almacenespaa.Network.Models.ComponentFamily.ResponseComponentFamily
import com.example.almacenespaa.Network.Models.SutirMaterial.Pedidos
import com.example.almacenespaa.Network.Models.SutirMaterial.Piezas
import com.example.almacenespaa.Network.ServiceApi.RetrofitHelper
import com.example.almacenespaa.Utils
import com.example.zitrocrm.repository.SharedPrefence
import com.example.zitrocrm.screens.login.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PedidosMaterialViewModel(
) : ViewModel() {
    val ListaPedidos = mutableStateOf(value = false)
    val navSurtir = mutableStateOf(value = false)
    var pedidos = listOf<Pedidos>()
    val pedidoSearch = mutableListOf<Pedidos>()
    var pedidoo = listOf<Piezas>()
    val comentarios = mutableStateOf("")
    val nombre = mutableStateOf("")
    val a = mutableStateOf(true)
    var components = mutableStateListOf<RefaccionesItem>()
    var componentNomCod = mutableStateListOf<RefaccionesItem>()

    fun getListPedidos(n: Int, subcentro: Int, usuID: Int, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                alertdialog(1, "¡Esperando respuesta del servidor!")
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getListaPedidos(
                    usuID = usuID,
                    subcentro = subcentro
                )
                if (responseService.pedidos.isNotEmpty()) {
                    pedidos = responseService.pedidos
                    if (n == 0) ListaPedidos.value = true
                    Log.d("ListPedidos", "Authentication" + pedidos)
                } else {
                    alertResponse("No hay resultados", context, false)
                }
            } catch (e: Exception) {
                Log.d("ListPedidos", "Error ListPedidos", e)
                alertdialog(3, "")
                delay(4000)
            }
            alertdialog(0, "")
            aa()
        }
    }

    fun getBusquedaPedido(usuID: Int, pedido: String, context: Context) {
        val dataStorePreferenceRepository = SharedPrefence(context)
        val subcentro = dataStorePreferenceRepository.getSubCentro()!!.toInt()
        viewModelScope.launch(Dispatchers.IO) {
            pedidoSearch.clear()
            try {
                alertdialog(1, "¡Esperando respuesta del servidor!")
                if (Utils(context).getValidationPedido(pedido)) {
                    val authService = RetrofitHelper.getAuthService()
                    val responseService = authService.getBucarPedido(usuID, subcentro, pedido)
                    if (responseService.pedidos.isNotEmpty()) {
                        responseService.pedidos.forEach {
                            nombre.value = it.nombre.toString()
                            solicitudNo.value = pedido
                            pedidoSearch.add(
                                Pedidos(
                                    it.solicitudRefaccionid,
                                    it.nombre,
                                    it.fechaStock,
                                    it.tipoPedido,
                                    it.departamentoidfk
                                )
                            )
                        }
                        getPedido(0, pedido, context, nombre.value)
                    } else {
                        alertResponse("No hay resultados", context, false)
                    }
                } else {
                    alertResponse("Qr escaneado incorrecto", context, false)
                }
            } catch (e: Exception) {
                Log.d("Logging", "Error Authentication", e)
                alertdialog(3, "")
                delay(4000)
            }
            alertdialog(0, "")
        }
    }

    fun getPedido(n: Int, pedido: String, context: Context, nombr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            solicitudNo.value = pedido
            nombre.value = nombr
            val dataStorePreferenceRepository = SharedPrefence(context)
            val subcentro = dataStorePreferenceRepository.getSubCentro()!!.toInt()
            try {
                alertdialog(1, "¡Esperando respuesta del servidor!")
                delay(2000)
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getPedido(
                    pedido = pedido,
                    subcentro = subcentro
                )
                if (responseService.isSuccessful) {
                    comentarios.value = responseService.body()!!.comentarios.toString()
                    pedidoo = responseService.body()!!.piezas
                    if (pedidoo.isNotEmpty()) {
                        if (n == 0) navSurtir.value = true
                    }
                }
                aa()
            } catch (e: Exception) {
                alertdialog(3, "")
                Log.d("Logging", "Error Authentication", e)
                delay(4000)
            }
            alertdialog(0, "")
        }
    }

    fun getPedido2(n: Int, pedido: String, context: Context, nombr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            solicitudNo.value = pedido
            nombre.value = nombr
            val dataStorePreferenceRepository = SharedPrefence(context)
            val subcentro = dataStorePreferenceRepository.getSubCentro()!!.toInt()
            try {
                alertdialog(1, "¡Esperando respuesta del servidor!")
                delay(2000)
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getPedido(
                    pedido = pedido,
                    subcentro = subcentro
                )
                if (responseService.isSuccessful) {
                    comentarios.value = responseService.body()!!.comentarios.toString()
                    pedidoo = responseService.body()!!.piezas
                    if (pedidoo.isNotEmpty()) {
                        if (n == 0) navSurtir.value = true
                    }
                }
            } catch (e: Exception) {
                alertdialog(3, "")
                Log.d("Logging", "Error Authentication", e)
                getPedido2(1, pedido, context, nombr)
                delay(4000)
            }
            alertdialog(0, "")
            aa()
        }
    }

    fun aa() {
        a.value = false
        a.value = true
    }

    val pzsurtir: MutableList<Int> = arrayListOf()

    val codigoComponente = mutableStateOf("")
    val ubicacion = mutableStateOf("")
    val solicitudNo = mutableStateOf("")
    val cantidadPz = mutableStateOf("")
    val qrUbicacion = mutableStateOf("")
    val noSerieEnviado = mutableStateOf("")
    val necSeriex = mutableStateOf("")

    fun clearGranel() {
        ubicacion.value = ""
        cantidadPz.value = ""
        qrUbicacion.value = ""
    }

    val navTerminarSurtido = mutableStateOf(false)

    fun postSurtir(
        context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val subcentro = SharedPrefence(context).getSubCentro()!!.toInt()
            val usuarioidx = SharedPrefence(context).getUsuID().toString()
            pzsurtir.clear()
            try {
                alertdialog(1, "¡Esperando respuesta del servidor!")
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.postSurtirPz(
                    codC = codigoComponente.value,
                    ubicacionx = ubicacion.value,
                    solx = solicitudNo.value,
                    canx = cantidadPz.value,
                    usuarioidx = usuarioidx,
                    codUbi = qrUbicacion.value,
                    noSerieEnviax = noSerieEnviado.value,
                    necSeriex = necSeriex.value,
                    subcentro = subcentro.toString()
                )
                if (responseService.toString() == "[]") {
                    alertdialog(2, "OCURRIO UN PROBLEMA CON LA RESPUESTA DEL SERVIDOR")
                    delay(4000)
                    alertdialog(0, "")
                }
                if (responseService.piezas.isNotEmpty()) {
                    if (responseService.piezas[0].ref!!.toString() == "Completo") {
                        alertdialog(2, "Se surtio correctamente los materiales")
                        ListaPedidos.value = false
                        navSurtir.value = false
                        clearGranel()
                        codigoComponente.value = ""
                        delay(4000)
                        getListPedidos(n = 1, subcentro = subcentro, usuarioidx.toInt(), context)
                        navTerminarSurtido.value = true
                        delay(1000)
                        navTerminarSurtido.value = false
                    } else {
                        alertdialog(2, "Se agrego correctamente")
                        delay(3000)
                        getPedido2(1, solicitudNo.value, context, nombre.value)
                        /*pedidoo.clear()
                        responseService.piezas.forEach {
                            pedidoo.add(Piezas(
                                ref = it.ref,
                                surtido = it.surtido,
                                dat = it.dat,
                                ubicacion = it.ubicacion
                            )
                            )
                        }*/
                        clearGranel()
                        alertdialog(0, "")
                    }
                    codigoComponente.value = ""
                }
                if (responseService.granel.isNotEmpty()) {
                    if (responseService.granel[0].granel2!!.isNotBlank()) {
                        delay(1000)
                        cantidadPz.value = ""
                        ubicacion.value = ""
                        for (i in 1..responseService.granel[0].granel2!!.split("-.-")[1].toInt()) {
                            pzsurtir.filter { it == i }.forEach {
                                pzsurtir.remove(i)
                            }
                            pzsurtir.add(i)
                        }
                        alertdialog(4, "")
                    }
                }
                if (responseService.piezas2.isNotEmpty()) {
                    if (responseService.piezas2[0].surtido == "no1") {
                        alertResponse("No existe", context, false)
                    } else if (responseService.piezas2[0].surtido == "no2") {
                        alertResponse("No pertenece a esta solicitud", context, false)
                    } else if (responseService.piezas2[0].surtido == "no3") {
                        alertResponse("Ya ha sido enviado anteriormente", context, false)
                    } else if (responseService.piezas2[0].surtido == "no4") {
                        alertResponse("Es incorrecto para esta solicitud", context, false)
                    } else if (responseService.piezas2[0].surtido == "no5") {
                        alertResponse(
                            "No se puede surtir porque el componente tiene asociaciones pendientes",
                            context,
                            false
                        )
                    } else if (responseService.piezas2[0].surtido == "no12") {
                        alertResponse("Cantidad a surtir incorrecta", context, false)
                    } else if (responseService.piezas2[0].surtido == "no15") {
                        alertResponse("Serie incorrecta", context, false)
                    } else if (responseService.piezas2[0].surtido == "no16") {
                        //alertResponse("Kit incompleto",context,false)
                        alertResponse("No se puede surtir", context, false)
                    } else if (responseService.piezas2[0].surtido == "no17") {
                        alertResponse("El QR no existe", context, false)
                    } else if (responseService.piezas2[0].surtido == "conSerie") {
                        alertResponse("Ok", context, true)
                    } else {
                        alertResponse("${responseService.piezas2[0].surtido}", context, false)
                    }
                    alertdialog(0, "")
                    codigoComponente.value = ""
                }
                aa()
                delay(1000)
            } catch (e: Exception) {
                alertdialog(3, "")
                Log.d("postSurtir", "Error postSurtir", e)
                delay(3000)
                alertdialog(0, "")
            }
        }
    }

    fun alertResponse(string: String, context: Context, ok: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            alertdialog(2, string)
            delay(4000)
            alertdialog(0, "")
            if (ok) getPedido2(1, solicitudNo.value, context, nombre.value)
            clearGranel()
        }
    }

    fun getComponenttFamily(item: Piezas, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                components.clear()
                alertdialog(1, "¡Esperando respuesta del servidor!")
                val authApiService = RetrofitHelper.getAuthService()
                val responseService = authApiService.getComponentFamily(item.idFami!!)
                if (responseService.code() == 200) {
                    Log.d("Componetes", responseService.body().toString())
                    components += responseService.body()!!.refacciones!!
                }
            } catch (e: Exception) {
                alertdialog(3, "")
                Log.d("Logging", "Error Authentication", e)
                delay(4000)
            }
            alertdialog(0, "")
        }
    }

    fun searchDataParence(context: Context, string: String) {
        componentNomCod.clear()
        if (string.count() >= 3) {
            componentNomCod +=
                components.filter { it.codigo!!.uppercase().contains(string.uppercase()) }
                    .asReversed()
            componentNomCod +=
                components.filter { it.nombre!!.uppercase().contains(string.uppercase()) }
                    .asReversed()
        }
    }

    fun pzSimilar(datos: String, similar: String, context: Context) {
        val dataStorePreferenceRepository = SharedPrefence(context)
        val subcentro = dataStorePreferenceRepository.getSubCentro()!!.toInt()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                alertdialog(1, "¡Esperando respuesta del servidor!")
                val authService = RetrofitHelper.getAuthService()
                val responseService = authService.getSimilarPz(datosx = datos, similarx = similar)
                if (responseService.isSuccessful) {
                    responseService.body()!!.piezas.forEach {
                        if (it.res!!.toInt() == 1) {
                            alertResponse(
                                "Componente similar asignado correctamente.",
                                context,
                                true
                            )
                        } else {
                            alertResponse(
                                "Componente no compatible.\nPor favor Ingresa un componente válido.",
                                context,
                                false
                            )

                        }
                    }
                }
                aa()
            } catch (e: Exception) {
                alertdialog(3, "")
                Log.d("Logging", "Error Authentication", e)
                delay(4000)
            }
            alertdialog(0, "")
        }
    }

    fun typeSurtido(qrValue: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (Utils(context).getValidationComponent(qrValue) || Utils(context).getValidationComponent2(
                        qrValue
                    )
                ) {
                    codigoComponente.value = qrValue
                    alertResponse("Enviando codigo a surtir: ${qrValue} ", context, false)
                    delay(2000)
                    postSurtir(context = context)
                } else if (Utils(context).ValidationBolsa_Caja_Gaveta(qrValue)) {
                    val qr_split = qrValue.split("|")
                    if (qrValue == qr_split[0] + "|Caja") {
                        alertResponse("QR de caja" + qrValue, context, false)
                        codigoComponente.value = ""
                        qrUbicacion.value = qrValue
                        postSurtir(context = context)
                    } else if (qrValue == qr_split[0] + "|Gaveta") {
                        alertResponse("QR de gaveta" + qrValue, context, false)
                    } else if (qrValue == qr_split[0] + "|Bolsa") {
                        alertResponse("QR de bolsa" + qrValue, context, false)
                    }
                } else if (Utils(context).ValidationUbicacion(qrValue)) {
                    alertResponse("QR de ubicacion :" + qrValue, context, false)
                } else {
                    alertResponse("QR válido cadena:" + qrValue, context, false)
                }
                aa()
            } catch (e: Exception) {

            }
        }
    }
}