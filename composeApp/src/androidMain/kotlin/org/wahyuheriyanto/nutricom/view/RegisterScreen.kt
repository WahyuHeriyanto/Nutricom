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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import android.app.DatePickerDialog
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import java.util.Calendar
import android.content.Context
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

enum class PasswordStrength(val label: String, val color: Color) {
    WEAK("Weak", Color.Red),
    FAIR("Fair", Color.Yellow),
    GOOD("Good", Color.Blue),
    STRONG("Strong", Color.Green)
}

fun evaluatePasswordStrength(password: String): Pair<PasswordStrength, Int> {
    val lengthScore = password.length * 5
    val uppercaseScore = if (password.any { it.isUpperCase() }) 20 else 0
    val numberScore = if (password.any { it.isDigit() }) 10 else 0
    val specialCharScore = if (password.any { !it.isLetterOrDigit() }) 10 else 0

    val totalScore = lengthScore + uppercaseScore + numberScore + specialCharScore

    return when {
        totalScore < 30 -> Pair(PasswordStrength.WEAK, totalScore)
        totalScore < 60 -> Pair(PasswordStrength.FAIR, totalScore)
        totalScore < 80 -> Pair(PasswordStrength.GOOD, totalScore)
        else -> Pair(PasswordStrength.STRONG, totalScore)
    }
}





@Composable
fun RegisterScreen(viewModel: AuthViewModel, onLoginSuccess: () -> Unit) {
    val loginState by viewModel.loginState.collectAsState()
    var passwordStrength by remember { mutableStateOf(PasswordStrength.WEAK) }
    var passwordStrengthPercent by remember { mutableStateOf(0) }



    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var phoneNum by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var checkPass by remember{ mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var showErrorDialog by remember { mutableStateOf(false) } // State untuk dialog error
    var passwordVisible by remember { mutableStateOf(false) } // State untuk visibility password



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

    // DatePicker Dialog
    fun showDatePickerDialog(context: Context) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            // Format the selected date to dd/MM/yyyy
            birth = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
        }, year, month, day).show()
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

    // Email validation function
    fun validateEmail(input: String): String? {
        return when {
            "@" !in input -> "Email harus mengandung '@'."
            input.contains(" ") -> "Email tidak boleh mengandung spasi."
            else -> null
        }
    }

    fun validatePass(input: String): String? {
        return when {
            input != password  -> "The password you entered is not the same"
            else -> null
        }
    }

    fun validatePhone(input: String): String? {
        return when {
            input.any { !it.isDigit() } -> "Input must number"
            input.length < 10 -> "min 10 digits of number"
            else -> null
        }
    }

    Image(painter = painterResource(R.drawable.background_login),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop)

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
                val (progress, textOne, textTwo, emailCon, loginCon, nameCon,userCon,
                    phoneCon, birthCon, passCon,confirmCon, registerCon) = createRefs()

                val startGuideline = createGuidelineFromStart(0.4f)
                val endGuideline = createGuidelineFromEnd(0.4f)
                val topGuideline = createGuidelineFromTop(0.05f)

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
                        .padding(0.dp, 10.dp)
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
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

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
                    onValueChange = { phoneNum = it
                        phoneError = validatePhone(it)},
                    label = { Text("Phone number") },
                    singleLine = true,
                    isError = phoneError != null,
                    modifier = Modifier
                        .width(250.dp)
                        .constrainAs(phoneCon) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(userCon.bottom)
                        }
                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )

                phoneError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                TextField(
                    value = birth,
                    onValueChange = { birth = it },
                    label = { Text("date and birth") },
                    trailingIcon = {
                        IconButton(
                            onClick = { showDatePickerDialog(context) },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.calendar_icon), // Make sure to have this icon
                                contentDescription = "",
                                modifier = Modifier.size(24.dp) // Adjust size as needed
                            )
                        }
                    },
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
                    onValueChange = { password = it
                        val (strength, percent) = evaluatePasswordStrength(it)
                        passwordStrength = strength
                        passwordStrengthPercent = percent
                        },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                            top.linkTo(textOne.bottom)
                        }

                        .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                )

                LinearProgressIndicator(
                    progress = passwordStrengthPercent / 100f,
                    color = passwordStrength.color,
                    modifier = Modifier
                        .constrainAs(progress) {
                            start.linkTo(startGuideline)
                            end.linkTo(endGuideline)
                            top.linkTo(birthCon.bottom)
                        }
                        .padding(32.dp, 0.dp)
                        .fillMaxWidth()
                        .height(8.dp)
                )

                Text(
                    text = passwordStrength.label,
                    color = passwordStrength.color,
                    modifier = Modifier.constrainAs(textOne) {
                        start.linkTo(startGuideline)
                        end.linkTo(endGuideline)
                        top.linkTo(progress.bottom)
                    }
                )

                TextField(
                    value = checkPass,
                    onValueChange = { checkPass = it
                        passError = validatePass(it)},
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = passError != null,
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

                passError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Button(
                    onClick = {
                        if (emailError == null &&
                            passError == null &&
                            phoneError == null &&
                            email.isNotBlank() &&
                            password.isNotBlank() &&
                            userName.isNotBlank() &&
                            birth.isNotBlank() &&
                            name.isNotBlank())
                        {
                        viewModel.register(email, password)
                    } else {
                        showErrorDialog = true // Show dialog if errors exist
                    } },
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
                    title = { Text("Register Failed") },
                    text = { Text("Check it again") },
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