package com.example.zitrocrm.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almacenespaa.R
import com.example.almacenespaa.Screens.surtir_material.PedidosMaterialViewModel
import com.example.almacenespaa.ui.theme.blackdark
import com.example.almacenespaa.ui.theme.reds
import com.example.almacensisco.navigation.Destination
import com.example.zitrocrm.repository.SharedPrefence
import com.example.zitrocrm.screens.login.components.ProgressBarLoading


/** SIZE BUTTONS AND SIZE ICONS **/
val SizeButtons = 160.dp
val SizeIconsTittle = 40.dp
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    onclickListaPedidos: (n:Int,subcentro: Int, usuID: Int,context: Context) -> Unit,
    pedidosMaterialViewModel : PedidosMaterialViewModel
){
    Scaffold(
        topBar = {
            CustomTopAppBarHome()
        }
    ) {
        ContentHome(navController,onclickListaPedidos)
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
                Text(
                    text ="Bienvenido \n"+username,
                    modifier = Modifier.align(CenterStart),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start
                )
                Image(
                    painter = painterResource(R.drawable.icon_almacen),
                    contentDescription = "",
                    modifier = Modifier
                        .align(Center)
                        .padding(5.dp)
                        .padding(start = 0.dp, end = 15.dp)
                )
                val activity = (LocalContext.current as? Activity)
                Icon(
                    Icons.Filled.ExitToApp,
                    "contentDescription",
                    tint = Color.White,
                    modifier = Modifier
                        .align(CenterEnd)
                        .padding(10.dp)
                        .size(25.dp)
                        .clickable {
                            activity?.finish()
                            datastore.clearSharedPreference()
                        }

                )
            }
        },
        backgroundColor = reds,
    )
}
/** CONTENT PAGUE COMPLET HOME **/
@Composable
private fun ContentHome(
    navController: NavController,
    onclickLogin: (n:Int,subcentro: Int, usuID: Int,context:Context) -> Unit
){
    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(state = ScrollState(1))) {
        Column(modifier = Modifier.fillMaxSize()) {
            titleHome()
            subTitleHome()
            contentHome(navController,onclickLogin)
//            subTitleMovimientos()
//            contentMoviemientos(navController)
//            subTitleInventarios()
//            contentInventarios()
//            subTitleAvance()
//            contentAvance()
        }
    }
}
/** CHILD CONTENT PAGE **/
@Composable
private fun titleHome(/*viewModel: ViewModel = hiltViewModel()*/){
    Column(modifier = Modifier
        .padding(10.dp, 10.dp, 10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxHeight()
        .padding(5.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = CenterHorizontally)
    {

        Image(
            painter = painterResource(R.drawable.icon_surtir_pedido_transp),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .size(50.dp)
        )
        Text(
            text = "Almacen España",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(CenterHorizontally))
    }
}

@Composable
private fun subTitleHome(){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .padding(5.dp))
    {
        Icon(
            Icons.Filled.Task, "",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp)
        )
        Text(
            text = "Surtir Material",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(alignment = Alignment.Center))
    }
}

@Composable
private fun contentHome(
    navController: NavController,
    onclickLogin: (n:Int,subcentro: Int, usuID: Int,context:Context) -> Unit
){
    val context = LocalContext.current
    val datastore = SharedPrefence(LocalContext.current)
    val subcentro = datastore.getSubCentro()
    val usuID = datastore.getUsuID()
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .clickable {
            onclickLogin(0,subcentro!!.toInt(),usuID,context)
        }
        .padding(5.dp)
    )
    { Column(
            modifier = Modifier.align(Center).padding(5.dp)
    ){
        Image(
            painter = painterResource(R.drawable.icon_pedidos),
            contentDescription = "",modifier = Modifier
                .size(70.dp)
                .align(CenterHorizontally)
        )
        Text(
            text ="Lista de Pedidos",
            modifier = Modifier.align(CenterHorizontally).padding(vertical = 3.dp),
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.Start
        )
    }
    }
}

@Composable
private fun subTitleMovimientos(){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .padding(5.dp))
    {
        Icon(
            Icons.Filled.WrapText, "",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp)
        )
        Text(
            text = "Movimientos",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(alignment = Alignment.Center))
    }
}
@Composable
private fun contentMoviemientos(navController: NavController){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .clickable {
            navController.navigate(route = Destination.MovimientosScreen.route)
        }
        .padding(5.dp)
    )
    { Column(
        modifier = Modifier.align(Center).padding(5.dp)
    ){
        Image(
            painter = painterResource(R.drawable.movimientos_icon),
            contentDescription = "",modifier = Modifier
                .size(70.dp)
                .align(CenterHorizontally)
        )
        Text(
            text ="Movimientos de Inventario",
            modifier = Modifier.align(CenterHorizontally).padding(vertical = 3.dp),
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.Start
        )
    }
    }
}

@Composable
private fun subTitleInventarios(){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .padding(5.dp))
    {
        Icon(
            Icons.Filled.Assignment, "",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp)
        )
        Text(
            text = "Inventarios Online",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(alignment = Alignment.Center))
    }
}
@Composable
private fun contentInventarios(/*navController: NavController*/){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .clickable {
            //navController.navigate(route = Destination.MaquinasListScreen.route)
        }
        .padding(5.dp)
    )
    { Column(
        modifier = Modifier.align(Center).padding(5.dp)
    ){
        Image(
            painter = painterResource(R.drawable.inventario),
            contentDescription = "",modifier = Modifier
                .size(70.dp)
                .align(CenterHorizontally)
        )
        Text(
            text ="Inventario",
            modifier = Modifier.align(CenterHorizontally).padding(vertical = 3.dp),
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.Start
        )
    }
    }
}

@Composable
private fun subTitleAvance(){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .padding(5.dp))
    {
        Icon(
            Icons.Filled.Timelapse, "",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp)
        )
        Text(
            text = "Avance",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(alignment = Alignment.Center))
    }
}
@Composable
private fun contentAvance(/*navController: NavController*/){
    Box(modifier = Modifier
        .padding(10.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .clickable {
            //navController.navigate(route = Destination.MaquinasListScreen.route)
        }
        .padding(5.dp)
    )
    { Column(
        modifier = Modifier.align(Center).padding(5.dp)
    ){
        Image(
            painter = painterResource(R.drawable.avance_icon),
            contentDescription = "",modifier = Modifier
                .size(70.dp)
                .align(CenterHorizontally)
        )
        Text(
            text ="Ver el Avance",
            modifier = Modifier.align(CenterHorizontally).padding(vertical = 3.dp),
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.Start
        )
    }
    }
}