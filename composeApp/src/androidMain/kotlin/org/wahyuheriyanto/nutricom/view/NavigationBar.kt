package org.wahyuheriyanto.nutricom.view

import android.app.Application
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.screens.ScanScreen
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.FoodViewModel
import org.wahyuheriyanto.nutricom.viewmodel.HealthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import org.wahyuheriyanto.nutricom.viewmodel.ScanViewModel


private fun navigateToRoute(route: String, navController: NavController) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf("home", "activity", "market", "community")
    val icons = listOf(
        R.drawable.home_icon,
        R.drawable.running_icon,
        R.drawable.basket_icon,
        R.drawable.globe_icon
    )
    val labels = listOf("Home", "Activity", "Market", "Community")

    BottomNavigation(backgroundColor = Color.White) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEachIndexed { index, route ->
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = icons[index]), contentDescription = null) },
                label = { Text(labels[index]) },
                selected = currentRoute == route,
                onClick = { navigateToRoute(route, navController) }
            )
        }
    }
}




@ExperimentalGetImage
@Composable
fun NavigationBar(viewModel: AuthViewModel,
                  viewModelThree : ScanViewModel,
                  navController: NavController,
    onTopBarActionClick: () -> Unit = {},
    onBottomNavItemSelected: (String) -> Unit = {},
            content: @Composable (PaddingValues) -> Unit ={}

) {


    val loginState by viewModel.loginState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navController = rememberNavController() as NavHostController
        Scaffold(
            topBar = {
                if (currentRoute !in listOf("login", "scanScreen", "result")) { // Hide TopAppBar on login screen
                    TopAppBar(
                        title = { /* Your title */ },
                        backgroundColor = Color.White,
                        actions = {
                            IconButton(onClick = onTopBarActionClick) {
                                Icon(
                                    painter = painterResource(id = R.drawable.bell_icon),
                                    contentDescription = "",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    )
                }
            },
            bottomBar = {
                if (currentRoute !in listOf("login", "scanScreen", "result")) {
                    BottomNavigationBar(navController = navController)
                }
            },
            drawerContent = {
                if (currentRoute != "login") { // Hide drawer on login screen
                    SideDrawer(navController = navController,viewModel)
                }
            },
            content = {
                    innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    Modifier.padding(innerPadding)
                ) {
//                composable("login"){ LoginScreen(viewModel = viewModel, onLoginSuccess = { /*TODO*/ }) {}}
                    composable("home") { HomeScreen(viewModel = viewModel, viewModelTwo = DataViewModel()) }
                    composable("activity") { ScreeningScreen(navController,viewModel = viewModel, viewModelTwo = DataViewModel()) }
                    composable("market") { NutrisiScreen(viewModel = viewModel,navController, viewModelTwo = DataViewModel(), viewModelThree = FoodViewModel()) }
                    composable("community") { ArticleScreen(viewModel = viewModel, viewModelTwo = DataViewModel()) }
                    composable("scanScreen") { ScanScreen(navController, viewModelThree) }
                    composable("result") { ResultScreen(navController, viewModelThree, viewModelTwo = FoodViewModel()) }
                    composable("diabetesScreen"){
                        DiabetesScreen()
                    }
                    composable("cardioScreen"){ CardioScreen()}
                    composable("wallet"){ WalletScreen()}
                    composable("schedule"){ ScheduleScreen()
                    }
                }

            }
        )
}