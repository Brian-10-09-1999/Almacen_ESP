package com.example.almacenespaa.Network.Models.Moviemientos

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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.almacenespaa.R
import com.example.almacenespaa.Screens.movimientos.Movimiento.MovimientosViewModel
import com.example.almacenespaa.Screens.surtir_material.enterExpand
import com.example.almacenespaa.Screens.surtir_material.enterFadeIn
import com.example.almacenespaa.Screens.surtir_material.exitCollapse
import com.example.almacenespaa.Screens.surtir_material.exitFadeOut
import com.example.almacenespaa.ui.theme.blackdark
import com.example.almacenespaa.utils.BarcodeAnalyser
import com.example.zitrocrm.repository.SharedPrefence
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MovScreen() {
    val movimientosViewModel : MovimientosViewModel = viewModel()
    var campedido = remember { mutableStateOf(false) }
    var campedido2 = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            //.wrapContentSize(Alignment.Center)
            .padding(horizontal = 10.dp)
    ) {

        titleListaPedidos()
        Spacer(modifier = Modifier.size(5.dp))
        ContentQR(campedido,campedido2)
        Spacer(modifier = Modifier.size(5.dp))
        if(movimientosViewModel.alerta.value=="OK"){
            ContentQR2(campedido,campedido2)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class)
@Composable
private fun ContentQR(
    campedido: MutableState<Boolean>,
    campedido2: MutableState<Boolean>
) {
    val movimientosViewModel : MovimientosViewModel = viewModel()
    val datastore = SharedPrefence(LocalContext.current)
    val subcentro = datastore.getSubCentro()
    val usuID = datastore.getUsuID()
    val context = LocalContext.current
    val Search = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(blackdark)
    ) {
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = "Ingresa el material a mover.",
            style = MaterialTheme.typography.subtitle2,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
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
                value = Search.value ,
                onValueChange = {Search.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                ,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if(Search.value.isNotBlank()){
                            movimientosViewModel.ValidaQrMovimiento(Search.value)
                        }
                    }
                ),
                label = { Text("Caja, Gaveta, Código Almacen o QR unico.") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            campedido2.value = false
                            campedido.value = !campedido.value
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.QrCode2,
                            contentDescription = "Icon Search",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .scale(scaleY = 0.7F, scaleX = .7F)
                        )
                    }
                }
            )
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
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                var preview by remember { mutableStateOf<Preview?>(null) }

                val datastore = SharedPrefence(LocalContext.current)
                val subcentro = datastore.getSubCentro()
                val usuID = datastore.getUsuID()
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
                                                if(campedido.value){
                                                    mMediaPlayer.start()
                                                    Search.value = barcodeValue
                                                    campedido.value = false
                                                    movimientosViewModel.ValidaQrMovimiento(Search.value)
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
@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class)
@Composable
private fun ContentQR2(
    campedido: MutableState<Boolean>,
    campedido2: MutableState<Boolean>
){
    val movimientosViewModel : MovimientosViewModel = viewModel()
    val datastore = SharedPrefence(LocalContext.current)
    val subcentro = datastore.getSubCentro()
    val usuID = datastore.getUsuID()
    val context = LocalContext.current
    val Search = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(blackdark)
    ) {
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = "Ingresa su destino.",
            style = MaterialTheme.typography.subtitle2,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
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
                value = Search.value ,
                onValueChange = {Search.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                ,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Phone),
                keyboardActions = KeyboardActions(
                    onDone = {
                        movimientosViewModel.PostMovimiento(
                            Search.value,context
                        )
                    }
                ),
                label = { Text("Caja, Gaveta o Ubicación") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            campedido.value = false
                            campedido2.value = !campedido2.value
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.QrCode2,
                            contentDescription = "Icon Search",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .scale(scaleY = 0.7F, scaleX = .7F)
                        )
                    }
                }
            )
        }
        AnimatedVisibility(
            visible = campedido2.value,
            enter = enterExpand + enterFadeIn,
            exit = exitCollapse + exitFadeOut
        ) {
            Box(modifier = Modifier
                .height(252.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                var preview by remember { mutableStateOf<Preview?>(null) }

                val datastore = SharedPrefence(LocalContext.current)
                val subcentro = datastore.getSubCentro()
                val usuID = datastore.getUsuID()
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
                                                if(campedido2.value){
                                                    mMediaPlayer.start()
                                                    Search.value = barcodeValue
                                                    campedido2.value = false
                                                    movimientosViewModel.PostMovimiento(
                                                        Search.value,context
                                                    )
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
            text = "Movimiento de materiales.",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}