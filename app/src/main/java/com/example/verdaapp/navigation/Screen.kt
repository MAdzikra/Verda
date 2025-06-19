package com.example.verdaapp.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Course : Screen("course")
    data object DetailCourse : Screen("detail_course/{moduleId}") {
        fun createRoute(moduleId: String) = "detail_course/$moduleId"
    }
    data object DetailArticle : Screen("detail_article/{articleId}") {
        fun createRoute(articleId: String) = "detail_article/$articleId"
    }
    data object Article : Screen("article")
    data object Kuis : Screen("kuis/{moduleId}") {
        fun createRoute(moduleId: String) = "kuis/$moduleId"
    }
    data object Chatbot : Screen("chatbot")
    data object ForgotPassword : Screen("forgot")
    data object ResetPassword : Screen("reset-password?access_token={access_token}") {
        fun createRoute(token: String) = "reset-password?access_token=$token"
    }
}