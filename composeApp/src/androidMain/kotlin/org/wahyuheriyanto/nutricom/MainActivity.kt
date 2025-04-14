package org.wahyuheriyanto.nutricom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wahyuheriyanto.nutricom.view.screens.MainScreen
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel


//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            SplashScreen() }
//        Handler().postDelayed({
//                setContent {
//                    Surface {
//                        val viewModel: AuthViewModel = viewModel()
//                        MainScreen(viewModel)
//                    }
//                }
//        }, 4000)
//    }
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                val viewModel: AuthViewModel = viewModel()
                MainScreen(viewModel)
            }
        }
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