package com.example.almacenespaa.Network.Models.Moviemientos

import com.google.gson.annotations.SerializedName

data class MoviemientoPost (

    @SerializedName("QRubicacion" ) var QRubicacion : String? = null,
    @SerializedName("resultado"   ) var resultado   : String? = null

)