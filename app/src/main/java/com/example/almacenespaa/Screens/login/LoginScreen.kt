package com.example.zitrocrm.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.almacenespaa.R
import com.example.almacenespaa.ui.theme.blacktransp
import com.example.zitrocrm.repository.SharedPrefence

import com.example.zitrocrm.screens.login.LoginViewModel
import com.example.zitrocrm.screens.login.components.*

@Composable
fun LoginScreen(
    onclickLogin: (user: String, pwd: String, context: Context) -> Unit,
    imageError: Boolean,
    loginViewModel : LoginViewModel
)
{   val context = LocalContext.current
    val datastore = SharedPrefence(LocalContext.current)
    val user = datastore.getUserLogin().toString()
    val psw = datastore.getPassLogin().toString()
    if(datastore.getInicioLogin().toString()=="true"){
        loginViewModel.checkInicio.value = true
        loginViewModel.login(user,psw,context)
    }
    Scaffold {
        Image(
            painter = painterResource(R.drawable.backgroud_crmm),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier
            .padding(0.dp, 55.dp, 0.dp, 0.dp)
            .fillMaxSize()){

            Image(
                painter = painterResource(R.drawable.log_almacen),
                contentDescription = null,
                modifier = Modifier
                    .size(185.dp)
                    .align(TopCenter),
                contentScale = ContentScale.Fit
            )
        }
        Box(modifier = Modifier.padding(0.dp,280.dp,0.dp,50.dp))
        {
            Spacer(
                Modifier
                    .matchParentSize()
                    .background(color = blacktransp)
            )

            var user by rememberSaveable { mutableStateOf(value = "") }
            var pwd by rememberSaveable { mutableStateOf(value = "") }
            val isValidate by derivedStateOf { user.isNotBlank() && pwd.isNotBlank() }
            val focusManager = LocalFocusManager.current
            val ctxt = LocalContext.current

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(Modifier.size(10.dp))

                Text(
                    text = "Iniciar Sesión",
                    color = Color.White,
                    fontSize = 25.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.subtitle2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.size(40.dp))

                EmailOutTextField(
                    textValue = user,
                    onValueChange = { user = it },
                    onClickButton = { user = "" },
                    onNext = { focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                Spacer(Modifier.size(30.dp))

                PasswordOutTextField(
                    textValue = pwd,
                    onValueChange = { pwd = it },
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
                Row(
                    Modifier
                        .padding(vertical = 0.dp)
                        .align(End)) {
                    Text(text = "Guardar Inicio de Sesión",
                        modifier =
                        Modifier
                            .padding(
                                horizontal = 3.dp
                            )
                            .align(CenterVertically),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Start,
                    )
                    Checkbox(
                        checked = loginViewModel.checkInicio.value ,
                        onCheckedChange = {
                            loginViewModel.checkInicio.value = it
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = colorResource(R.color.reds),
                            uncheckedColor = colorResource(R.color.blackdark),
                            checkmarkColor = colorResource(R.color.white)
                        )
                    )
                }
                Spacer(Modifier.size(5.dp))
                ButtonLogin(
                    onclick = { onclickLogin(user, pwd, ctxt) },
                    enabled = isValidate
                )
                Spacer(Modifier.size(20.dp))
            }
        }
        ErrorImageAuth(isImageValidate = imageError)
        //ProgressBarLoading(context)
    }
}
