package com.example.verdaapp

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.verdaapp.navigation.NavigationItem
import com.example.verdaapp.navigation.Screen
import com.example.verdaapp.ui.theme.VerdaAppTheme
import com.example.verdaapp.ui.view.course.CourseScreen
import com.example.verdaapp.ui.view.home.HomeScreen
import com.example.verdaapp.ui.view.login.LoginScreen
import com.example.verdaapp.ui.view.modul.DetailCourseScreen
import com.example.verdaapp.ui.view.register.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VerdaAppTheme {
                var showSplash by remember { mutableStateOf(true) }
                val navController = rememberNavController()

                val appLinkData = intent?.data
                val token = appLinkData?.getQueryParameter("token")

                LaunchedEffect(Unit) {
                    showSplash = false
                    if (!token.isNullOrEmpty()) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                        Toast.makeText(this@MainActivity, "Email berhasil dikonfirmasi, silakan login.", Toast.LENGTH_LONG).show()
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
                    composable(
                        route = Screen.DetailCourse.route,
                        arguments = listOf(navArgument("moduleId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                        DetailCourseScreen(moduleId = moduleId, navController = navController)
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

@Composable
fun VerdaApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
//                HomeScreen()
            }
            composable(Screen.Article.route) {
//                ArticleScreen()
            }
            composable(Screen.Course.route) {
//                CourseScreen()
            }
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}

@Preview
@Composable
private fun VerdaAppPrev() {
    VerdaAppTheme {
        val navController = rememberNavController()
        val modifier = Modifier
        VerdaApp(modifier, navController)
    }
}