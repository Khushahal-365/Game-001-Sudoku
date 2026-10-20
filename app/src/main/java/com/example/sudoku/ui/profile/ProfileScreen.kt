package com.example.sudoku.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sudoku.ui.components.AppIcons
import com.example.sudoku.ui.components.CuteIconContainer
import com.example.sudoku.ui.game.GameViewModel
import com.example.sudoku.ui.theme.components.ThemedBackgroundOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: GameViewModel,
    onNavigateToStats: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playerName by viewModel.playerName.collectAsStateWithLifecycle()
    val playerAvatarIndex by viewModel.playerAvatarIndex.collectAsStateWithLifecycle()
    val coins by viewModel.coins.collectAsStateWithLifecycle()
    val statistics by viewModel.statistics.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val score = remember(statistics, coins) { viewModel.getUserScore() }

    var showRulesDialog by remember { mutableStateOf(false) }

    val emojis = listOf("🦁", "🦊", "🐼", "🦅", "🐺", "🐯", "🦉", "🦄", "🐉", "🐬", "🦋", "🚀")
    val avatarEmoji = emojis.getOrElse(playerAvatarIndex) { "🌟" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CuteIconContainer(
                            icon = AppIcons.Person,
                            contentDescription = null,
                            containerColor = Color(0xFFEDE9FE),
                            iconColor = Color(0xFF7C3AED),
                            size = 36.dp,
                            iconSize = 20.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                        Text(
                            text = "Personal Profile",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable(onClick = onNavigateToSettings)
                            .testTag("profile_settings_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = AppIcons.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ThemedBackgroundOverlay(
                themeColor = settings.themeColor,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            // Player Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(avatarEmoji, fontSize = 42.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = playerName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = if (score > 2500) "Grandmaster Solver" else if (score > 1000) "Master Solver" else "Logic Enthusiast",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Coins and Streak Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ProfileMetric(
                            icon = AppIcons.Coin,
                            iconTint = Color.Unspecified,
                            value = "$coins",
                            label = "Coins Earned"
                        )

                        ProfileMetric(
                            icon = AppIcons.Flame,
                            iconTint = Color(0xFFEF4444),
                            value = "${statistics.currentStreak} 🔥",
                            label = "Streak Days"
                        )

                        ProfileMetric(
                            icon = AppIcons.Trophy,
                            iconTint = Color(0xFF3B82F6),
                            value = "${statistics.totalWon}",
                            label = "Games Won"
                        )
                    }
                }
            }

            // Quick Menu Items with Cute Rounded Icon Squircles
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ProfileMenuCard(
                    title = "My Statistics",
                    subtitle = "Win rates, best times & detailed breakdown",
                    icon = AppIcons.Star,
                    iconTint = Color(0xFFD97706),
                    iconBg = Color(0xFFFEF3C7),
                    onClick = onNavigateToStats,
                    testTag = "profile_menu_stats"
                )

                ProfileMenuCard(
                    title = "Achievements & Badges",
                    subtitle = "Track your solver milestones and rewards",
                    icon = AppIcons.Trophy,
                    iconTint = Color(0xFF9333EA),
                    iconBg = Color(0xFFF3E8FF),
                    onClick = onNavigateToAchievements,
                    testTag = "profile_menu_achievements"
                )

                ProfileMenuCard(
                    title = "How to Play & Rules",
                    subtitle = "Learn Classic Sudoku, Killer Cages, & 45 Rule",
                    icon = AppIcons.Info,
                    iconTint = Color(0xFF2563EB),
                    iconBg = Color(0xFFDBEAFE),
                    onClick = { showRulesDialog = true },
                    testTag = "profile_menu_rules"
                )

                ProfileMenuCard(
                    title = "Settings & Appearance",
                    subtitle = "Themes, haptic vibration, error highlights",
                    icon = AppIcons.Settings,
                    iconTint = Color(0xFF059669),
                    iconBg = Color(0xFFD1FAE5),
                    onClick = onNavigateToSettings,
                    testTag = "profile_menu_settings"
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
    }

    if (showRulesDialog) {
        AlertDialog(
            onDismissRequest = { showRulesDialog = false },
            title = { Text("How to Play", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Classic Sudoku Rules",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "1. Fill the 9x9 grid with numbers 1 to 9.\n2. Each row, column, and 3x3 block must contain every number from 1 to 9 without repeating.",
                        fontSize = 13.sp
                    )

                    HorizontalDivider()

                    Text(
                        text = "Killer Sudoku Rules",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "1. Follow all classic Sudoku rules.\n2. Dotted lines form \"cages\". The numbers inside each cage must sum up to the target number in the top-left.\n3. No digits can repeat within a single cage!\n\n💡 Pro Tip - The 45 Rule: Every 3x3 block sums to 45. Subtracting known cage sums from 45 instantly reveals remaining numbers!",
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showRulesDialog = false }) {
                    Text("Got it!")
                }
            }
        )
    }
}

@Composable
private fun ProfileMetric(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProfileMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CuteIconContainer(
                    icon = icon,
                    contentDescription = null,
                    containerColor = iconBg,
                    iconColor = iconTint,
                    size = 44.dp,
                    iconSize = 22.dp,
                    shape = RoundedCornerShape(14.dp)
                )

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = AppIcons.Forward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
