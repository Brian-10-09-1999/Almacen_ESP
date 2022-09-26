package com.example.zitrocrm.screens.login.components

import android.media.MediaPlayer
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.example.almacenespaa.R
import com.example.almacenespaa.Screens.surtir_material.*
import com.example.almacenespaa.Utils
import com.example.almacenespaa.ui.theme.blackdark
import com.example.almacenespaa.utils.BarcodeAnalyser
import com.example.zitrocrm.repository.SharedPrefence
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val alert = mutableStateOf(0)
val progressBarString = mutableStateOf("")
fun alertdialog(alrt:Int,string: String){
    alert.value = alrt
    progressBarString.value = string
}
@Composable
fun ProgressBarLoading(
    viewModel:PedidosMaterialViewModel
) {
    if (alert.value==1) {
        AlertDialog(
            onDismissRequest = {
            },
            confirmButton = {
                /*val context = LocalContext.current
                val mMediaPlayer = MediaPlayer.create(context, R.raw.bip)
                mMediaPlayer.start()*/
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(30.dp))
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 6.dp,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(
                        text = "Cargando..",
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = progressBarString.value,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(25.dp))
                }
            }
        )
    }
    if (alert.value==2) {
        AlertDialog(
            onDismissRequest = {
            },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(20.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5.dp))
                                .border(
                                    border = BorderStroke(
                                        width = 3.dp,
                                        color = colorResource(id = R.color.reds)
                                    )
                                )
                                .padding(10.dp),
                        ) {
                            Icon(
                                Icons.Filled.Notes,
                                contentDescription = "",
                                Modifier.align(Alignment.CenterStart)
                            )
                            Text(
                                text = "Informacion",
                                style = MaterialTheme.typography.subtitle2,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(
                        text = progressBarString.value,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                }
            }
        )
    }
    if (alert.value==3) {
        AlertDialog(
            onDismissRequest = {
            },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(25.dp))
                    Icon(Icons.Filled.Cancel, contentDescription = "",
                        Modifier
                            .size(60.dp)
                            .align(CenterHorizontally))
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(
                        text = "Error..",
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "No se pudo contactar al servidor, comprueba tu conexion o intentalo mas tarde.",
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 15.dp)
                    )
                    Spacer(modifier = Modifier.size(25.dp))
                }
            }
        )
    }
    if (alert.value==4) {
        AlertDialog(
            onDismissRequest = {
                alert.value=0
            },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(5.dp))
                            .border(
                                border = BorderStroke(
                                    width = 3.dp,
                                    color = colorResource(id = R.color.reds)
                                )
                            )
                            .padding(10.dp),
                    ) {
                        Icon(
                            Icons.Filled.Notes,
                            contentDescription = "",
                            Modifier.align(Alignment.CenterStart)
                        )
                        Text(
                            text = "Selecciona cantidad a surtir",
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    var drop = remember { mutableStateOf(false) }
                    OutlinedTextField(
                        enabled = false,
                        value = viewModel.cantidadPz.value,
                        onValueChange = {  },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Ascii,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {  }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                drop.value = !drop.value
                            }
                        ,
                        label = { Text("Cantidad") },
                        trailingIcon = {
                            Icon(Icons.Filled.MenuOpen,"contentDescription",
                                Modifier.clickable { drop.value = !drop.value })
                        }
                    )
                    DropdownMenu(
                        expanded = drop.value,
                        onDismissRequest = { drop.value = false },
                        modifier = Modifier.width(300.dp).padding(horizontal = 5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                                .background(color = colorResource(R.color.reds))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center)
                            ) {
                                Icon(
                                    Icons.Filled.ArrowBack, "Hora", modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(horizontal = 10.dp)
                                        .clickable {
                                            drop.value = false
                                        }
                                )
                                Text(
                                    text = "Selecciona la cantidad a surtir",
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                        }
                        viewModel.pzsurtir.forEach {
                            DropdownMenuItem(onClick = {
                                viewModel.cantidadPz.value = it.toString()
                                drop.value = false
                            }) {
                                Text(text = it.toString()+"pz")
                            }
                        }
                    }
                    val campedido = remember { mutableStateOf(false)}

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (alert.value==0){
                            viewModel.ubicacion.value = ""
                        }
                        val context = LocalContext.current
                        OutlinedTextField(
                            value = viewModel.ubicacion.value ,
                            onValueChange = {
                                val qr_split = it.split("\n")
                                if (Utils(context).getValidat(it)){
                                    viewModel.postSurtir(context)
                                }
                                viewModel.ubicacion.value = qr_split[0]
                                            },
                            modifier = Modifier
                                .fillMaxWidth()
                            ,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                }
                            ),
                            label = { Text("Ingresa ubicacion") },
                            trailingIcon = {
                                IconButton(
                                    onClick = { campedido.value = !campedido.value }
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
                            .height(200.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            val context = LocalContext.current
                            val lifecycleOwner = LocalLifecycleOwner.current
                            var preview by remember { mutableStateOf<Preview?>(null) }
                            val datastore = SharedPrefence(LocalContext.current)
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
                                                                mMediaPlayer.start()
                                                                viewModel.ubicacion.value = barcodeValue
                                                                viewModel.postSurtir(context)
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
                    Spacer(modifier = Modifier.size(5.dp))
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            viewModel.postSurtir(context)
                            alert.value=0
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),

                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.reds)
                        )
                    ) {
                        Text(
                            text = "Surtir pedido",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.size(5.dp))
                }
            }
        )
    }
}