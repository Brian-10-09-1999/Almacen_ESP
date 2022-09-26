package com.example.almacenespaa.Network.Models.SutirMaterial

import com.google.gson.annotations.SerializedName

data class SurtidoMaterial (

    @SerializedName("dato"      ) var dato      : String?            = null,
    @SerializedName("respuesta" ) var respuesta : String?            = null,
    @SerializedName("pedidos"   ) var pedidos   : ArrayList<Pedidos> = arrayListOf()

)
data class Pedidos (

    @SerializedName("solicitud_refaccionid" ) var solicitudRefaccionid : String? = null,
    @SerializedName("nombre"                ) var nombre               : String? = null,
    @SerializedName("fechaStock"            ) var fechaStock           : String? = null,
    @SerializedName("tipoPedido"            ) var tipoPedido           : String? = null,
    @SerializedName("departamentoidfk"      ) var departamentoidfk     : String? = null,
    @SerializedName("tecnico"               ) var tecnico               : String? = null,
    @SerializedName("fechaHora"             ) var fechaHora               : String? = null,

)