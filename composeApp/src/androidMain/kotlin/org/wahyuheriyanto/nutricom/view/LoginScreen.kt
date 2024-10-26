package org.wahyuheriyanto.nutricom.view

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.wahyuheriyanto.nutricom.R


@Composable
//Main Screen of Login Page
fun MainScreen(viewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(viewModel, onLoginSuccess = {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }, onSignUpClick = {
                navController.navigate("register")
            })
        } //Login Page Navigation
        composable("home") {
            HomeScreen()
        } //Home Page Navigation
        composable("register") {
            RegisterScreen(viewModel = AuthViewModel()) {
            }
        } //Register Page Navigation
    } //NavHost
} //MainScreen

@Composable
fun LoginScreen(viewModel: AuthViewModel, onLoginSuccess: () -> Unit, onSignUpClick: () -> Unit) {
    val loginState by viewModel.loginState.collectAsState()

    var checked by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                .size(320.dp, 420.dp)
                .constrainAs(loginBox) {
                    start.linkTo(backStartGuideline)
                    end.linkTo(backEndGuideline)
                    top.linkTo(title.bottom)
                }
                .clip(RoundedCornerShape(10.dp))
                .background(Color(android.graphics.Color.parseColor("#00AA16")))
        ) {
            ConstraintLayout {
                val (progress, googlesign, rememberme, textOne, textTwo, emailCon, passCon, loginCon, registerText) = createRefs()

                val startGuideline = createGuidelineFromStart(0.4f)
                val endGuideline = createGuidelineFromEnd(0.4f)
                val topGuideline = createGuidelineFromTop(0.1f)
                val checkStartGuideline = createGuidelineFromStart(0.05f)
                val checkEndGuideline = createGuidelineFromEnd(0.05f)

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
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
                    singleLine = true,
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(rememberme){
                    start.linkTo(checkStartGuideline)
                    top.linkTo(passCon.bottom)
                }

                ) {

                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                    Text(
                        "Remember me", color = Color.White
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text("Forgot Password"
                        , color = Color.White
                    )

                }

                Button(
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier
                        .constrainAs(loginCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(rememberme.bottom)
                        }
                        .padding(10.dp)
                        .size(120.dp, 40.dp)
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

                Button(
                    onClick = {
                        // Trigger Google Sign-In here
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
                    },
                    modifier = Modifier.constrainAs(googlesign) {
                        start.linkTo(startGuideline)
                        end.linkTo(endGuideline)
                        top.linkTo(textTwo.bottom)
                    }
                        .padding(0.dp,10.dp),
                    colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#FFFFFF")))

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)// Jarak antara ikon dan teks

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp) // Ukuran ikon Google
                        )
                        Text("Login with Google")
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .constrainAs(registerText) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(googlesign.bottom)
                        }
                ) {

                    Text(
                        "Don't have an account?",
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    Text(
                        "Sign Up",
                        modifier = Modifier.clickable { onSignUpClick() },
                        color = Color.White,
                        fontSize = 16.sp
                    )

                }


                Box(modifier = Modifier.constrainAs(progress){
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    bottom.linkTo(emailCon.top)
                }){
//                    when (loginState) {
//                        is LoginState.Loading -> {
//                            CircularProgressIndicator()
//                        }
//                        is LoginState.Error -> {
//                            Text((loginState as LoginState.Error).message, color = Color.Red)
//                        }
//                        else -> {
//                            // Idle state
//                        }
//                    }
                }


            }
            // Show AlertDialog for login error
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showErrorDialog = false
                    },
                    title = { Text("Login Gagal") },
                    text = { Text("Email and password invalid. Please try again") },
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
fun UIPreview(){
    Surface(modifier= Modifier.fillMaxSize()) {
        LoginScreen(viewModel = AuthViewModel(), onLoginSuccess = { /*TODO*/ }) {
        }
    }
}



