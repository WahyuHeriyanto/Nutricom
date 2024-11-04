package org.wahyuheriyanto.nutricom.view



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.R

@Composable
fun SideDrawer(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header with the logo
        Row {
            Image(
                painter = painterResource(id = R.drawable.nutricom_logo_small),
                contentDescription = "Nutricom Icon",
                modifier = Modifier
                    .size(120.dp)
                    .padding(10.dp,0.dp)
            )

            Text(text = "Nama")
        }


        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "Dashboard",
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp,0.dp))

        // Options buttons
        DrawerOption(
            iconResId = R.drawable.running_icon,
            label = "Option 1",
            onClick = { navController.navigate("option1") }
        )

        DrawerOption(
            iconResId = R.drawable.basket_icon,
            label = "Option 2",
            onClick = { navController.navigate("option2") }
        )

        DrawerOption(
            iconResId = R.drawable.globe_icon,
            label = "Option 3",
            onClick = { navController.navigate("option3") }
        )
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