package com.example.almacenespaa.Network.Models.SutirMaterial

import com.google.gson.annotations.SerializedName

data class Piezas (

    @SerializedName("ref"        ) var ref        : String? = null,
    @SerializedName("existencia" ) var existencia : Int?    = null,
    @SerializedName("surtido"    ) var surtido    : String? = null,
    @SerializedName("dat"        ) var dat        : String? = null,
    @SerializedName("ubicacion"  ) var ubicacion  : String? = null,
    @SerializedName("idFami"     ) var idFami     : String? = null

)