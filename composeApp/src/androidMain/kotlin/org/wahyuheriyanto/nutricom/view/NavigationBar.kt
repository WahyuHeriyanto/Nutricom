package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.wahyuheriyanto.nutricom.R

@Composable
fun NavigationBar(
    onTopBarActionClick: () -> Unit = {},
    onBottomNavItemSelected: (String) -> Unit = {},
            content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nutricom") },
                actions = {
                    IconButton(onClick = onTopBarActionClick) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(backgroundColor = Color.White
            //Color(android.graphics.Color.parseColor("#CAE8AC")
            ) {
                BottomNavigationItem(

                    icon = { Icon(painter = painterResource(id = R.drawable.home_icon), contentDescription ="" ) },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { onBottomNavItemSelected("home") }
                )
                BottomNavigationItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.running_icon), contentDescription ="" ) },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { onBottomNavItemSelected("settings") }
                )
                BottomNavigationItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.basket_icon), contentDescription = "") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { onBottomNavItemSelected("settings") }
                )
                BottomNavigationItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.globe_icon), contentDescription = "") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { onBottomNavItemSelected("settings") }
                )
            }
        },
        content = {paddingValues ->
            // Menambahkan padding values ke dalam konten
            Box(modifier = Modifier.padding(paddingValues)) {
                content(paddingValues)
            }}
    )
}

@Preview
@Composable

fun NavPreview(){
    Surface (modifier = Modifier.fillMaxSize()){
        NavigationBar {

        }
    }
}