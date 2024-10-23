package org.wahyuheriyanto.nutricom

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wahyuheriyanto.nutricom.view.SplashScreen
import org.wahyuheriyanto.nutricom.view.LoginScreen
import org.wahyuheriyanto.nutricom.view.MainScreen
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel


import android.content.Intent
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.wahyuheriyanto.nutricom.viewmodel.performGoogleSignIn


class MainActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var viewModel: AuthViewModel // Initialize your ViewModel

    companion object {
        const val REQUEST_CODE_GOOGLE_SIGN_IN = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            SplashScreen()
        }
        Handler().postDelayed({
                setContent {
                    Surface {
                        val viewModel: AuthViewModel = viewModel()
                        MainScreen(viewModel)
                    }
                }
        }, 4000)
    }



}





//@Preview (name = "Android Screen",
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun AppAndroidPreview() {
//    Surface(modifier = Modifier.fillMaxSize()) {
//        App()
//    }
//}