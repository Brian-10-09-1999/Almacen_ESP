package com.example.almacenespaa.Network.Models.ComponentFamily

import com.google.gson.annotations.SerializedName

data class ResponseComponentFamily(

	@SerializedName("refacciones")
	val refacciones: ArrayList<RefaccionesItem>? = arrayListOf()
)

data class RefaccionesItem(

	@SerializedName("codigo") val codigo: String? = null,
	@SerializedName("nombre") val nombre: String? = null
)
