package org.wahyuheriyanto.nutricom.view



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import kotlinx.coroutines.delay
import org.wahyuheriyanto.nutricom.R
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

    var shimmerOffset by remember { mutableStateOf(0f) }



    performData(viewModel = viewModel, viewModelTwo = viewModelTwo)
    fetchImageUrls(viewModelTwo = viewModelTwo)



    ConstraintLayout {
        val (title, text, boxCarousel, carousel, indicator, contentBox, bmiBox, caloriesBox, gridBox) = createRefs()

        val topGuideline = createGuidelineFromTop(0.02f)
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

        // Animasi shimmer
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
                CarouselItem(
                    imageRes = imageUrls.getOrElse(activeIndex) { "" },
                    title = "Health Tip #${activeIndex + 1}",
                    description = "Stay active and eat well!",
                    onClick = {}
                )
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
                IndicatorDot(isActive = index == activeIndex)
            }
        }

        //Content Box
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(40.dp))
            .constrainAs(contentBox) {
                top.linkTo(boxCarousel.bottom, margin = -30.dp)
            }
            .background(Color.White)
        )

        //BMI Box Content
        Box(modifier = Modifier
            .width(350.dp)
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
                    .width(280.dp)
                    .height(280.dp)){
                Column {
                    LineChart(width = 150, height = 150)
                    Text(text = "Normal Weight")
                }
                Spacer(modifier = Modifier.width(55.dp))

                Column {
                    Text(text = "Weight")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Height")
                }
            }

        }

        //Calories Box
        Box(modifier = Modifier
            .width(350.dp)
            .constrainAs(caloriesBox) {
                top.linkTo(bmiBox.bottom)
                start.linkTo(startGuideline)
                end.linkTo(endGuideline)
            }
            .padding(top = 20.dp)
            .height(200.dp)
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

        )

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



        )

    }
}

@Composable
fun CarouselItem(imageRes: String, title: String, description: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(android.graphics.Color.parseColor("#DCEFC9")))
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = imageRes,
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

@Preview (heightDp = 1200)
@Composable

fun LongPreview(){
    Surface(modifier = Modifier.fillMaxSize()){
        HomeScreen(viewModel = AuthViewModel(), viewModelTwo = DataViewModel())
    }
}


