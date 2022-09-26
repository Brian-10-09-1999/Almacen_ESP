package com.example.almacensisco.navigation

import com.example.almacenespaa.R

sealed class Destination(val route:String){
    object SplashScreen : Destination(route = "splash_screen")
    object LoginScreen : Destination(route = "login_screen")
    object HomeScreen : Destination(route = "home_screen")
    object PedidosMaterialScreen : Destination(route = "pedidos_material_screen")
    object SurtirPedidoScreen : Destination(route = "surtir_pedido_screen")
    object MovimientosScreen : Destination(route = "moviemientos_screen")

    companion object {
        fun getStartDestination() = SplashScreen.route
    }
}

sealed class MovimientosNavItem(var title:String, var icon:Int, var screen_route:String){

    object Movimientos : MovimientosNavItem("Movimientos", R.drawable.circular__1_,"home")
    object Ajustes: MovimientosNavItem("Ajustes", R.drawable.configuraciones,"my_network")
    object Bodegas: MovimientosNavItem("Bodegas", R.drawable.carretilla,"add_post")

}
