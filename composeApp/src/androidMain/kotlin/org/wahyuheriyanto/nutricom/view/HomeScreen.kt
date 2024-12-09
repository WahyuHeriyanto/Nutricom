package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.delay
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.widget.CarouselItem
import org.wahyuheriyanto.nutricom.view.widget.IndicatorDot
import org.wahyuheriyanto.nutricom.view.widget.LineChart
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import org.wahyuheriyanto.nutricom.viewmodel.fetchImageUrls
import org.wahyuheriyanto.nutricom.viewmodel.performData

@Composable
fun HomeScreen(viewModel: AuthViewModel, viewModelTwo: DataViewModel) {
    val loginState by viewModel.loginState.collectAsState()
    val imageUrls by viewModelTwo.imageUrls.collectAsState()
    val name by viewModel.userName.collectAsState()
    val wv by viewModelTwo.weight.collectAsState()
    val hv by viewModelTwo.height.collectAsState()
    var shimmerOffset by remember { mutableStateOf(0f) }
    val datas = listOf(18f, 18.5f, 17.8f, 21.2f)
    val dates = listOf("22", "23", "24", "25")

    performData(viewModel = viewModel, viewModelTwo = viewModelTwo)
    fetchImageUrls(viewModelTwo = viewModelTwo)
    // Scroll State
    val scrollState = rememberScrollState()

    //Scroll Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ){
        ConstraintLayout {
            val (title, text, boxCarousel, carousel, indicator, contentBox, bmiBox, caloriesBox, gridBox) = createRefs()

            val topGuideline = createGuidelineFromTop(0.005f)
            val startGuideline = createGuidelineFromStart(0.1f)
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
            val imageCount = imageUrls.size

            // Carousel Auto-Slide with Timer
            LaunchedEffect(imageCount) {
                while (true) {
                    delay(3000L) // Delay 3 detik
                    activeIndex = (activeIndex + 1) % imageCount
                }
            }

            // Animation shimmer
            LaunchedEffect(Unit) {
                while (true) {
                    shimmerOffset += 0.1f
                    if (shimmerOffset >= 1f) shimmerOffset = 0f
                    delay(30L) // Kecepatan animasi
                }
            }

            // Shimmer brush
            val shimmerBrush = Brush.linearGradient(
                colors = listOf(Color.White.copy(alpha = 0.4f), Color.White, Color.White.copy(alpha = 0.4f)),
                start = Offset.Zero,
                end = Offset(x = 300f * shimmerOffset, y = 0f)
            )


            Row(modifier = Modifier.constrainAs(title){
                top.linkTo(topGuideline)
                start.linkTo(startGuideline)

            }) {
                Text(text = "Hello, ",
                    fontSize = 20.sp,
                )
                when (loginState) {
                    is LoginState.Loading -> {

                    }
                    is LoginState.Success -> {
                        val names = name
                        Text(text = names,
                            fontSize = 20.sp,
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

            Box(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(boxCarousel) {
                    top.linkTo(text.bottom)
                }
                .fillMaxWidth()
                .height(250.dp)
                .background(Color(android.graphics.Color.parseColor("#DCEFC9")))
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
                    .width(350.dp)
                    .height(180.dp)
                    .background(Color.Transparent)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (dragAmount.x < -50) {
                                    activeIndex = (activeIndex + 1) % imageCount
                                } else if (dragAmount.x > 50) {
                                    activeIndex = (activeIndex - 1 + imageCount) % imageCount
                                }
                            }
                        )
                    }
            ) {
                if (imageUrls.isNotEmpty()) {
                    CarouselItem(imageRes = imageUrls.getOrElse(activeIndex) { "" },
                        onClick = {})

                } else {
                    Text("Loading images...", modifier = Modifier.align(Alignment.Center))
                }
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
//
                    IndicatorDot(isActive = index == activeIndex)
                }
            }

            //Content Box
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(1500.dp)
                .clip(RoundedCornerShape(40.dp))
                .constrainAs(contentBox) {
                    top.linkTo(boxCarousel.bottom, margin = -40.dp)

                }
                .background(Color.White)
            )

            //BMI Box Content
            Box(modifier = Modifier
                .width(320.dp)
                .constrainAs(bmiBox) {
                    top.linkTo(boxCarousel.bottom)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)


                }
                .height(250.dp)
                .background(Color.White)
                .background(brush = shimmerBrush)
                .clip(RoundedCornerShape(30.dp))
                .border(
                    width = 2.dp, // Ketebalan garis
                    color = Color.Gray.copy(alpha = 0.5f), // Warna garis tipis abu-abu
                    shape = RoundedCornerShape(30.dp) // Bentuk rounded mengikuti box
                )

                .drawBehind {
                    // Custom warna shadow bagian bawah
                    drawRect(
                        color = Color.Gray.copy(alpha = 0.2f), // Warna shadow abu-abu tipis
                        topLeft = Offset(0f, size.height - 8.dp.toPx()), // Posisi shadow di bawah
                        size = Size(size.width, 20.dp.toPx()) // Ketebalan shadow
                    )
                },
                contentAlignment = Alignment.Center

            ){
                Row (horizontalArrangement = Arrangement.Center, // Menempatkan Column di tengah Row
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(300.dp)
                        .height(280.dp)){
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "Body Mass Index (BMI)")
                        Spacer(modifier = Modifier.height(20.dp))
                        LineChart(width = 150, height = 150, data = datas, date = dates)
                        Text(text = "Normal Weight")
                    }
                    Spacer(modifier = Modifier.width(30.dp))

                    Column (horizontalAlignment = AbsoluteAlignment.Left,
                        modifier = Modifier.width(100.dp)) {
                        Text(text = "Weight",
                            fontSize = 20.sp)

                        when (loginState) {
                            is LoginState.Loading -> {
                            }
                            is LoginState.Success -> {
                                val weights = wv
                                Text(text = "$weights",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            else -> {
                            }
                        }

                        Spacer(modifier = Modifier.height(45.dp))
                        Text(text = "Height",
                            fontSize = 20.sp)
                        when (loginState) {
                            is LoginState.Loading -> {
                            }
                            is LoginState.Success -> {
                                val heights = hv
                                Text(text = "$heights",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            else -> {
                            }
                        }
                    }
                }

            }

            //Calories Box
            Box(modifier = Modifier
                .width(320.dp)
                .constrainAs(caloriesBox) {
                    top.linkTo(bmiBox.bottom)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                }
                .padding(top = 20.dp)
                .height(250.dp)
                .background(Color.White)
                .background(brush = shimmerBrush)
                .clip(RoundedCornerShape(30.dp))
                .border(
                    width = 2.dp, // Ketebalan garis
                    color = Color.Gray.copy(alpha = 0.5f), // Warna garis tipis abu-abu
                    shape = RoundedCornerShape(30.dp) // Bentuk rounded mengikuti box
                )

                .drawBehind {
                    // Custom warna shadow bagian bawah
                    drawRect(
                        color = Color.Gray.copy(alpha = 0.2f), // Warna shadow abu-abu tipis
                        topLeft = Offset(0f, size.height - 8.dp.toPx()), // Posisi shadow di bawah
                        size = Size(size.width, 20.dp.toPx()) // Ketebalan shadow
                    )
                }

            ){
                Row (horizontalArrangement = Arrangement.Center, // Menempatkan Column di tengah Row
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(300.dp)
                        .height(280.dp)){
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "Intake Calories")
                        Spacer(modifier = Modifier.height(20.dp))
                        LineChart(width = 120, height = 120, data = datas, date = dates)
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text = "Output Calories")
                        Spacer(modifier = Modifier.height(20.dp))
                        LineChart(width = 120, height = 120, data = datas, date = dates)
                    }
                }
            }

            //Grid Content
            Box(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(gridBox) {
                    top.linkTo(caloriesBox.bottom)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                }
                .padding(top = 40.dp)
                .height(300.dp)
                .background(Color.White)
                .border(
                    width = 2.dp, // Ketebalan garis
                    color = Color.Gray.copy(alpha = 0.5f), // Warna garis tipis abu-abu

                )



            ){

                // Grid Layout
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Row pertama
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(2) { // Dua box di baris pertama
                            Box(
                                modifier = Modifier
                                    .size(120.dp) // Ukuran box
                                    .background(Color.LightGray)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray.copy(alpha = 0.5f), // Warna stroke tipis
                                    )
                            )
                        }
                    }

                    // Row kedua
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(2) { // Dua box di baris kedua
                            Box(
                                modifier = Modifier
                                    .size(120.dp) // Ukuran box
                                    .background(Color.LightGray)
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray.copy(alpha = 0.5f), // Warna stroke tipis
                                    )
                            )
                        }
                    }
                }

            }



        }


    }



}

@Preview
@Composable

fun HomePreview(){
    Surface(modifier = Modifier.fillMaxSize()){
        HomeScreen(viewModel = AuthViewModel(), viewModelTwo = DataViewModel())
    }
}

@Preview (heightDp = 1200)
@Composable

fun LongPreview(){
    Surface(modifier = Modifier.fillMaxSize()){
        HomeScreen(viewModel = AuthViewModel(), viewModelTwo = DataViewModel())
    }
}


