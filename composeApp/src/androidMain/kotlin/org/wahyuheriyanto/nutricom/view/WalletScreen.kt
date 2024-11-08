package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun WalletScreen(){

    ConstraintLayout {

        val (title,text,spacer,titleTwo,point,topupBox,) = createRefs()

        val startGuideline = createGuidelineFromStart(0.1f)
        val endGuideline = createGuidelineFromEnd(0.1f)
        val topGuideline = createGuidelineFromTop(0.2f)

        Text(text = "My Nutripoints ",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(title){
                top.linkTo(topGuideline)
                start.linkTo(startGuideline)

            }
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

        Spacer(modifier = Modifier
            .height(20.dp)
            .constrainAs(spacer) {
                top.linkTo(text.bottom)
            })

        Text(text = "My Balanced ",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(titleTwo){
                top.linkTo(spacer.bottom)
                start.linkTo(startGuideline)

            }
        )

        Box(modifier = Modifier
            .padding(0.dp, 10.dp)
            .size(320.dp, 100.dp)
            .constrainAs(point) {
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
                top.linkTo(titleTwo.bottom)
            }
            .clip(RoundedCornerShape(35.dp))
            .background(Color(android.graphics.Color.parseColor("#00AA16")))
        ){
        }




    }

}

@Preview
@Composable

fun WalletPreview(){
    Surface(modifier = Modifier.fillMaxSize()) {
        WalletScreen()
    }
}