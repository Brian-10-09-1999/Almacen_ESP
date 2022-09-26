package com.example.almacenespaa.Network.Models.SutirMaterial

import com.google.gson.annotations.SerializedName

data class Similar(
    @SerializedName("piezas" ) var piezas : ArrayList<Pz> = arrayListOf()
)
data class Pz (

    @SerializedName("res" ) var res : String? = null

)