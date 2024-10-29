package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun HomeScreen(
    showNavigation: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { if (showNavigation) NavigationBar {

        } },
        bottomBar = { if (showNavigation) NavigationBar {

        } },
        drawerContent = {
            SideDrawer { selectedItem ->
                // Handle item selection from the side drawer
            }
        },
        content = { paddingValues ->
            content(paddingValues)
        }
    )
}

@Preview
@Composable

fun HomePreview(){
    Surface(modifier = Modifier.fillMaxSize()){
        HomeScreen {

        }
    }
}