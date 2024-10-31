    package org.wahyuheriyanto.nutricom.view

    import android.util.Log
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.material.Scaffold
    import androidx.compose.material.Surface
    import androidx.compose.material.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.tooling.preview.Preview
    import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
    import org.wahyuheriyanto.nutricom.viewmodel.LoginState


    @Composable
    fun HomeScreen(
        viewModel: AuthViewModel, // Use the passed viewModel here
        showNavigation: Boolean = true,
        content: @Composable (PaddingValues) -> Unit = {}
    ) {
        Scaffold(
            topBar = {
                if (showNavigation) NavigationBar(viewModel = viewModel) {
                    
                } // Use the same viewModel here
            },
            bottomBar = {
                if (showNavigation) NavigationBar(viewModel = viewModel) {
                    
                } // Use the same viewModel here
            },
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
            HomeScreen(viewModel = AuthViewModel()) {

            }
        }
    }