package com.example.almacenespaa.Network.Models

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("usuID"         ) var usuID         : Int?               = null,
    @SerializedName("usu"           ) var usu           : String?            = null,
    @SerializedName("dep"           ) var dep           : String?            = null,
    @SerializedName("subcentro"     ) var subcentro     : Int?               = null,
    @SerializedName("version"       ) var version       : String?            = null,
    @SerializedName("AppName"       ) var AppName       : String?            = null,
    @SerializedName("tipoAcceso"    ) var tipoAcceso    : String?            = null,
)
