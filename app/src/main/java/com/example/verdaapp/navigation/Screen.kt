package com.example.verdaapp.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Course : Screen("course")
    data object DetailCourse : Screen("detail_course/{moduleId}") {
        fun createRoute(moduleId: String) = "detail_course/$moduleId"
    }
    data object Article : Screen("article")
    data object Kuis : Screen("kuis")
    data object Chatbot : Screen("chatbot")
}