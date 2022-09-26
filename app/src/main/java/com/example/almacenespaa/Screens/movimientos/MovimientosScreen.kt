package com.example.almacenespaa.Screens.movimientos

import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.almacenespaa.Network.Models.Moviemientos.BodegasScreen
import com.example.almacenespaa.Network.Models.Moviemientos.MovAjustesScreen
import com.example.almacenespaa.Network.Models.Moviemientos.MovScreen
import com.example.almacenespaa.R
import com.example.almacenespaa.Screens.surtir_material.*
import com.example.almacenespaa.ui.theme.blackdark
import com.example.almacenespaa.ui.theme.reds
import com.example.almacenespaa.utils.BarcodeAnalyser
import com.example.almacensisco.navigation.MovimientosNavItem
import com.example.zitrocrm.repository.SharedPrefence
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MovimientosScreen(){
    val navController = rememberNavController()
    Scaffold(
        topBar = { CustomTopAppBarHome() },
        bottomBar = { BottomNavigation(navController = navController) }
    ) {

        NavigationGraph(navController = navController)
    }
}
/** TOPBAR **/
@Composable
private fun CustomTopAppBarHome(
    //viewModel: ViewModel = hiltViewModel()
) {
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(70.dp),
        title = {
            val datastore = SharedPrefence(LocalContext.current)
            val username = ""+datastore.getUsu().toString()
            Box(modifier = Modifier.fillMaxSize()) {
                /*Text(
                    text ="Movimientos",
                    modifier = Modifier.align(Alignment.CenterEnd).padding(horizontal = 5.dp),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start
                )*/
                Image(
                    painter = painterResource(R.drawable.icon_almacen),
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(5.dp)
                        .padding(start = 0.dp, end = 15.dp)
                )
                Image(
                    painter = painterResource(R.drawable.back_button),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            //navController.navigate(route = Destination.PedidosMaterialScreen.route)
                        }
                        .align(Alignment.CenterStart)
                        .size(29.dp)
                )
            }
        },
        backgroundColor = reds,
    )
}
@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        MovimientosNavItem.Movimientos,
        MovimientosNavItem.Ajustes,
        MovimientosNavItem.Bodegas,
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = colorResource(id = R.color.reds),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 11.sp,
                    )
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = MovimientosNavItem.Movimientos.screen_route) {
        composable(MovimientosNavItem.Movimientos.screen_route) {
            MovScreen()
        }
        composable(MovimientosNavItem.Ajustes.screen_route) {
            MovAjustesScreen()
        }
        composable(MovimientosNavItem.Bodegas.screen_route) {
            BodegasScreen()
        }
    }
}



