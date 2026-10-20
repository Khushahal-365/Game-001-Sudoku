package com.example.sudoku.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Game : Screen("game")
    data object Daily : Screen("daily")
    data object Leaderboard : Screen("leaderboard")
    data object Profile : Screen("profile")
    data object Statistics : Screen("statistics")
    data object Achievements : Screen("achievements")
    data object Settings : Screen("settings")
}
