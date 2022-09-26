package com.example.almacensisco.navigation

import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.almacenespaa.Screens.movimientos.MovimientosScreen
import com.example.almacenespaa.Screens.surtir_material.PedidosMaterialScreen
import com.example.almacenespaa.Screens.surtir_material.PedidosMaterialViewModel
import com.example.almacenespaa.Screens.surtir_material.SurtirPedidoScreen
import com.example.almacensisco.Screens.SplashScreen
import com.example.zitrocrm.screens.HomeScreen
import com.example.zitrocrm.screens.LoginScreen
import com.example.zitrocrm.screens.login.LoginViewModel
import com.example.zitrocrm.screens.login.components.ProgressBarLoading

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreen(
    cameraM: CameraManager
){
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val pedidosViewModel : PedidosMaterialViewModel = viewModel()
    val imageError = loginViewModel.imageErrorAuth.value
    NavHost(
        navController = navController,
        startDestination = Destination.getStartDestination()
    ) {
        composable(route = Destination.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Destination.LoginScreen.route) {
            if (loginViewModel.isSuccessLoading.value) {
                LaunchedEffect(key1 = Unit) {
                    navController.navigate(route = Destination.HomeScreen.route) {
                        popUpTo(route = Destination.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                }
            } else {
                LoginScreen(
                    onclickLogin = (loginViewModel::login),
                    imageError = imageError,
                    loginViewModel = loginViewModel
                )
            }
        }
        composable(route = Destination.HomeScreen.route){
            if (pedidosViewModel.ListaPedidos.value) {
                LaunchedEffect(key1 = Unit) {
                    navController.navigate(route = Destination.PedidosMaterialScreen.route) {
                        popUpTo(route = Destination.HomeScreen.route) {
                            inclusive = false
                        }
                    }
                }
                pedidosViewModel.ListaPedidos.value = false
            }
            HomeScreen(navController, onclickListaPedidos = (pedidosViewModel::getListPedidos), pedidosViewModel)
        }
        composable(route = Destination.PedidosMaterialScreen.route){
            if (pedidosViewModel.navSurtir.value) {
                LaunchedEffect(key1 = Unit) {
                    navController.navigate(route = Destination.SurtirPedidoScreen.route){
                        popUpTo(route = Destination.SurtirPedidoScreen.route) {
                            inclusive = false
                        }
                    }
                }
                pedidosViewModel.navSurtir.value = false
            }
            PedidosMaterialScreen(navController, cameraM = cameraM, pedidosViewModel)
        }
        composable(route = Destination.SurtirPedidoScreen.route) {
            if(pedidosViewModel.navTerminarSurtido.value){
                LaunchedEffect(key1 = Unit) {
                    navController.popBackStack()
                }
                pedidosViewModel.navTerminarSurtido.value = false
            }
            SurtirPedidoScreen(pedidosViewModel,navController = navController)
        }
        composable(route = Destination.MovimientosScreen.route){
            MovimientosScreen()
        }
    }
    ProgressBarLoading(pedidosViewModel)
}