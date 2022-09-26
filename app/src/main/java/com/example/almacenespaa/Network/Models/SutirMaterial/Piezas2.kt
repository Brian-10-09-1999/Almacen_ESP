package com.example.almacenespaa.Network.Models.SutirMaterial

import com.google.gson.annotations.SerializedName

data class Piezas2 (
    @SerializedName("piezas2" ) var piezas2 : ArrayList<Surtido> = arrayListOf(),
    @SerializedName("piezas"  ) var piezas  : ArrayList<Piezas>     = arrayListOf(),
    @SerializedName("granel"  ) var granel  : ArrayList<Granel> = arrayListOf(),
    @SerializedName("proceso" ) var proceso : String?           = null
)
data class Surtido (
    @SerializedName("surtido" ) var surtido : String? = null
)
data class Granel (
    @SerializedName("granel2" ) var granel2 : String? = null
)
