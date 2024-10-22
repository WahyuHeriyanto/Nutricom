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

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState

@Composable
fun LoginScreen(viewModel: AuthViewModel) {
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }  // State untuk dialog sukses

    // LaunchedEffect untuk memantau perubahan loginState dan memicu dialog sukses
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            showSuccessDialog = true
        }
    }

    Box(
        modifier = Modifier.padding(20.dp, 150.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(android.graphics.Color.parseColor("#00AA16")))
    ) {
        ConstraintLayout {
            val (textOne, emailCon, passCon, loginCon, registerCon) = createRefs()

            val startGuideline = createGuidelineFromStart(0.3f)
            val endGuideline = createGuidelineFromEnd(0.3f)
            val topGuideline = createGuidelineFromTop(0.2f)

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.constrainAs(emailCon) {
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
                modifier = Modifier.constrainAs(passCon) {
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
                modifier = Modifier.constrainAs(loginCon) {
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    top.linkTo(textOne.bottom)
                }
                    .padding(10.dp)
                    .background(Color.Transparent),
                colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#CAE8AC")))
            ) {
                Text("Login", modifier = Modifier.background(color = Color.Transparent))
            }

            // Tombol untuk Register
            Button(
                onClick = { viewModel.register(email, password) },
                modifier = Modifier.constrainAs(registerCon) {
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    top.linkTo(loginCon.bottom)
                }
                    .padding(10.dp)
                    .background(Color.Transparent),
                colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#CAE8AC")))
            ) {
                Text("Register", modifier = Modifier.background(color = Color.Transparent))
            }

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

        // Tampilan AlertDialog untuk registrasi sukses
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSuccessDialog = false  // Menghilangkan dialog ketika di-dismiss
                },
                title = { Text("Registrasi Berhasil") },
                text = { Text("Akun Anda telah berhasil terdaftar.") },
                confirmButton = {
                    Button(onClick = {
                        showSuccessDialog = false  // Menutup dialog dengan mengubah state
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}



