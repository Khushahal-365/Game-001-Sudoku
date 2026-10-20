package com.example.sudoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sudoku.ui.achievements.AchievementsScreen
import com.example.sudoku.ui.daily.DailyChallengeScreen
import com.example.sudoku.ui.game.GameScreen
import com.example.sudoku.ui.game.GameViewModel
import com.example.sudoku.ui.home.HomeScreen
import com.example.sudoku.ui.navigation.Screen
import com.example.sudoku.ui.settings.SettingsScreen
import com.example.sudoku.ui.statistics.StatisticsScreen
import com.example.sudoku.ui.theme.SudokuTheme

class MainActivity : ComponentActivity() {

    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settings by gameViewModel.settings.collectAsStateWithLifecycle()

            SudokuTheme(themeColor = settings.themeColor, isDarkMode = settings.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SudokuApp(viewModel = gameViewModel)
                }
            }
        }
    }
}

@Composable
fun SudokuApp(viewModel: GameViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            com.example.sudoku.ui.main.MainScreen(
                viewModel = viewModel,
                onNavigateToGame = { navController.navigate(Screen.Game.route) },
                onNavigateToStats = { navController.navigate(Screen.Statistics.route) },
                onNavigateToAchievements = { navController.navigate(Screen.Achievements.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Game.route) {
            GameScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Daily.route) {
            DailyChallengeScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToGame = { navController.navigate(Screen.Game.route) }
            )
        }

        composable(Screen.Leaderboard.route) {
            com.example.sudoku.ui.leaderboard.LeaderboardScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                showBackButton = true
            )
        }

        composable(Screen.Profile.route) {
            com.example.sudoku.ui.profile.ProfileScreen(
                viewModel = viewModel,
                onNavigateToStats = { navController.navigate(Screen.Statistics.route) },
                onNavigateToAchievements = { navController.navigate(Screen.Achievements.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Achievements.route) {
            AchievementsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
