package com.example.almacenespaa.Network.Models.SutirMaterial

import com.google.gson.annotations.SerializedName

data class SPedido (

    @SerializedName("piezas"      ) var piezas      : ArrayList<Piezas> = arrayListOf(),
    @SerializedName("comentarios" ) var comentarios : String?           = null

)