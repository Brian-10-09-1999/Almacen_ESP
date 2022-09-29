package com.example.almacenespaa.Screens.surtir_material

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.almacenespaa.Network.Models.SutirMaterial.Pedidos
import com.example.almacenespaa.Network.Models.SutirMaterial.Piezas
import com.example.almacenespaa.R
import com.example.almacenespaa.Utils
import com.example.almacenespaa.ui.theme.blackdark
import com.example.almacenespaa.ui.theme.blacktransp
import com.example.almacenespaa.ui.theme.reds
import com.example.almacenespaa.utils.BarcodeAnalyser
import com.example.zitrocrm.repository.SharedPrefence
import com.example.zitrocrm.screens.login.components.alert
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

@SuppressLint("UnusedTransitionTargetStateParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SurtirPedidoScreen(
    pedidosMaterialViewModel: PedidosMaterialViewModel,
    navController: NavController,
) {

    Scaffold(
        topBar = {
            TopAppSurtir(navController, pedidosMaterialViewModel)
        }
    ) {
        if (true/*pedidosMaterialViewModel.a.value*/) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                LazyColumn() {
                    item {
                        TitlePedido(pedidosMaterialViewModel)
                    }
                    item {
                        ContentQRSurtir(pedidosMaterialViewModel)
                        Spacer(modifier = Modifier.size(5.dp))
                    }
                    itemsIndexed(pedidosMaterialViewModel.pedidoo) { index, item ->
                        PedidoCard(item, pedidosMaterialViewModel)
                    }
                }
            }
        }
        AlertPedidos(pedidosMaterialViewModel)
    }
}

@Composable
private fun TopAppSurtir(
    navController: NavController,
    pedidosMaterialViewModel: PedidosMaterialViewModel
) {
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
                            pedidosMaterialViewModel.pedidoSearch.clear()
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
                Column(Modifier.align(CenterEnd)) {
                    IconButton(
                        onClick = {
                            alertComentarios.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Comment,
                            contentDescription = "Comment"
                        )
                    }
                }

            }
        },
        backgroundColor = reds,
    )
}

@Composable
private fun TitlePedido(
    pedidosMaterialViewModel: PedidosMaterialViewModel
) {
    Column(
        modifier = Modifier
            .padding(0.dp, 10.dp, 0.dp, 10.dp)
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
            text = "N." + pedidosMaterialViewModel.solicitudNo.value + " - " + pedidosMaterialViewModel.nombre.value,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

    }
}

@Composable
private fun ContentQRSurtir(
    pedidosMaterialViewModel: PedidosMaterialViewModel,
) {
    val context = LocalContext.current
    val openCamera = remember { mutableStateOf(false) }
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
            OutlinedTextField(
                value = pedidosMaterialViewModel.codigoComponente.value,
                onValueChange = { pedidosMaterialViewModel.codigoComponente.value = it },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        pedidosMaterialViewModel.postSurtir(context = context)
                    }
                ),
                label = { Text("Ingresa código de componente a surtir") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            openCamera.value = !openCamera.value
                        }
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
        }
        AnimatedVisibility(
            visible = openCamera.value,
            enter = enterExpand + enterFadeIn,
            exit = exitCollapse + exitFadeOut
        ) {
            Box(
                modifier = Modifier
                    .height(252.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                var preview by remember { mutableStateOf<Preview?>(null) }
                val mMediaPlayer = MediaPlayer.create(context, R.raw.bip)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(252.dp)
                            .padding(10.dp)
                    ) {
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
                                val cameraExecutors: ExecutorService =
                                    Executors.newSingleThreadExecutor()
                                val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                                    ProcessCameraProvider.getInstance(context)

                                cameraProviderFuture.addListener({
                                    preview = Preview.Builder().build().also {
                                        it.setSurfaceProvider(previewView.surfaceProvider)
                                    }
                                    val cameraProvider: ProcessCameraProvider =
                                        cameraProviderFuture.get()
                                    val barcodeAnalyser = BarcodeAnalyser { barcodes ->
                                        barcodes.forEach { barcode ->
                                            barcode.rawValue?.let { barcodeValue ->
                                                if (openCamera.value) {
                                                    openCamera.value = false
                                                    if (alert.value == 0) {
                                                        mMediaPlayer.start()
                                                        val qr_split = barcodeValue.split("\n")
                                                        if (Utils(context).getValidat(barcodeValue)) {
                                                            pedidosMaterialViewModel.postSurtir(
                                                                context = context
                                                            )
                                                            pedidosMaterialViewModel.codigoComponente.value =
                                                                qr_split[0]
                                                        }
                                                        pedidosMaterialViewModel.typeSurtido(
                                                            barcodeValue,
                                                            context
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
                                            imageAnalysis
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

@Composable
fun PedidoCard(
    item: Piezas,
    pedidosMaterialViewModel: PedidosMaterialViewModel
) {
    var espandcard by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    Row(modifier = Modifier
        .padding(0.dp, 5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(blackdark)
        .fillMaxWidth()
        .clickable {
            espandcard = !espandcard

        }
        .padding(5.dp)
    ) {
        Icon(
            Icons.Filled.Description, "",
            modifier = Modifier
                .padding(10.dp)
                .align(CenterVertically)
        )
        var xpanded = rememberSaveable() {
            mutableStateOf(false)
        }
        Column {
            Text(
                text = item.ref!!,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = "Existencia: " + item.existencia,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = "Ubicacion: " + item.ubicacion,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = "Piezas surtidas: " + item.surtido,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
            AnimatedVisibility(visible = espandcard) {

                Column {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp))
                            .background(colorResource(R.color.graydark))
                            .align(Start)
                            .clickable {
                                xpanded.value = true
                                pedidosMaterialViewModel.getComponenttFamily(item, context)
                                pedidosMaterialViewModel.componentNomCod.clear()
                            },
                    ) {
                        Icon(
                            Icons.Filled.LibraryAdd, contentDescription = "add",
                            Modifier
                                .align(
                                    CenterVertically
                                )
                                .padding(start = 10.dp)
                        )
                        Text(
                            text = "Surtir pieza similar",
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.padding(
                                vertical = 8.dp,
                                horizontal = 10.dp
                            ),
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.size(5.dp))

                }
            }
        }
        if (xpanded.value) {
            ShowCatalogoSimilares(
                context,
                xpanded,
                pedidosMaterialViewModel,
                item
            )
        }
    }
}

@Composable
fun ShowCatalogoSimilares(
    context: Context,
    xpanded: MutableState<Boolean>,
    pedidosMaterialViewModel: PedidosMaterialViewModel,
    item: Piezas
) {
    val openCamera = remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxHeight()
            .background(blackdark, RoundedCornerShape(10.dp))
            .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
    ) {
        AlertDialog(
            onDismissRequest = {
               // xpanded.value = false
            },
            buttons = {
                val descrip = rememberSaveable { mutableStateOf("") }
                val similarx = remember { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(reds)
                ) {
                    //--------------------------------------------------------------------
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { xpanded.value = false },
                            modifier = Modifier.align(End)
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "",
                                modifier = Modifier.align(End)
                            )
                        }
                        Text(text = "No. ${pedidosMaterialViewModel.solicitudNo.value}",
                            modifier = Modifier.align(CenterHorizontally))
                        
                        Text(text = "Nombre: ${pedidosMaterialViewModel.nombre.value}",
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.size(5.dp))
                    }
                }
                //------------------------------------------------------------------------------
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // -----------------------------------------------------------------------------------

                    OutlinedTextField(
                        value = similarx.value,
                        onValueChange = {
                            similarx.value = it
                            pedidosMaterialViewModel.searchDataParence(context,similarx.value)
                            descrip.value = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp, horizontal = 10.dp),
                        label = { Text(text = "Ingresar código/nombre similar") },
                        placeholder = { Text(text = "Ingresar código/nombre similar") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(10),
                        trailingIcon = {
                            IconButton(onClick = {
                                openCamera.value = !openCamera.value
                            }) {
                                Icon(
                                    Icons.Filled.QrCode2,
                                    contentDescription = "Visibility",
                                )
                            }
                        }
                    )
                    AnimatedVisibility(
                        visible = openCamera.value,
                        enter = enterExpand + enterFadeIn,
                        exit = exitCollapse + exitFadeOut
                    ) {
                        Box(
                            modifier = Modifier
                                .height(252.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            val context = LocalContext.current
                            val lifecycleOwner = LocalLifecycleOwner.current
                            var preview by remember { mutableStateOf<Preview?>(null) }
                            val mMediaPlayer = MediaPlayer.create(context, R.raw.bip)
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(252.dp)
                                        .padding(10.dp)
                                ) {
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
                                            val cameraExecutors: ExecutorService =
                                                Executors.newSingleThreadExecutor()
                                            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                                                ProcessCameraProvider.getInstance(context)

                                            cameraProviderFuture.addListener({
                                                preview = Preview.Builder().build().also {
                                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                                }
                                                val cameraProvider: ProcessCameraProvider =
                                                    cameraProviderFuture.get()
                                                val barcodeAnalyser = BarcodeAnalyser { barcodes ->
                                                    barcodes.forEach { barcode ->
                                                        barcode.rawValue?.let { barcodeValue ->
                                                            if (openCamera.value) {
                                                                openCamera.value = false
                                                                val qr_split = barcodeValue.split(" ")
                                                                similarx.value= qr_split[0]
                                                                pedidosMaterialViewModel.searchDataParence(context,similarx.value)
                                                                descrip.value = ""
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
                                                        imageAnalysis
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
                    OutlinedTextField(
                        enabled = false,
                        value = descrip.value,
                        onValueChange = {
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Ascii,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp, horizontal = 10.dp),
                        label = { Text("Descripción del material") },
                        trailingIcon = {
                            Icon(Icons.Filled.Article, "contentDescription")
                        }
                    )
                    // ----------------------------------------------------------------------------------
                    
                    LazyColumn(
                        modifier = Modifier
                            //.padding(top = 10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(blacktransp)
                            .height(200.dp)
                    ){
                        itemsIndexed(pedidosMaterialViewModel.componentNomCod){
                            index, item ->
                            Card(
                                modifier =
                                Modifier
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        similarx.value = item.codigo.toString()
                                        descrip.value = item.nombre.toString()
                                        pedidosMaterialViewModel.componentNomCod.clear()
                                    }

                            ) {
                                Text(
                                    text = "Código: " + item.codigo.toString() + "\nDescripción: " + item.nombre.toString(),
                                    style = MaterialTheme.typography.subtitle2,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(horizontal = 10.dp, vertical = 15.dp)
                                )

                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    val isValidate by derivedStateOf {
                        similarx.value.isNotBlank() && descrip.value.isNotBlank()
                    }
                    Button(
                        enabled = isValidate,
                        onClick = {
                            pedidosMaterialViewModel.pzSimilar(
                                    datos = item.dat!!,
                                    similar = similarx.value,
                                    context = context
                                )
                            xpanded.value = false
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .height(60.dp),
                        elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.reds)
                        )
                    ) {
                        Row() {
                            Text(
                                text = "Enviar",
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 10.dp)
                            )
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Enviar",
                                tint = Color.White
                            )
                        }

                    }
                    Spacer(Modifier.height(10.dp))
                }

            }
        )
    }
}




