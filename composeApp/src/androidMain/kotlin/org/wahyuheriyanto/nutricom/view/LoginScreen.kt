package org.wahyuheriyanto.nutricom.view

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.data.DataStoreUtils
import org.wahyuheriyanto.nutricom.model.LoginItem
import org.wahyuheriyanto.nutricom.model.UidItem
import org.wahyuheriyanto.nutricom.model.UserItem
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.ScanViewModel
import org.wahyuheriyanto.nutricom.viewmodel.performData
import org.wahyuheriyanto.nutricom.viewmodel.performDataLogin


@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalGetImage
@Composable
fun MainScreen(viewModel: AuthViewModel) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val savedCredentials by DataStoreUtils.getCredentials(context).collectAsState(initial = Pair(null, null))
    var email by remember { mutableStateOf(savedCredentials.first ?: "") }
    var password by remember { mutableStateOf(savedCredentials.second ?: "") }
    val loginState by viewModel.loginState.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    var uidCredentials by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        Log.e("MainScreen", "SplashScreen mulai (4 detik)")
        delay(4000)
        DataStoreUtils.getLoginCredentials(context).collect { uid ->
            Log.e("MainScreen", "UID Loaded dari DataStore: $uid")
            uidCredentials = uid
            performDataLogin(viewModel, viewModelTwo = DataViewModel(),uidCredentials)
            isLoading = false
        }
    }


    // Tampilkan SplashScreen selama loading
    if (isLoading) {
        Log.e("MainScreen", "Menampilkan SplashScreen")
        SplashScreen()
        return
    }

    // Mengisi email dan password dari DataStore jika tersedia
    LaunchedEffect(savedCredentials) {
        Log.e("cekjalur","jalur 1")
        savedCredentials.first?.let { email = it }
        savedCredentials.second?.let { password = it }
    }

    // Melakukan login hanya jika uidCredentials sudah ada dan hanya sekali
    LaunchedEffect(uidCredentials) {
        if (!uidCredentials.isNullOrEmpty()) {
            Log.e("cekjalur","jalur 2")
            performData(viewModel = viewModel, viewModelTwo = DataViewModel())
            navController.navigate("home") {
                popUpTo(0) { inclusive = true }
            }
        }
    }


    // Navigasi sesuai dengan kondisi uidCredentials
    NavHost(navController,
        startDestination = if (uidCredentials.isNullOrEmpty()) "login" else "home" )
    {
            Log.e("cekjalur","jalur 3")
//        NavHost(navController, startDestination = if (uidCredentials.isNullOrEmpty()) "login" else "home") {
        composable("login") {
            LoginScreen(navController,viewModel, onLoginSuccess = {
                navController.navigate("home") {
                    popUpTo(0) { inclusive = true }
                }
            }, onSignUpClick = {
                navController.navigate("register")
            })
        }

        composable("home") {
            NavigationBar(viewModel = viewModel, viewModelTwo = DataViewModel(), viewModelThree = ScanViewModel(), navController = navController)
        }

        composable("register") {
            RegisterScreen(viewModel = AuthViewModel()) {
            }
        }
        composable("ubahSandi") {
            UbahSandiScreen(navController = navController)
        }


    }
}

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel, onLoginSuccess: () -> Unit, onSignUpClick: () -> Unit) {
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()
    val savedCredentials by DataStoreUtils.getCredentials(context).collectAsState(initial = Pair(null, null))
    val uidCredentials by DataStoreUtils.getLoginCredentials(context).collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()

    var uidLogin by remember { mutableStateOf(uidCredentials ?: "") }
    var checked by remember { mutableStateOf(savedCredentials.first != null) }
    var email by remember { mutableStateOf(savedCredentials.first ?: "") }
    var password by remember { mutableStateOf(savedCredentials.second ?: "") }
    var showSuccessDialog by remember { mutableStateOf(false) }  // State untuk dialog sukses
    var showErrorDialog by remember { mutableStateOf(false) } // State untuk dialog error
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) } // State untuk visibility password

    Log.e("cekjalur","jalur 4")

    val activity = context as? Activity

    // Email validation function
    fun validateEmail(input: String): String? {
        return when {
            "@" !in input -> "Email harus mengandung '@'."
            input.contains(" ") -> "Email tidak boleh mengandung spasi."
            else -> null
        }
    }

    // Password validation function
    fun validatePassword(input: String, email: String): String? {
        val emailLocalPart = email.substringBefore("@")
        return when {
            input.length < 6 -> "Password minimal harus 6 karakter."
            emailLocalPart.isNotEmpty() && emailLocalPart in input -> "Password tidak boleh mengandung nama dari email."
            else -> null
        }
    }

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

    LaunchedEffect(savedCredentials) {
        savedCredentials.first?.let { email = it }
        savedCredentials.second?.let { password = it }
        Log.e("cekjalur","jalur 5")

    }

    // LaunchedEffect untuk memantau perubahan loginState
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success && uidCredentials.isNullOrEmpty()) {
            Log.e("cekjalur","jalur 6")
            DataStoreUtils.saveLoginCredentials(context, viewModel.uid.value)
            onLoginSuccess()  // Pindah ke HomeScreen
        }
    }


    Image(
        painter = painterResource(R.drawable.background_login),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop // Menggunakan Crop untuk menutupi layar sepenuhnya
    )

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
                Log.e("cekjalur","jalur 7")
                val (googlesign, rememberme,textTwo, emailCon, passCon, loginCon, registerText, warningCon) = createRefs()

                val startGuideline = createGuidelineFromStart(0.4f)
                val endGuideline = createGuidelineFromEnd(0.4f)
                val topGuideline = createGuidelineFromTop(0.1f)
                val checkStartGuideline = createGuidelineFromStart(0.05f)
                val checkEndGuideline = createGuidelineFromEnd(0.05f)

                TextField(
                    value = email,
                    onValueChange = { email = it
                        emailError = validateEmail(it)},
                    label = { Text("Email") },
                    singleLine = true,
                    isError = emailError != null,
                    modifier = Modifier
                        .width(250.dp)
                        .constrainAs(emailCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(topGuideline)
                        }
                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )
                emailError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.constrainAs(warningCon){
                            start.linkTo(emailCon.start)
                            bottom.linkTo(emailCon.top)
                        }
                    )
                }

                TextField(
                    value = password,
                    onValueChange = { password = it
                        passwordError = validatePassword(it, email)},
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    isError = passwordError != null,
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(id = if (passwordVisible) R.drawable.open_eye else R.drawable.close_eye),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    },
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

                passwordError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

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
                        , color = Color.White,
                        modifier = Modifier.clickable {
                            navController.navigate("ubahSandi")
                        }
                    )

                }

                Button(
                    onClick = {
                        viewModel.login(LoginItem(email,password))
                        if (checked) {
                            // Save email and password when the user logs in
                            coroutineScope.launch {
                                DataStoreUtils.saveCredentials(context, email, password)
                            }
                        }
                              },
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
                    modifier = Modifier
                        .constrainAs(googlesign) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(textTwo.bottom)
                        }
                        .padding(0.dp, 10.dp),
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
        MainScreen(viewModel = AuthViewModel())

    }
}



