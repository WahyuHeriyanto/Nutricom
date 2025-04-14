package org.wahyuheriyanto.nutricom.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.wahyuheriyanto.nutricom.R

@Composable
fun SplashScreen(){
    Box(modifier = Modifier.fillMaxSize()
//        .background(color = Color(android.graphics.Color.parseColor("#00AA16")))
    ){
        ConstraintLayout {
            val logo = createRef()
            val startGuideline = createGuidelineFromStart(0.3f)
            val endGuideline = createGuidelineFromEnd(0.3f)
            val topGuideline = createGuidelineFromTop(0.4f)
            val bottomGuideline = createGuidelineFromBottom(0.4f)
            Image(painter = painterResource(id = R.drawable.logo_nutricom),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
                    .constrainAs(logo){
                        top.linkTo(topGuideline)
                        bottom.linkTo(bottomGuideline)
                        start.linkTo(startGuideline)
                        end.linkTo(endGuideline)
                    }
            )
        }
    }
}
@Preview(name = "Splash Screen View",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true)
@Composable
fun SplashScreenPreview(){
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SplashScreen()
        }
    }
}