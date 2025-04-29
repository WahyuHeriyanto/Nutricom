package org.wahyuheriyanto.nutricom.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import android.util.Log
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.data.model.UserItem

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

@OptIn(ExperimentalMaterialApi::class)
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
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf("") }
    var genderExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isChecked by remember { mutableStateOf(false) }

    // GoogleSignInOptions setup
    val googleSignInOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)) // Web client ID from Firebase
            .requestEmail()
            .build()
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
                showSuccessDialog = true
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
            input != password  -> "Kata sandi yang dimasukan tidak sama"
            else -> null
        }
    }

    fun validatePhone(input: String): String? {
        return when {
            input.any { !it.isDigit() } -> "Input harus berupa angka"
            input.length < 10 -> "Minimal 10 digit angka"
            else -> null
        }
    }

    Image(painter = painterResource(R.drawable.background_login),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(android.graphics.Color.parseColor("#F4F4F4")))
    ) {

        ConstraintLayout {

            val (title, loginBox) = createRefs()

            val backStartGuideline = createGuidelineFromStart(0.2f)
            val backTopGuideline = createGuidelineFromTop(0.03f)
            val backEndGuideline = createGuidelineFromEnd(0.2f)


            Text(text = "DAFTAR", fontSize = 30.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(title) {
                    start.linkTo(backStartGuideline)
                    top.linkTo(backTopGuideline, margin = 10.dp)
                    end.linkTo(backEndGuideline)

                }
            )

            Box(
                modifier = Modifier
                    .padding(0.dp, 10.dp)
                    .size(320.dp, 730.dp)
                    .constrainAs(loginBox) {
                        start.linkTo(backStartGuideline)
                        end.linkTo(backEndGuideline)
                        top.linkTo(title.bottom, margin = 10.dp)
                    }
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(android.graphics.Color.parseColor("#00AA16")))
            ) {
                ConstraintLayout {
                    val (progress, textOne, emailCon, loginCon, nameCon, userCon,
                        phoneCon, birthCon, genderCon, passCon, confirmCon,emailWarning, warningMes, passConfirmWarning) = createRefs()
                    val checkBoxRef = createRef()
                    val startGuideline = createGuidelineFromStart(0.4f)
                    val endGuideline = createGuidelineFromEnd(0.4f)
                    val topGuideline = createGuidelineFromTop(0.05f)

                    TextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = validateEmail(it)
                        },
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
                            modifier = Modifier.constrainAs(emailWarning) {
                                start.linkTo(emailCon.start)
                                bottom.linkTo(emailCon.top)
                            }
                        )
                    }

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama Lengkap") },
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
                            .constrainAs(userCon) {
                                start.linkTo(startGuideline)
                                end.linkTo(endGuideline)
                                top.linkTo(nameCon.bottom, margin = 10.dp)
                            }
                            .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                    )

                    TextField(
                        value = phoneNum,
                        onValueChange = {
                            phoneNum = it
                            phoneError = validatePhone(it)
                        },
                        label = { Text("Nomer Telepon") },
                        singleLine = true,
                        isError = phoneError != null,
                        modifier = Modifier
                            .width(250.dp)
                            .constrainAs(phoneCon) {
                                start.linkTo(startGuideline)
                                end.linkTo(endGuideline)
                                top.linkTo(if (phoneError?.isNotEmpty() == true) warningMes.bottom else userCon.bottom, margin = 10.dp)
                            }
                            .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                    )

                    phoneError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.constrainAs(warningMes) {
                                top.linkTo(userCon.bottom, margin = 10.dp)
                                start.linkTo(emailCon.start)
                            }
                        )
                    }

                    // Gender Dropdown
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = !genderExpanded },
                        modifier = Modifier
                            .width(250.dp)
                            .padding(0.dp, 10.dp)
                            .constrainAs(genderCon) {
                                start.linkTo(startGuideline)
                                end.linkTo(endGuideline)
                                top.linkTo(phoneCon.bottom)
                            }
                            .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                    ) {
                        TextField(
                            value = gender,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Jenis Kelamin") },
                            trailingIcon = {
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            listOf("Laki-laki", "Perempuan").forEach { selection ->
                                DropdownMenuItem(
                                    onClick = {
                                        gender = selection
                                        genderExpanded = false
                                    }
                                ) {
                                    Text(selection)
                                }
                            }
                        }
                    }



                    TextField(
                        value = birth,
                        onValueChange = { birth = it },
                        label = { Text("Tanggal Lahir") },
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
//                        .padding(0.dp, 10.dp)
                            .constrainAs(birthCon) {
                                start.linkTo(startGuideline)
                                end.linkTo(endGuideline)
                                top.linkTo(genderCon.bottom)
                            }
                            .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                    )


                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
                            val (strength, percent) = evaluatePasswordStrength(it)
                            passwordStrength = strength
                            passwordStrengthPercent = percent
                        },
                        label = { Text("Kata Sandi") },
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
                                top.linkTo(if (password.isNotEmpty()) textOne.bottom else birthCon.bottom, margin = 10.dp)
                            }

                            .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                    )
                    if (password.isNotEmpty()) {
                        LinearProgressIndicator(
                            progress = passwordStrengthPercent / 100f,
                            color = passwordStrength.color,
                            modifier = Modifier
                                .constrainAs(progress) {
                                    start.linkTo(startGuideline)
                                    end.linkTo(endGuideline)
                                    top.linkTo(birthCon.bottom)
                                }
                                .padding(32.dp, 10.dp, 32.dp, 10.dp)
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
                                .padding(0.dp, 5.dp)
                        )
                    }



                    TextField(
                        value = checkPass,
                        onValueChange = {
                            checkPass = it
                            passError = validatePass(it)
                        },
                        label = { Text("Konfirmasi Kata Sandi") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = passError != null,
                        modifier = Modifier
                            .width(250.dp)
                            .constrainAs(confirmCon) {
                                start.linkTo(startGuideline)
                                end.linkTo(endGuideline)
                                top.linkTo(if (passError?.isNotEmpty() == true) passConfirmWarning.bottom else passCon.bottom, margin = 10.dp)
                            }
                            .background(Color(android.graphics.Color.parseColor("#FFFFFF")))
                    )

                    passError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.constrainAs(passConfirmWarning) {
                                top.linkTo(passCon.bottom, margin = 10.dp)
                                start.linkTo(emailCon.start)
                            }
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .constrainAs(checkBoxRef) {
                                top.linkTo(confirmCon.bottom, margin = 10.dp)
                                start.linkTo(emailCon.start)
                                end.linkTo(emailCon.end)
                            }
//                            .padding(horizontal = 30.dp)
                            .padding(30.dp,0.dp, 40.dp, 0.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { isChecked = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Dengan mencentang kotak ini, Anda menyetujui Syarat dan Ketentuan serta Kebijakan Privasi kami.",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Justify,
                            color = Color.White,
                            softWrap = true,
                            maxLines = Int.MAX_VALUE,
                            modifier = Modifier.weight(1f)
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
                                name.isNotBlank()
                            ) {
                                viewModel.register(
                                    UserItem(
                                        email,
                                        password,
                                        name,
                                        userName,
                                        phoneNum,
                                        gender,
                                        birth
                                    )
                                )
                                Log.e("CekDaftar","lewat")
                            } else {
                                showErrorDialog = true
                            }
                        },
                        modifier = Modifier
                            .constrainAs(loginCon) {
                                start.linkTo(startGuideline)
                                end.linkTo(endGuideline)
                                top.linkTo(checkBoxRef.bottom)
                            }
                            .padding(10.dp)
                            .size(120.dp, 40.dp)
                            .background(Color.Transparent),
                        colors = ButtonDefaults.buttonColors(
                            Color(
                                android.graphics.Color.parseColor(
                                    "#CAE8AC"
                                )
                            )
                        )
                    ) {
                        Text(
                            "Daftar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.background(color = Color.Transparent)
                        )
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

                // Tampilan AlertDialog untuk error register
                if (showErrorDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showErrorDialog = false
                        },
                        title = { Text("Pendaftaran akun gagal") },
                        text = { Text("Silakan cek kembali data dan koneksi internet") },
                        confirmButton = {
                            Button(onClick = {
                                showErrorDialog = false
                            }) {
                                Text("OK")
                            }
                        }
                    )
                }

                if (showSuccessDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showSuccessDialog = false
                        },
                        title = { Text("Pendaftaran akun berhasil") },
                        text = { Text("Silakan untuk kembali masuk") },
                        confirmButton = {
                            Button(onClick = {
                                showSuccessDialog = false
                            }) {
                                Text("OK")
                            }
                        }
                    )
                }
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