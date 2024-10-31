package org.wahyuheriyanto.nutricom.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DatabaseViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState


@Composable
fun NavigationBar(viewModel: AuthViewModel,
    onTopBarActionClick: () -> Unit = {},
    onBottomNavItemSelected: (String) -> Unit = {},
            content: @Composable (PaddingValues) -> Unit
) {


    val loginState by viewModel.loginState.collectAsState()
    val point by viewModel.points.collectAsState()

    LaunchedEffect(loginState){
        Log.e("cekIsi","Penasaran : $point")

        when (loginState) {
            is LoginState.Success -> {
                Log.e("cekIsi","point ini wkwkwk : $point")

            }
            is LoginState.Error -> {
                Log.e("Error","Belum keisi")

            }
            is LoginState.Loading -> {

            }
            LoginState.Idle -> {
                Log.e("Errorku","Belum keisi kocak")
            }
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Image(painter = painterResource(id = R.drawable.nutricom_logo_small),
                    contentDescription = "",
                    modifier = Modifier.size(90.dp)) },
                backgroundColor = Color.White,
                actions = {
                    IconButton(onClick = onTopBarActionClick,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .padding(0.dp, 0.dp)
                            .width(150.dp)
                            .height(35.dp)
                            .background(Color(android.graphics.Color.parseColor("#DCEFC9")))) {

                        Row {
                            Icon(painter = painterResource(id = R.drawable.wallet_icon), contentDescription = "",
                                modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))

                            when (loginState) {
                        is LoginState.Loading -> {

                        }
                        is LoginState.Success -> {
                            val points = point
                            Text(text = "$points pts", fontSize = 18.sp)
                            Log.e("CekPoint","Point 2 : $points")
                        }
                        else -> {
                            // Idle state
                        }
                    }


                        }

                    }
                    IconButton(onClick = onTopBarActionClick ) {
                        Icon(painter = painterResource(id = R.drawable.notification_icon), contentDescription = "",
                            modifier = Modifier.size(30.dp))
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(backgroundColor = Color.White,
            //Color(android.graphics.Color.parseColor("#CAE8AC")
            ) {
                BottomNavigationItem(

                    icon = { Icon(painter = painterResource(id = R.drawable.home_icon), contentDescription ="",
                        modifier = Modifier.size(30.dp)) },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { onBottomNavItemSelected("home") },
                )
                BottomNavigationItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.running_icon), contentDescription ="",
                        modifier = Modifier.size(30.dp)) },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { onBottomNavItemSelected("settings") }
                )
                BottomNavigationItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.basket_icon), contentDescription = "",
                        modifier = Modifier.size(30.dp)) },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { onBottomNavItemSelected("settings") }
                )
                BottomNavigationItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.globe_icon), contentDescription = "",
                        modifier = Modifier.size(30.dp)) },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { onBottomNavItemSelected("settings") }
                )
            }
        },
        content = {paddingValues ->
            // Menambahkan padding values ke dalam konten
            Box(modifier = Modifier.padding(paddingValues)) {
                content(paddingValues)
            }}
    )
}

@Preview
@Composable

fun NavPreview(){
    Surface (modifier = Modifier.fillMaxSize()){
        NavigationBar(viewModel = AuthViewModel()) {
            
        }
    }
}