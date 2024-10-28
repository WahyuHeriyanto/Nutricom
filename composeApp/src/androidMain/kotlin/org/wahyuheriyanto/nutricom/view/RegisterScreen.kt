package org.wahyuheriyanto.nutricom.view

import android.app.Activity
import android.provider.Settings.Global.getString
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.wahyuheriyanto.nutricom.MainActivity.Companion.REQUEST_CODE_GOOGLE_SIGN_IN

import org.wahyuheriyanto.nutricom.R

// In your Android-specific Activity or Fragment


@Composable
fun RegisterNav(viewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            RegisterScreen(viewModel) {
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
fun RegisterScreen(viewModel: AuthViewModel, onLoginSuccess: () -> Unit) {
    val loginState by viewModel.loginState.collectAsState()

    var checked by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var phoneNum by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var checkPass by remember{ mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }  // State untuk dialog sukses
    var showErrorDialog by remember { mutableStateOf(false) } // State untuk dialog error


    val context = LocalContext.current
    val activity = context as? Activity

    // GoogleSignInOptions setup
    val googleSignInOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)) // Web client ID from Firebase
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    // Register for activity result launcher
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            // Pass Google ID token to FirebaseAuth
            val idToken = account?.idToken
            if (idToken != null) {
                viewModel.loginWithGoogle(idToken)
            }
        } catch (e: ApiException) {
            // Handle error
        }
    }





    // LaunchedEffect untuk memantau perubahan loginState
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                onLoginSuccess()  // Pindah ke HomeScreen
            }
            is LoginState.Error -> {
                showErrorDialog = true
            }
            is LoginState.Loading -> {

            }
            LoginState.Idle -> {

            }
        }
    }

    Image(painter = painterResource(R.drawable.background_login),
        contentDescription = null,
        modifier = Modifier.fillMaxSize())
    ConstraintLayout {

        val (title, loginBox) = createRefs()

        val backStartGuideline = createGuidelineFromStart(0.2f)
        val backTopGuideline = createGuidelineFromTop(0.03f)
        val backEndGuideline = createGuidelineFromEnd(0.2f)
        val backBottomGuideline = createGuidelineFromBottom(0.2f)


        Text(text = "SIGN UP", fontSize = 30.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(title){
                start.linkTo(backStartGuideline)
                end.linkTo(backEndGuideline)
                top.linkTo(backTopGuideline)
            })

        Box(
            modifier = Modifier
                .padding(0.dp, 10.dp)
                .size(320.dp, 600.dp)
                .constrainAs(loginBox) {
                    start.linkTo(backStartGuideline)
                    end.linkTo(backEndGuideline)
                    top.linkTo(title.bottom)
                }
                .clip(RoundedCornerShape(10.dp))
                .background(Color(android.graphics.Color.parseColor("#00AA16")))
        ) {
            ConstraintLayout {
                val (googlesign, rememberme, textOne, textTwo, emailCon, loginCon, nameCon,userCon,
                    phoneCon, birthCon, passCon,confirmCon, registerCon) = createRefs()

                val startGuideline = createGuidelineFromStart(0.4f)
                val endGuideline = createGuidelineFromEnd(0.4f)
                val topGuideline = createGuidelineFromTop(0.05f)

                val checkStartGuideline = createGuidelineFromStart(0.05f)
                val checkEndGuideline = createGuidelineFromEnd(0.05f)

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .width(250.dp)
                        .padding(0.dp, 10.dp)
                        .constrainAs(emailCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(topGuideline)
                        }
                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full name") },
                    modifier = Modifier
                        .width(250.dp)
                        .constrainAs(nameCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(emailCon.bottom)
                        }
                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )

                TextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Username") },
                    modifier = Modifier
                        .width(250.dp)
                        .padding(0.dp, 10.dp)
                        .constrainAs(userCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(nameCon.bottom)
                        }
                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )

                TextField(
                    value = phoneNum,
                    onValueChange = { phoneNum = it },
                    label = { Text("Phone number") },
                    modifier = Modifier
                        .width(250.dp)
                        .constrainAs(phoneCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(userCon.bottom)
                        }
                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )

                TextField(
                    value = birth,
                    onValueChange = { birth = it },
                    label = { Text("date and birth") },
                    modifier = Modifier
                        .width(250.dp)
                        .padding(0.dp, 10.dp)
                        .constrainAs(birthCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(phoneCon.bottom)
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
                            top.linkTo(birthCon.bottom)
                        }

                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )

                TextField(
                    value = checkPass,
                    onValueChange = { checkPass = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .width(250.dp)
                        .constrainAs(confirmCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(passCon.bottom)
                        }
                        .padding(0.dp, 10.dp)
                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )





                Button(
                    onClick = { viewModel.register(email, password) },
                    modifier = Modifier
                        .constrainAs(loginCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(confirmCon.bottom)
                        }
                        .padding(10.dp)
                        .size(120.dp, 40.dp)
                        .background(Color.Transparent),
                    colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#CAE8AC")))
                ) {
                    Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.background(color = Color.Transparent))
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

            // Tampilan AlertDialog untuk error login
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showErrorDialog = false
                    },
                    title = { Text("Login Gagal") },
                    text = { Text("Email dan password tidak terdaftar.") },
                    confirmButton = {
                        Button(onClick = {
                            showErrorDialog = false
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

fun RegisterPreview(){

    Surface(modifier= Modifier.fillMaxSize()) {
        RegisterScreen(viewModel = AuthViewModel()) {

        }

    }
}