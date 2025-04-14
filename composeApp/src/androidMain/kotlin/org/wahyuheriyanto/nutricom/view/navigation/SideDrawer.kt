package org.wahyuheriyanto.nutricom.view.navigation


import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.data.DataStoreUtils
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState

fun performingLogout(navController: NavController, context: Context, viewModel: AuthViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()

            val googleSignInClient = GoogleSignIn.getClient(
                context,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            )
            googleSignInClient.signOut().await()

            DataStoreUtils.clearLoginCredentials(context)
            viewModel.setLoginState(LoginState.Idle)
            withContext(Dispatchers.Main) {
                DataStoreUtils.clearLoginCredentials(context)
                viewModel.setLoginState(LoginState.Idle)
                delay(3000) // Beri waktu untuk memastikan DataStore benar-benar terhapus
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        } catch (e: Exception) {
            Log.e("LogoutError", "Logout failed: ${e.message}")
        }
    }
}

@Composable
fun SideDrawer(navController: NavController, viewModel: AuthViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with the logo
        Row(
            modifier = Modifier
                .fillMaxWidth(), // Make the Row fill the entire width
            verticalAlignment = Alignment.CenterVertically // Align items vertically in the center
        ) {
            Image(
                painter = painterResource(id = R.drawable.nutricom_logo_small),
                contentDescription = "Nutricom Icon",
                modifier = Modifier
                    .size(120.dp)
                    .padding(10.dp, 0.dp)
            )

            // Center the text vertically within a Column
            Column(
                modifier = Modifier
                    .padding(start = 8.dp), // Add some spacing between the image and text
                verticalArrangement = Arrangement.Center, // Center items vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally
            ) {
                Text(text = "Username:", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "ID Number : ", fontSize = 16.sp)
            }
        }



        Box(modifier = Modifier
            .background(color = Color.Gray)
            .fillMaxWidth()
            .height(1.dp))
        Spacer(modifier = Modifier.height(10.dp))

        // Options buttons
        DrawerOption(
            iconResId = R.drawable.profile_icon,
            label = "Data Diri Saya",
            onClick = { navController.navigate("dataDiri") }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    performingLogout(navController, context, viewModel)
                })
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.profile_icon),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Logout", fontSize = 16.sp)
        }
    }

}

@Composable
fun DrawerOption(iconResId: Int, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp)
    }
}

// Update the NavHost in the NavigationBar composable


@Preview
@Composable

fun SideDrawerPreview(){
    Surface (modifier = Modifier.fillMaxSize()){

    }
}