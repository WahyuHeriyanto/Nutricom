package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun WalletScreen(){

    ConstraintLayout {

        val (title,text,titleTwo,point,topupBox,) = createRefs()

        val startGuideline = createGuidelineFromStart(0.1f)
        val endGuideline = createGuidelineFromEnd(0.1f)
        val topGuideline = createGuidelineFromTop(0.2f)

        Text(text = "My Nutripoints ",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = "Exchange your currency on nutripoints \n" +
                "for better care nutrition's!",
            fontSize = 15.sp
            , modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(title.bottom)
                    start.linkTo(startGuideline)
                }
                .padding(0.dp, 10.dp)
        )
    }

}

@Preview
@Composable

fun WalletPreview(){
    Surface(modifier = Modifier.fillMaxSize()) {
        WalletScreen()
    }
}