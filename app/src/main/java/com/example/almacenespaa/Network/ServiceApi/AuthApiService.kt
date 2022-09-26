package com.example.almacenespaa.Network.ServiceApi

import com.example.almacenespaa.Network.Models.Login
import com.example.almacenespaa.Network.Models.Moviemientos.MoviemientoPost
import com.example.almacenespaa.Network.Models.Moviemientos.QRMoviValida
import com.example.almacenespaa.Network.Models.SutirMaterial.*
import com.example.zitrocrm.utils.Val_Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {
    @GET(Val_Constants.API_LOGIN)
    suspend fun LoginApi(
        @Query("usuariox") user : String,
        @Query("passx") pwd : String) : Response<Login>

    @FormUrlEncoded
    @POST(Val_Constants.API_LISTA_PEDIDOS)
    suspend fun getListaPedidos (
        @Field("usuID") usuID :Int,
        @Field("subcentro") subcentro:Int) : SurtidoMaterial

    @FormUrlEncoded
    @POST(Val_Constants.API_LISTA_PEDIDOS)
    suspend fun getBucarPedido (
        @Field("usuID") usuID :Int,
        @Field("subcentro") subcentro:Int,
        @Field("solicitud_refaccionid") solicitud_refaccionid:String) : SurtidoMaterial

    @GET(Val_Constants.API_BUSCAR_PEDIDO)
    suspend fun getPedido (
        @Query("pedido") pedido :String ,
        @Query("subcentro") subcentro:Int) : Response<SPedido>

    @GET(Val_Constants.API_SIMILAR_PIEZAS)
    suspend fun getSimilarPz (
        @Query("datosx") datosx :String,
        @Query("similarx") similarx:String) : Response<Similar>

    @GET(Val_Constants.API_SURTIR_PZ)
    suspend fun postSurtirPz (
        @Query("codC") codC : String,
        @Query("ubicacionx") ubicacionx:String,
        @Query("solx") solx:String,
        @Query("canx") canx:String,
        @Query("usuarioidx") usuarioidx:String,
        @Query("codUbi") codUbi:String,
        @Query("noSerieEnviax") noSerieEnviax:String,
        @Query("necSeriex") necSeriex:String,
        @Query("subcentro") subcentro:String
    ) : Piezas2

    @GET(Val_Constants.API_VALIDA_QR_MOV)
    suspend fun ValidaQrMov (
        @Query("codx") codx : String
    ): Response<QRMoviValida>

    @GET(Val_Constants.API_MOVIMIENTO)
    suspend fun EnvioMovimeinto(
        @Query("codx") codx : String,
        @Query("ubicacionx") ubicacionx : String,
        @Query("cantidadx") cantidadx : String,
        @Query("usuarioIDx") usuarioIDx : String,
        @Query("usuarioIDx") granelx : String,
    ): Response<MoviemientoPost>
}