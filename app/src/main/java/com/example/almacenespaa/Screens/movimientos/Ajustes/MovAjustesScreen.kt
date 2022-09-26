package com.example.almacenespaa.Network.Models.Moviemientos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.almacenespaa.R
import com.example.almacenespaa.ui.theme.blackdark

@Composable
fun MovAjustesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        titleAjustes()
        Spacer(modifier = Modifier.size(5.dp))
        ContentAjustes()
    }
}
@Composable
private fun titleAjustes(
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
            text = "Ajustes.",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}



@Composable
fun ContentAjustes(){
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(blackdark)
    ) {

    }
}