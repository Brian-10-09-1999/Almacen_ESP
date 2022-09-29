package com.example.almacenespaa

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class Utils  (val context : Context?) {
    fun getValidationPedido(string: String): Boolean{
        val component = "[0-9 ]{1,30}"
        return component.toRegex().matches(string)
    }
    fun getValidationComponent(string: String): Boolean{
        val component = "[0-9#/-]{1,30}[\\s]{1}[0-9]{4,30}"
        return component.toRegex().matches(string)
    }
    fun getValidationComponent2(string: String): Boolean {
        val component = "[A-Za-z0-9 /-]{1,30}[-]{1}[A-Za-z0-9 /-]{1,30}[-]{1}[A-Za-z0-9 /-]{1,30}[\\s]{1}[A-Za-z0-9 /-]{1,30}"
        return component.toRegex().matches(string)
    }
    fun ValidationBolsa_Caja_Gaveta(string: String): Boolean {
        val component = "[A-Za-z0-9/-]{1,30}[|]{1}[A-Za-z0-9 /-]{1,30}"
        return component.toRegex().matches(string)
    }
    fun ValidationUbicacion(string: String): Boolean {
        val component = "[A-Za-z0-9/-]{1,30}[|]{1}[A-Za-z0-9 /-]{1,30}[|]{1}[A-Za-z0-9 /-]{1,30}"
        return component.toRegex().matches(string)
    }
    fun getValidat(string: String): Boolean{
        val component = "[A-Za-z0-9 |/-]{1,30}[\n]{1}"
        return component.toRegex().matches(string)
    }
    fun getValidationName(string: String): Boolean{
        val component = "[A-Z a-z0-9:._/\"-]{3,30}"
        return component.toRegex().matches(string)
    }
    fun getValidationRefaccion(string: String): Boolean{
        val component = "[A-Za-z0-9]{1,15}[-]{1}[A-Za-z0-9]{1,30}[-]{1}[A-Za-z0-9]{1,30}"
        return component.toRegex().matches(string)
    }
    fun getValidationRefaccion2(string: String): Boolean{
        val component = "[A-Za-z0-9]{1,15}[-]{1}[A-Za-z0-9]{1,30}"
        return component.toRegex().matches(string)
    }
}