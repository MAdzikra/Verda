package com.example.verdaapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.verdaapp.datastore.UserPreferenceKeys
import com.example.verdaapp.datastore.UserPreferenceKeys.USER_TOKEN
import com.example.verdaapp.datastore.dataStore
import com.example.verdaapp.navigation.NavigationItem
import com.example.verdaapp.navigation.Screen
import com.example.verdaapp.ui.theme.VerdaAppTheme
import com.example.verdaapp.ui.view.artikel.ArticleScreen
import com.example.verdaapp.ui.view.artikel.DetailArticleScreen
import com.example.verdaapp.ui.view.chatbot.ChatbotScreen
import com.example.verdaapp.ui.view.course.CourseScreen
import com.example.verdaapp.ui.view.forgot.ForgotPasswordScreen
import com.example.verdaapp.ui.view.forgot.ResetPasswordScreen
import com.example.verdaapp.ui.view.home.HomeScreen
import com.example.verdaapp.ui.view.kuis.QuizScreen
import com.example.verdaapp.ui.view.login.LoginScreen
import com.example.verdaapp.ui.view.modul.DetailCourseScreen
import com.example.verdaapp.ui.view.register.RegisterScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VerdaAppTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }
                val currentIntent by rememberUpdatedState(newValue = intent)
                LaunchedEffect(currentIntent) {
                    delay(3000)
                    val uri = currentIntent?.data
                    val tokenFromUri = uri?.getQueryParameter("access_token")

                    Log.d("DeepLink", "Intent data: $uri")
                    Log.d("DeepLinkDebug", "Query: ${uri?.query}")
                    Log.d("DeepLinkDebug", "access_token: ${uri?.getQueryParameter("access_token")}")

                    if (tokenFromUri != null) {
                        startDestination = Screen.ResetPassword.createRoute(tokenFromUri)
                        return@LaunchedEffect
                    } else {
                        val token = this@MainActivity.dataStore.data
                            .catch { emit(emptyPreferences()) }
                            .map { it[USER_TOKEN] ?: "" }
                            .firstOrNull() ?: ""

                        Log.d("TokenCheck", "Fetched token from DataStore: '$token'")

                        startDestination = if (token.isNotEmpty()) {
                            Screen.Home.route
                        } else {
                            Screen.Login.route
                        }
                    }
                }

                if (startDestination == null) {
                    SplashScreen()
                } else {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination!!
                    ) {
                        composable(Screen.Login.route) { LoginScreen(navController) }
                        composable(Screen.Register.route) { RegisterScreen(navController) }
                        composable(Screen.Home.route) { HomeScreen(navController) }
                        composable(Screen.Course.route) { CourseScreen(navController) }
                        composable(Screen.Article.route) { ArticleScreen(navController) }
                        composable(Screen.Chatbot.route) { ChatbotScreen(navController) }
                        composable(Screen.ForgotPassword.route) { ForgotPasswordScreen(navController) }

                        composable(route = Screen.Kuis.route,
                            arguments = listOf(navArgument("moduleId") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                            QuizScreen(navController = navController, moduleId = moduleId)
                        }

                        composable(
                            route = Screen.DetailCourse.route,
                            arguments = listOf(navArgument("moduleId") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val moduleId = backStackEntry.arguments?.getString("moduleId") ?: ""
                            val userId = backStackEntry.arguments?.getString("userId") ?: ""
                            DetailCourseScreen(moduleId = moduleId, navController = navController)
                        }

                        composable(
                            route = Screen.DetailArticle.route,
                            arguments = listOf(navArgument("articleId") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val articleId = backStackEntry.arguments?.getString("articleId") ?: ""
                            DetailArticleScreen(
                                navController = navController,
                                articleId = articleId
                            )
                        }

                        composable(
//                            route = Screen.ResetPassword.route,
                            route = "reset-password?access_token={access_token}",
                            arguments = listOf(
                                navArgument("access_token") {
                                    type = NavType.StringType
                                    nullable = true
                                    defaultValue = ""
                                }
                            )
                        ) { backStackEntry ->
                            val uri = intent?.data
                            val tokenUri = uri?.getQueryParameter("access_token") ?: ""
//                            val token = backStackEntry.arguments?.getString("access_token") ?: ""
//                            Log.d("ResetPasswordScreen", "Received token: $token")
                            Log.d("ResetPasswordScreen", "Received token: $tokenUri")
                            ResetPasswordScreen(navController, token = tokenUri)
                        }

//                        composable(
//                            route = Screen.ResetPassword.route,
//                            arguments = listOf(navArgument("token") { defaultValue = "" })
//                        ) { backStackEntry ->
//                            val token = backStackEntry.arguments?.getString("token") ?: ""
//                            ResetPasswordScreen(navController, token)
//                        }

                    }
                }
            }
        }
    }

    suspend fun Context.getTokenFromDataStore(): String {
        return try {
            this.dataStore.data
                .catch { emit(emptyPreferences()) }
                .map { preferences -> preferences[USER_TOKEN] }
                .firstOrNull() ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
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

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}