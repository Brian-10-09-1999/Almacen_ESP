package com.example.almacenespaa.Screens.surtir_material

import android.annotation.SuppressLint
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.almacenespaa.Network.Models.SampleData
import com.example.almacenespaa.Network.Models.SutirMaterial.Pedidos
import com.example.almacenespaa.R
import com.example.almacenespaa.ui.theme.*
import com.example.almacenespaa.utils.BarcodeAnalyser
import com.example.zitrocrm.repository.SharedPrefence
import com.example.zitrocrm.screens.login.components.alert
import com.example.zitrocrm.utils.Val_Constants
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PedidosMaterialScreen(
    navController: NavController,
    cameraM: CameraManager,
    pedidosMaterialViewModel : PedidosMaterialViewModel,
){
    Column() {
        Scaffold(
            topBar = {
                TopAppBarw(navController)
            }
        ) {
            RefreshContent(pedidosMaterialViewModel,cameraM)
        }
    }
}

@Composable
fun RefreshContent(
    pedidosMaterialViewModel : PedidosMaterialViewModel,
    cameraM: CameraManager,
) {
    val context = LocalContext.current
    val datastore = SharedPrefence(LocalContext.current)
    val subcentro = datastore.getSubCentro()
    val usuID = datastore.getUsuID()
    SwipeRefresh(
        state = rememberSwipeRefreshState(false),
        onRefresh = { pedidosMaterialViewModel.getListPedidos(n=1,subcentro = subcentro!!.toInt(), usuID = usuID,context) },
    ) {
        var espandcard by remember {
            mutableStateOf(true)
        }
        LazyColumn(
            state = rememberLazyListState(),
            contentPadding = PaddingValues(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                titleListaPedidos()
            }
            item {
                ContentQR(pedidosMaterialViewModel)
            }
            item {
                MaquinasInactivasCard(onCardArrowClick = { espandcard = !espandcard }, expanded = espandcard)
            }
            if(pedidosMaterialViewModel.a.value) {
                itemsIndexed(pedidosMaterialViewModel.pedidos) { index, item ->
                    PedidosTodos(item,pedidosMaterialViewModel,espandcard)
                }
            }
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun MaquinasInactivasCard(
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
) {
    val transitionState = remember { MutableTransitionState(expanded).apply {
        targetState = !expanded
    }}
    val transition = updateTransition(targetState = transitionState, label = "transition")
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = Val_Constants.ExpandAnimation)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }
    Card(
        backgroundColor = colorResource(R.color.blackdark),
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 4.dp
            )
    ) { Column(
        modifier = Modifier
            .fillMaxWidth()
        ) {
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        //.clickable { onCardArrowClick.invoke() }
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.15f)
                            .align(CenterVertically)){
                        Image(painter = painterResource(id = R.drawable.inventario),
                            contentDescription ="IconList", modifier = Modifier
                                .padding(start = 10.dp)
                                .size(30.dp))
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Pedidos a surtir",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.subtitle2,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 25.dp
                                )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.15f)
                            .align(Alignment.CenterVertically)
                    ) {
                        CardArrow(
                            degrees = arrowRotationDegree,
                            onClick = onCardArrowClick
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun PedidosTodos(
    item: Pedidos,
    viewModel: PedidosMaterialViewModel,
    espandcard : Boolean
) {
    val context = LocalContext.current
    AnimatedVisibility(
        visible = espandcard,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Row(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(blackdark)
            .fillMaxWidth()
            .clickable {
                if (alert.value == 0) {
                    viewModel.getPedido(
                        n=0,
                        pedido = item.solicitudRefaccionid.toString(),
                        context,
                        nombr = item.nombre.toString()
                    )
                }
            }
            .padding(5.dp)
        ) {
            Icon(
                Icons.Filled.MenuOpen, "",
                modifier = Modifier
                    .padding(10.dp)
            )
            Column() {
                Text(
                    text = item.nombre.toString(),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = "Fecha: "+item.fechaStock.toString(),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = "No. de pedido: "+item.solicitudRefaccionid.toString(),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = "Nombre Solicitante: "+item.tecnico.toString(),
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = "Fecha de Entrega: "+item.fechaHora.toString(),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}
val enterFadeIn = fadeIn(
    animationSpec = TweenSpec(
        durationMillis = Val_Constants.FadeInAnimation,
        easing = FastOutLinearInEasing
    )
)
val enterExpand = expandVertically(
    animationSpec = tween(
        Val_Constants.ExpandAnimation
    )
)
val exitFadeOut = fadeOut(
    animationSpec = TweenSpec(
        durationMillis = Val_Constants.FadeOutAnimation,
        easing = LinearOutSlowInEasing
    )
)
val exitCollapse =
    shrinkVertically(animationSpec = tween(Val_Constants.CollapseAnimation))

/***/
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun MaquinasPedidosCard(
    card2: SampleData,
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
    pedidosMaterialViewModel: PedidosMaterialViewModel,
    navController: NavController,
) {
    val  transitionState = remember { MutableTransitionState(expanded).apply {
        targetState = !expanded
    }}
    val transition = updateTransition(targetState = transitionState, label = "transition")
    val cardBgColor by transition.animateColor({
        tween(durationMillis = Val_Constants.ExpandAnimation)
    }, label = "bgColorTransition") {
        if (expanded) blackdark else blackdark
    }
    val cardElevation by transition.animateDp({
        tween(durationMillis = Val_Constants.ExpandAnimation)
    }, label = "elevationTransition") {
        if (expanded) 20.dp else 5.dp
    }
    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = Val_Constants.ExpandAnimation,
            easing = FastOutSlowInEasing
        )
    }, label = "cornersTransition") {
        15.dp
    }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = Val_Constants.ExpandAnimation)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }
    Card(
        backgroundColor = cardBgColor,
        elevation = cardElevation,
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 4.dp
            )
    ) { Column(
        modifier = Modifier
            .fillMaxWidth()
        ) {
            Box {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.15f)
                            .align(Alignment.CenterVertically)){
                        Image(painter = painterResource(id = R.drawable.inventario),
                            contentDescription ="IconList", modifier = Modifier
                                .padding(start = 10.dp)
                                .size(30.dp))
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = card2.title,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.subtitle2,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 25.dp
                                )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.15f)
                            .align(CenterVertically)
                    ) {
                        CardArrow(
                            degrees = arrowRotationDegree,
                            onClick = onCardArrowClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = "Expandable Arrow",
                modifier = Modifier
                    .rotate(degrees)
                    .size(30.dp),
            )
        }
    )
}
/****/

@Composable
private fun titleListaPedidos(
){
    Column(modifier = Modifier
        .padding(0.dp, 10.dp, 0.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .fillMaxWidth()
        .background(blackdark)
        .padding(5.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Image(
            painter = painterResource(R.drawable.icon_lista_pedidos),
            contentDescription = "",
            modifier = Modifier
                .padding(0.dp)
                .size(50.dp)
        )
        Text(
            text = "Lista de Pedidos",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}
@Composable
private fun TopAppBarw(
    navController : NavController,
){
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(70.dp),
        title = {

            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.back_button),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                        }
                        .align(Alignment.CenterStart)
                        .size(29.dp)
                )
                Image(
                    painter = painterResource(R.drawable.icon_almacen),
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(5.dp)
                        .padding(start = 0.dp, end = 15.dp)
                )
            }
        },
        backgroundColor = reds,
    )
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
private fun ContentQR(
    pedidosMaterialViewModel : PedidosMaterialViewModel,
){
    val datastore = SharedPrefence(LocalContext.current)
    val usuID = datastore.getUsuID()
    val context = LocalContext.current
    val campedido = remember { mutableStateOf(false)}
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(blackdark)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(5.dp),
            verticalAlignment = CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pedidos),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .padding(0.dp, 0.dp, 10.dp, 0.dp),
                contentScale = ContentScale.Fit
            )
            val Search = remember {
                mutableStateOf("")
            }
            OutlinedTextField(
                value = Search.value ,
                onValueChange = {
                    val qr_split = it.split("\n")
                    Search.value = it.toString() //qr_split[0]
                    /*if (Utils(context).getValidat(it)){
                        pedidosMaterialViewModel.getBusquedaPedido(usuID = usuID,pedido = Search.value, context = context)
                    }*/
                                },
                modifier = Modifier
                    .fillMaxWidth()
                    ,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(
                    onDone = {
                        //onclickSurtir(subcentro!!.toInt(),Search.value.toInt())
                        if(Search.value.isNotBlank()){
                            pedidosMaterialViewModel.getBusquedaPedido(usuID = usuID,pedido = Search.value, context = context)
                        }
                    }
                ),
                label = { Text("Ingresa pedido") },
                trailingIcon = {
                    IconButton(
                        onClick = { campedido.value = !campedido.value }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.QrCode2,
                            contentDescription = "Icon Search",
                            tint = Color.White,
                            modifier = Modifier
                                .align(CenterVertically)
                                .scale(scaleY = 0.7F, scaleX = .7F)
                        )
                    }
                }
            )
            IconButton(
                onClick = {campedido.value = !campedido.value }
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = "Icon Search",
                    tint = Color.White,
                    modifier = Modifier
                        .align(CenterVertically)
                        .scale(scaleY = 0.7F, scaleX = .7F)
                )
            }
        }
        AnimatedVisibility(
            visible = campedido.value,
            enter = enterExpand + enterFadeIn,
            exit = exitCollapse + exitFadeOut
        ) {
            Box(modifier = Modifier
                .height(252.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                val lifecycleOwner = LocalLifecycleOwner.current
                var preview by remember { mutableStateOf<Preview?>(null) }
                val mMediaPlayer = MediaPlayer.create(context, R.raw.bip)
                Column(modifier = Modifier
                    .fillMaxSize()) {
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .height(252.dp)
                        .padding(10.dp)) {
                        AndroidView(factory = { AndroidViewContext ->
                            PreviewView(AndroidViewContext).apply {
                                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                )
                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            }
                        },
                            modifier = Modifier.fillMaxWidth(),
                            update = { previewView ->
                                val cameraSelector: CameraSelector = CameraSelector.Builder()
                                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                    .build()
                                val cameraExecutors: ExecutorService = Executors.newSingleThreadExecutor()
                                val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                                    ProcessCameraProvider.getInstance(context)

                                cameraProviderFuture.addListener({
                                    preview = Preview.Builder().build().also {
                                        it.setSurfaceProvider(previewView.surfaceProvider)
                                    }
                                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                                    val barcodeAnalyser = BarcodeAnalyser { barcodes ->
                                        barcodes.forEach { barcode ->
                                            barcode.rawValue?.let { barcodeValue ->
                                                if(campedido.value) {
                                                    campedido.value = false
                                                    if (alert.value == 0) {
                                                        mMediaPlayer.start()
                                                        pedidosMaterialViewModel.getBusquedaPedido(
                                                            usuID = usuID,
                                                            pedido = barcodeValue,
                                                            context = context
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build()
                                        .also {
                                            it.setAnalyzer(cameraExecutors, barcodeAnalyser)
                                        }
                                    try {
                                        cameraProvider.unbindAll()
                                        cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            cameraSelector,
                                            preview,
                                            imageAnalysis,
                                        )
                                    } catch (e: Exception) {
                                        Log.d("", "CameraPreview: ${e.localizedMessage}")
                                    }
                                }, ContextCompat.getMainExecutor(context))
                            }
                        )
                        Image(
                            painter = painterResource(R.drawable.background_camera),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}