package org.wahyuheriyanto.nutricom.view.navigation

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.screens.ArticleDetailScreen
import org.wahyuheriyanto.nutricom.view.screens.ArticleScreen
import org.wahyuheriyanto.nutricom.view.screens.CardioScreen
import org.wahyuheriyanto.nutricom.view.screens.DataDiriScreen
import org.wahyuheriyanto.nutricom.view.screens.DiabetesScreen
import org.wahyuheriyanto.nutricom.view.screens.HomeScreen
import org.wahyuheriyanto.nutricom.view.screens.NutrisiDetailScreen
import org.wahyuheriyanto.nutricom.view.screens.NutrisiScreen
import org.wahyuheriyanto.nutricom.view.screens.PredictLoadingScreen
import org.wahyuheriyanto.nutricom.view.screens.RecommendationScreen
import org.wahyuheriyanto.nutricom.view.screens.ResultPredictScreen
import org.wahyuheriyanto.nutricom.view.screens.ResultScreen
import org.wahyuheriyanto.nutricom.view.screens.ScreeningScreen
import org.wahyuheriyanto.nutricom.view.factory.DiabetesViewModelFactory
import org.wahyuheriyanto.nutricom.view.screens.NewScreeningScreen
import org.wahyuheriyanto.nutricom.view.screens.ScanScreen
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DiabetesViewModel
import org.wahyuheriyanto.nutricom.viewmodel.FoodViewModel
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
    val items = listOf("home", "skrining", "Nutrisi", "Artikel")
    val icons = listOf(
        R.drawable.home_icon,
        R.drawable.health_icon,
        R.drawable.indicator_icon,
        R.drawable.article_icon
    )
    val labels = listOf("home", "skrining", "Nutrisi", "Artikel")

    BottomNavigation(backgroundColor = Color.White) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEachIndexed { index, route ->
            BottomNavigationItem(
                icon = { Icon(painter = painterResource(id = icons[index]),
                    contentDescription = null
                ,modifier = Modifier.size(35.dp)) },
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
                  viewModelTwo: DataViewModel,
                  viewModelThree : ScanViewModel,
                  navController: NavController,
    onTopBarActionClick: () -> Unit = {},
    onBottomNavItemSelected: (String) -> Unit = {},
            content: @Composable (PaddingValues) -> Unit ={}

) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val loginState : LoginState by viewModel.loginState.collectAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val diabetesViewModel: DiabetesViewModel = viewModel(factory = DiabetesViewModelFactory(context))
    val name by viewModel.fullName.collectAsState()

    val navController = rememberNavController() as NavHostController
        Scaffold(
            topBar = {
                if (currentRoute !in listOf("login", "scanScreen", "result")) { // Hide TopAppBar on login screen
                    TopAppBar(
                        title = { /* Your title */ },
                        backgroundColor = Color.White,
                        actions = {
                            IconButton(onClick = onTopBarActionClick) {
                                Row (horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically){
                                    Icon(
                                        painter = painterResource(id = R.drawable.user_icon),
                                        contentDescription = "",
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    when (loginState) {
                                        is LoginState.Loading -> { }
                                        is LoginState.Success -> {
                                            val names = name
                                            Text(
                                                text = names,
                                                fontFamily = FontFamily(
                                                    Font(
                                                        resId = R.font.inter_medium,
                                                        weight = FontWeight.Medium
                                                    )
                                                ),
                                                color = Color.Black
                                            )
                                        }
                                        else -> { }
                                    }
                                }

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
                    composable("home") { HomeScreen(viewModel = viewModel, viewModelTwo = DataViewModel(),navController) }
                    composable("skrining") { ScreeningScreen(navController,viewModel = viewModel, viewModelTwo = DataViewModel()) }
                    composable("nutrisi") { NutrisiScreen(viewModel = viewModel,navController, viewModelTwo = DataViewModel(), viewModelThree = FoodViewModel()) }
                    composable("artikel") { ArticleScreen(viewModel = viewModel, viewModelTwo = DataViewModel(),navController) }
                    composable("scanScreen") { ScanScreen(navController, viewModelThree) }
                    composable("result") { ResultScreen(navController, viewModelThree, viewModelTwo = FoodViewModel()) }
                    composable("diabetesScreen"){
                        DiabetesScreen(navController)
                    }
                    composable("dataDiri"){ DataDiriScreen(navController = navController) }
                    composable("recommendationList"){ RecommendationScreen(
                        navController = navController,
                        viewModel = viewModel,
                        viewModelTwo = viewModelTwo
                    )
                    }
                    composable("nutrisiDetail"){ NutrisiDetailScreen(
                        navController = navController,
                        viewModel = viewModel,
                        viewModelTwo = DataViewModel()
                    )
                    }
                    composable("newScreening"){ NewScreeningScreen()}
                    composable(
                        route = "predictLoadingScreen/{gender}/{age}/{hypertension}/{heartDisease}/{smokingHistory}/{bmi}/{hbA1c}/{bloodGlucose}",
                        arguments = listOf(
                            navArgument("gender") { type = NavType.IntType },
                            navArgument("age") { type = NavType.IntType },
                            navArgument("hypertension") { type = NavType.IntType },
                            navArgument("heartDisease") { type = NavType.IntType },
                            navArgument("smokingHistory") { type = NavType.IntType },
                            navArgument("bmi") { type = NavType.FloatType },
                            navArgument("hbA1c") { type = NavType.FloatType },
                            navArgument("bloodGlucose") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        PredictLoadingScreen(
                            navController = navController,
                            viewModel = diabetesViewModel,
                            gender = backStackEntry.arguments?.getInt("gender") ?: 0,
                            age = backStackEntry.arguments?.getInt("age") ?: 0,
                            hypertension = backStackEntry.arguments?.getInt("hypertension") ?: 0,
                            heartDisease = backStackEntry.arguments?.getInt("heartDisease") ?: 0,
                            smokingHistory = backStackEntry.arguments?.getInt("smokingHistory") ?: 0,
                            bmi = backStackEntry.arguments?.getFloat("bmi") ?: 0f,
                            hbA1c = backStackEntry.arguments?.getFloat("hbA1c") ?: 0f,
                            bloodGlucose = backStackEntry.arguments?.getInt("bloodGlucose") ?: 0
                        )
                    }


                    composable("cardioScreen"){ CardioScreen() }
                    composable("resultPredictScreen") {
                        ResultPredictScreen(
                            navController = navController,
                            viewModel = diabetesViewModel // Pakai ViewModel yang sama
                        )
                    }
                    composable(
                        "articleDetailScreen/{title}/{author}/{content}/{imageUrl}",
                        arguments = listOf(
                            navArgument("title") { type = NavType.StringType },
                            navArgument("author") { type = NavType.StringType },
                            navArgument("content") { type = NavType.StringType },
                            navArgument("imageUrl") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val title = backStackEntry.arguments?.getString("title") ?: ""
                        val author = backStackEntry.arguments?.getString("author") ?: ""
                        val content = backStackEntry.arguments?.getString("content") ?: ""
                        val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""

                        ArticleDetailScreen(title, author, content, imageUrl)
                    }
                }

            }
        )
}