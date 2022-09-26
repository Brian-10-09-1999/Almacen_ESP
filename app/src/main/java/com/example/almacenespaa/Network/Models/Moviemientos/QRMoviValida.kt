package com.example.almacenespaa.Network.Models.Moviemientos

import com.google.gson.annotations.SerializedName

data class QRMoviValida (

    @SerializedName("QRtipo"   ) var QRtipo   : String? = null,
    @SerializedName("isGranel" ) var isGranel : String? = null,
    @SerializedName("alerta"   ) var alerta   : String? = null,
    @SerializedName("datos"    ) var datos    : Datos?  = Datos()

)
data class Datos (

    @SerializedName("dato1" ) var dato1 : String? = null,
    @SerializedName("dato2" ) var dato2 : String? = null,
    @SerializedName("dato3" ) var dato3 : String? = null,
    @SerializedName("dato4" ) var dato4 : String? = null

)