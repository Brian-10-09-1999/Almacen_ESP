package com.example.zitrocrm.utils

object Val_Constants {
    /**Url España**/
    const val BASE_URL = "http://10.110.76.16"
    /**BaseURL-Productivo Luis ZT<dn5-z**/
    //const val BASE_URL = "http://10.10.0.11"
    /**BaseURL-Pruebas**/
    //const val BASE_URL = "http://10.10.0.252:8082/"
    /***LOGIN*/
    const val API_LOGIN = "Android/validaUsuario.php"
    /**Lista Pedidos**/
    const val API_LISTA_PEDIDOS = "Android/listaPedidosInicial.php"
    /**Trae toda la informacion del pedido**/
    const val API_BUSCAR_PEDIDO = "Android/WebServicePrueba.php"
    /**Asigna componente similar al surtido**/
    const val API_SIMILAR_PIEZAS = "Android/similar.php"
    /**Realiza el surtido por piezas**/
    const val API_SURTIR_PZ = "Android/surtidoNuevo.php"
    /**Valida QR movimiento**/
    const val API_VALIDA_QR_MOV = "Android/ubicaciones/validaQRMov.php"
    /**Moviemiento**/
    const val API_MOVIMIENTO = "Android/ubicaciones/ingresaComUbicacion.php"


    const val ExpandAnimation = 300
    const val CollapseAnimation = 300
    const val FadeInAnimation = 300
    const val FadeOutAnimation = 300
}