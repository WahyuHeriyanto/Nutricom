package org.wahyuheriyanto.nutricom.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import org.wahyuheriyanto.nutricom.viewmodel.performData


@Composable
fun HomeScreen(viewModel: AuthViewModel, viewModelTwo: DataViewModel) {
    val loginState by viewModel.loginState.collectAsState()
    val name by viewModel.userName.collectAsState()
    val wv by viewModelTwo.weight.collectAsState()
    val hv by viewModelTwo.height.collectAsState()
    val cv by viewModelTwo.calorie.collectAsState()
    val bv by viewModelTwo.bmi.collectAsState()

    performData(viewModel = viewModel, viewModelTwo = viewModelTwo)

    ConstraintLayout {
        val (title, text, carousel, indicator, boxindi, bmi) = createRefs()

        val topGuideline = createGuidelineFromTop(0.02f)
        val bottomGuideline = createGuidelineFromBottom(0.1f)
        val startGuideline = createGuidelineFromStart(0.1f)
        val endGuidelines = createGuidelineFromEnd(0.0f)

        val endGuideline = createGuidelineFromEnd(0.1f)

        // Carousel image resources
        val images = listOf(
            R.drawable.green_box_signin,
            R.drawable.green_box_signin,
            R.drawable.green_box_signin,
            R.drawable.green_box_signin,
            R.drawable.green_box_signin
        )

        var activeIndex by remember { mutableStateOf(0) }

        Row(modifier = Modifier.constrainAs(title){
            top.linkTo(topGuideline)
            start.linkTo(startGuideline)

        }) {
            Text(text = "Hello, ",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            when (loginState) {
                is LoginState.Loading -> {

                }
                is LoginState.Success -> {
                    val names = name
                    Text(text = names,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold)
                }
                else -> {
                    // Idle state
                }
            }

        }

        Text(text = "Take care of your health and stay in a \n" +
                "good mood!",
            fontSize = 15.sp
            , modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(title.bottom)
                    start.linkTo(startGuideline)
                }
                .padding(0.dp, 10.dp)
        )

        // Carousel Content
        // Single image carousel
        Box(
            modifier = Modifier
                .constrainAs(carousel) {
                    top.linkTo(text.bottom)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                }
                .width(300.dp)
                .height(180.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        // Detect horizontal drag to change image
                        if (dragAmount < -90) {
                            activeIndex = (activeIndex + 1).coerceAtMost(images.size - 1)
                        } else if (dragAmount > 90) {
                            activeIndex = (activeIndex - 1).coerceAtLeast(0)
                        }
                    }
                }
        ) {
            CarouselItem(
                imageRes = images[activeIndex],
                title = "Health Tip #${activeIndex + 1}",
                description = "Stay active and eat well!",
                onClick = {} // You can specify an action here if needed
            )
        }

        // Indicator for active image
        Row(
            modifier = Modifier
                .constrainAs(indicator) {
                    top.linkTo(carousel.bottom)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                }
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            images.forEachIndexed { index, _ ->
                IndicatorDot(isActive = index == activeIndex)
            }
        }

        //Indicator BMI

        Row (modifier = Modifier.constrainAs(boxindi){
            top.linkTo(indicator.bottom)
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
        }){
            Box(modifier = Modifier
                .padding(10.dp, 10.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(android.graphics.Color.parseColor("#DCEFC9")))
            ){
                when (loginState) {
                    is LoginState.Loading -> {
                    }
                    is LoginState.Success -> {
                        val weights = wv
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Text(text = "$weights",
                                fontSize = 45.sp,
                                fontWeight = FontWeight.Bold,
                                )
                            Text(text = "KG",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                )
                        }

                    }
                    else -> {
                    }
                }

            }
            Box(modifier = Modifier
                .padding(10.dp, 10.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(android.graphics.Color.parseColor("#DCEFC9")))
            ){
                when (loginState) {
                    is LoginState.Loading -> {
                    }
                    is LoginState.Success -> {
                        val heights = hv
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Text(text = "$heights",
                                fontSize = 45.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(text = "CM",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    else -> {
                    }
                }
            }
            Box(modifier = Modifier
                .padding(10.dp, 10.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(android.graphics.Color.parseColor("#DCEFC9")))
            ){
                when (loginState) {
                    is LoginState.Loading -> {
                    }
                    is LoginState.Success -> {
                        val calories = cv
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Text(text = "$calories",
                                fontSize = 45.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(text = "KKal",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    else -> {
                    }
                }
            }
        }
        Box(modifier = Modifier
            .padding(10.dp, 10.dp)
            .size(260.dp, 100.dp)
            .clip(RoundedCornerShape(10.dp))
            .constrainAs(bmi) {
                top.linkTo(boxindi.bottom)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
            }
            .background(Color(android.graphics.Color.parseColor("#C9E4EF")))
        ){
            Row (modifier = Modifier.align(Alignment.CenterStart)){
                Image(painter = painterResource(id = R.drawable.bmi_icon), contentDescription = "",
                    modifier = Modifier.size(130.dp))

//                Spacer(modifier = Modifier.width(10.dp))

                when (loginState) {
                    is LoginState.Loading -> {
                    }
                    is LoginState.Success -> {
                        val bmis = bv
                        Text(text = "$bmis",
                            fontSize = 45.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(20.dp,0.dp)
                        )
                    }
                    else -> {
                    }
                }


            }

        }



    }


}

@Composable
fun CarouselItem(imageRes: Int, title: String, description: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(330.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = description, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun IndicatorDot(isActive: Boolean) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (isActive) Color.Black else Color.Gray)
    )
}

@Preview
@Composable

fun HomePreview(){
    Surface(modifier = Modifier.fillMaxSize()){
        HomeScreen(viewModel = AuthViewModel(), viewModelTwo = DataViewModel())
    }
}

