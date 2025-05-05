package com.example.verdaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.emptyPreferences
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.verdaapp.datastore.UserPreferenceKeys.USER_TOKEN
import com.example.verdaapp.datastore.dataStore
import com.example.verdaapp.navigation.NavigationItem
import com.example.verdaapp.navigation.Screen
import com.example.verdaapp.ui.theme.VerdaAppTheme
import com.example.verdaapp.ui.view.artikel.ArticleScreen
import com.example.verdaapp.ui.view.artikel.DetailArticleScreen
import com.example.verdaapp.ui.view.course.CourseScreen
import com.example.verdaapp.ui.view.home.HomeScreen
import com.example.verdaapp.ui.view.login.LoginScreen
import com.example.verdaapp.ui.view.modul.DetailCourseScreen
import com.example.verdaapp.ui.view.register.RegisterScreen
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VerdaAppTheme {
                var showSplash by remember { mutableStateOf(true) }
                val navController = rememberNavController()

//                val appLinkData = intent?.data
//                val token = appLinkData?.getQueryParameter("token")

                LaunchedEffect(Unit) {
                    val token = this@MainActivity.dataStore.data
                        .catch { emit(emptyPreferences()) }
                        .map { it[USER_TOKEN] ?: "" }
                        .first()
//                    val token = context.dataStore.data.map { it[UserPreferencesKeys.USER_TOKEN] ?: "" }.first()
                    showSplash = false
//                    if (!token.isNullOrEmpty()) {
//                        navController.navigate(Screen.Login.route) {
//                            popUpTo(0) { inclusive = true }
//                        }
//                        Toast.makeText(this@MainActivity, "Email berhasil dikonfirmasi, silakan login.", Toast.LENGTH_LONG).show()
//                    }
                    if (token.isNotEmpty()) {
                        // langsung navigate ke HomeScreen
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        // navigate ke Login
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }

                if (showSplash) {
                    SplashScreen()
                } else {
                    LoginScreen(navController)
                }

                NavHost(navController, startDestination = Screen.Login.route) {
                    composable(Screen.Login.route) { LoginScreen(navController) }
                    composable(Screen.Register.route) { RegisterScreen(navController) }
                    composable(Screen.Home.route) { HomeScreen(navController) }
                    composable(Screen.Course.route) { CourseScreen(navController) }
                    composable(Screen.Article.route) { ArticleScreen(navController) }
                    composable(
                        route = Screen.DetailCourse.route,
                        arguments = listOf(navArgument("moduleId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                        DetailCourseScreen(moduleId = moduleId, navController = navController)
                    }
                    composable(
                        route = Screen.DetailArticle.route,
                        arguments = listOf(navArgument("articleId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val articleId = backStackEntry.arguments?.getString("articleId") ?: ""
                        DetailArticleScreen(navController = navController, articleId = articleId)
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF86C37A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_screen),
                contentDescription = "Splash Screen",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = R.drawable.ic_home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(R.string.menu_article),
                icon = R.drawable.ic_article,
                screen = Screen.Article
            ),
            NavigationItem(
                title = stringResource(R.string.menu_module),
                icon = R.drawable.ic_course,
                screen = Screen.Course
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
//                selected = false,
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

//@Composable
//fun VerdaApp(
//    modifier: Modifier = Modifier,
//    navController: NavHostController = rememberNavController(),
//) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//
//    Scaffold(
//        bottomBar = {
//            BottomBar(navController)
//        },
//        modifier = modifier
//    ) { innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = Screen.Home.route,
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            composable(Screen.Home.route) {
//                HomeScreen(navController)
//            }
//            composable(Screen.Article.route) {
//                ArticleScreen(navController)
//            }
//            composable(Screen.Course.route) {
//                CourseScreen(navController)
//            }
//        }
//    }
//}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}