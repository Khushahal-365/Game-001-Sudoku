package com.example.sudoku.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudoku.ui.components.AppIcons
import com.example.sudoku.ui.daily.DailyChallengeScreen
import com.example.sudoku.ui.game.GameViewModel
import com.example.sudoku.ui.home.HomeScreen
import com.example.sudoku.ui.leaderboard.LeaderboardScreen
import com.example.sudoku.ui.profile.ProfileScreen

@Composable
fun MainScreen(
    viewModel: GameViewModel,
    onNavigateToGame: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 12.dp,
                modifier = Modifier.testTag("main_bottom_nav_bar")
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    // Tab 0: Main / Home
                    CuteNavTabItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = AppIcons.Home,
                        label = "Main",
                        activeColor = Color(0xFF2563EB),
                        activeBgColor = Color(0xFFDBEAFE),
                        testTag = "nav_tab_main"
                    )

                    // Tab 1: Daily Challenges
                    CuteNavTabItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = AppIcons.Date,
                        label = "Daily",
                        activeColor = Color(0xFFEA580C),
                        activeBgColor = Color(0xFFFFEDD5),
                        testTag = "nav_tab_daily"
                    )

                    // Tab 2: Leaderboard
                    CuteNavTabItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = AppIcons.Leaderboard,
                        label = "Podium",
                        activeColor = Color(0xFFD97706),
                        activeBgColor = Color(0xFFFEF3C7),
                        testTag = "nav_tab_leaderboard"
                    )

                    // Tab 3: Me / Personal
                    CuteNavTabItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = AppIcons.Person,
                        label = "Me",
                        activeColor = Color(0xFF7C3AED),
                        activeBgColor = Color(0xFFEDE9FE),
                        testTag = "nav_tab_profile"
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToGame = onNavigateToGame,
                    onNavigateToDaily = { selectedTab = 1 },
                    onNavigateToLeaderboard = { selectedTab = 2 },
                    onNavigateToProfile = { selectedTab = 3 }
                )
                1 -> DailyChallengeScreen(
                    viewModel = viewModel,
                    onNavigateBack = { selectedTab = 0 },
                    onNavigateToGame = onNavigateToGame
                )
                2 -> LeaderboardScreen(
                    viewModel = viewModel,
                    onNavigateBack = { selectedTab = 0 },
                    showBackButton = false
                )
                3 -> ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToStats = onNavigateToStats,
                    onNavigateToAchievements = onNavigateToAchievements,
                    onNavigateToSettings = onNavigateToSettings
                )
            }
        }
    }
}

@Composable
private fun RowScope.CuteNavTabItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    activeColor: Color,
    activeBgColor: Color,
    testTag: String
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selected) activeBgColor else Color.Transparent)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(22.dp),
                    tint = if (selected) activeColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        },
        label = {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold,
                color = if (selected) activeColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent
        ),
        modifier = Modifier.testTag(testTag)
    )
}
