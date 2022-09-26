package com.example.almacenespaa.Screens.surtir_material

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.almacenespaa.R

var alertComentarios = mutableStateOf(false)
var alertSimilar = mutableStateOf(false)
var alertSimilaApi = mutableStateOf(false)

@Composable
fun AlertPedidos(
    pedidosMaterialViewModel: PedidosMaterialViewModel,
){
    AnimatedVisibility(
        visible = alertComentarios.value
    ) {
        AlertDialog(
            onDismissRequest = {
                alertComentarios.value = false
            },
            confirmButton = {
                Spacer(modifier = Modifier.size(1.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .border(
                            border = BorderStroke(
                                width = 3.dp,
                                color = colorResource(id = R.color.reds)
                            )
                        ).padding(10.dp),
                ) {
                    Icon(Icons.Filled.Comment, contentDescription ="" ,Modifier.align(CenterStart))
                    Text(text = "Comentarios",style = MaterialTheme.typography.subtitle2, modifier = Modifier.align(Alignment.Center))
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                        .clip(RoundedCornerShape(3.dp))
                        .border(
                            border = BorderStroke(
                                width = 2.dp,
                                color = colorResource(id = R.color.reds)
                            )
                        )
                        .padding(5.dp)
                    /*.background(colorResource(R.color.blackdark))*/,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(pedidosMaterialViewModel.comentarios.value.isBlank()){
                        Text(
                            text = "No hay comentarios",
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.padding(horizontal = 10.dp),
                            color = Color.White
                        )
                    }else{
                        Text(
                            text = pedidosMaterialViewModel.comentarios.value,
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.padding(horizontal = 10.dp),
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.size(1.dp))
            }
        )
    }
}