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
import org.wahyuheriyanto.nutricom.view.SplashScreen
import org.wahyuheriyanto.nutricom.view.LoginScreen
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen()
        }
        Handler().postDelayed({
                setContent {
                    Surface {
                        LoginScreen(viewModel = AuthViewModel())
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