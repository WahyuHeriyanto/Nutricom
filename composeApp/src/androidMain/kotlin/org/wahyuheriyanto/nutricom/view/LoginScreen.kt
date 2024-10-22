package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.wahyuheriyanto.nutricom.R

@Composable
fun MainScreen(viewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(viewModel) {
                // Navigasi ke HomeScreen setelah login berhasil
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true } // Menghapus halaman login dari stack
                }
            }
        }
        composable("home") {
            HomeScreen()
        }
    }
}


@Composable
fun LoginScreen(viewModel: AuthViewModel, onLoginSuccess: () -> Unit) {
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }  // State untuk dialog sukses
    var showErrorDialog by remember { mutableStateOf(false) } // State untuk dialog error

    // LaunchedEffect untuk memantau perubahan loginState
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                onLoginSuccess()  // Pindah ke HomeScreen
            }
            is LoginState.Error -> {
                showErrorDialog = true  // Tampilkan dialog error
            }
            is LoginState.Loading -> {
                // Optional: Menampilkan loading indicator jika perlu
            }
            LoginState.Idle -> {
                // Optional: Tidak ada aksi
            }
        }
    }

    Image(painter = painterResource(R.drawable.background_login),
        contentDescription = null,
        modifier = Modifier.fillMaxSize())
    ConstraintLayout {

        val (logo, title, loginBox) = createRefs()

        val backStartGuideline = createGuidelineFromStart(0.2f)
        val backTopGuideline = createGuidelineFromTop(0.03f)
        val backEndGuideline = createGuidelineFromEnd(0.2f)
        val backBottomGuideline = createGuidelineFromBottom(0.2f)

        Image(painter = painterResource(id = R.drawable.logo_signin_nutricom),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp, 100.dp)
                .constrainAs(logo) {
                    start.linkTo(backStartGuideline)
                    end.linkTo(backEndGuideline)
                    top.linkTo(backTopGuideline)
                }
        )


        Text(text = "SIGN IN", fontSize = 30.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(title){
                start.linkTo(backStartGuideline)
                end.linkTo(backEndGuideline)
                top.linkTo(logo.bottom)
            })

        Box(
            modifier = Modifier
                .padding(0.dp, 10.dp)
                .size(320.dp, 400.dp)
                .constrainAs(loginBox) {
                    start.linkTo(backStartGuideline)
                    end.linkTo(backEndGuideline)
                    top.linkTo(title.bottom)
                }
                .clip(RoundedCornerShape(10.dp))
                .background(Color(android.graphics.Color.parseColor("#00AA16")))
        ) {
            ConstraintLayout {
                val (textOne, textTwo, emailCon, passCon, loginCon, registerCon) = createRefs()

                val startGuideline = createGuidelineFromStart(0.4f)
                val endGuideline = createGuidelineFromEnd(0.4f)
                val topGuideline = createGuidelineFromTop(0.2f)

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .width(250.dp)
                        .constrainAs(emailCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(topGuideline)
                        }
                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .width(250.dp)
                        .constrainAs(passCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(emailCon.bottom)
                        }
                        .padding(0.dp, 10.dp)
                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )

                Text("Forgot Password", modifier = Modifier.constrainAs(textOne) {
                    start.linkTo(passCon.start)
                    top.linkTo(passCon.bottom)
                }, color = Color.White)

                Button(
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier
                        .constrainAs(loginCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(textOne.bottom)
                        }
                        .padding(10.dp)
                        .size(120.dp,40.dp)
                        .background(Color.Transparent),
                    colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#CAE8AC")))
                ) {
                    Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.background(color = Color.Transparent))
                }

                Text("Or Sign In with", modifier = Modifier.constrainAs(textTwo) {
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    top.linkTo(loginCon.bottom)
                }, color = Color.White)

//                Button(
//                    onClick = { viewModel.register(email, password) },
//                    modifier = Modifier
//                        .constrainAs(registerCon) {
//                            start.linkTo(startGuideline)
//                            end.linkTo(endGuideline)
//                            top.linkTo(loginCon.bottom)
//                        }
//                        .padding(10.dp)
//                        .background(Color.Transparent),
//                    colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#CAE8AC")))
//                ) {
//                    Text("Register", modifier = Modifier.background(color = Color.Transparent))
//                }

                when (loginState) {
                    is LoginState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is LoginState.Error -> {
                        Text("Error: ${(loginState as LoginState.Error).message}")
                    }
                    else -> {
                        // Idle state
                    }
                }
            }

            // Tampilan AlertDialog untuk error login
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showErrorDialog = false  // Menghilangkan dialog ketika di-dismiss
                    },
                    title = { Text("Login Gagal") },
                    text = { Text("Email dan password tidak terdaftar.") },
                    confirmButton = {
                        Button(onClick = {
                            showErrorDialog = false  // Menutup dialog dengan mengubah state
                        }) {
                            Text("OK")
                        }
                    }
                )
            }
        }

    }


}

@Preview
@Composable

fun UIPreview(){

    Surface(modifier= Modifier.fillMaxSize()) {
        LoginScreen(viewModel = AuthViewModel()) {
            
        }

    }
}



