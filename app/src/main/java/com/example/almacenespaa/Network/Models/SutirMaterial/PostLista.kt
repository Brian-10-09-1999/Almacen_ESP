package com.example.almacenespaa.Network.Models.SutirMaterial

import com.google.gson.annotations.SerializedName

data class PostLista (
    @SerializedName("subcentro") val subcentro  :Int,
    @SerializedName("usuID"    ) val usuID      :Int

        )


