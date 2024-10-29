package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SideDrawer(
    onDrawerItemSelected: (String) -> Unit
) {
    ModalDrawer(
        drawerContent = {
            Column {
                Text("Drawer Item 1", modifier = Modifier.clickable { onDrawerItemSelected("item1") })
                Text("Drawer Item 2", modifier = Modifier.clickable { onDrawerItemSelected("item2") })
            }
        },
        content = {}
    )
}