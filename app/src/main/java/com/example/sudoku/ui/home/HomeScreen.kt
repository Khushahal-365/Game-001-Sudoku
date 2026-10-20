package com.example.sudoku.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sudoku.data.model.Difficulty
import com.example.sudoku.data.model.GameMode
import com.example.sudoku.ui.components.AppIcons
import com.example.sudoku.ui.daily.DailyTrophyIllustration
import com.example.sudoku.ui.game.GameViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import com.example.sudoku.ui.theme.components.ThemedBackgroundOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: GameViewModel,
    onNavigateToGame: () -> Unit,
    onNavigateToDaily: () -> Unit,
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val activeGames by viewModel.activeGames.collectAsStateWithLifecycle()
    val statistics by viewModel.statistics.collectAsStateWithLifecycle()
    val coins by viewModel.coins.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    var selectedMode by remember { mutableStateOf(GameMode.CLASSIC) }
    var showDifficultySheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    val activeGameForSelectedMode = activeGames[selectedMode]
    val hasActiveGameForMode = activeGameForSelectedMode != null && !activeGameForSelectedMode.isCompleted
    val isDailyCompleted = viewModel.isTodayDailyCompleted()

    // Date formatting (e.g., "Oct 19")
    val todayFormatted = remember {
        val sdf = SimpleDateFormat("MMM d", Locale.US)
        sdf.timeZone = TimeZone.getDefault()
        sdf.format(Date())
    }

    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        com.example.sudoku.ui.components.CuteIconContainer(
                            icon = AppIcons.Grid,
                            contentDescription = null,
                            containerColor = if (isDark) Color(0xFF1E3A8A) else Color(0xFFDBEAFE),
                            iconColor = if (isDark) Color(0xFF93C5FD) else Color(0xFF2563EB),
                            size = 36.dp,
                            iconSize = 20.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                        Text(
                            text = "Sudoku",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    // Coin Counter Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isDark) Color(0xFF332A15) else Color(0xFFFEF3C7),
                        modifier = Modifier.padding(end = 8.dp).testTag("header_coins_pill")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = AppIcons.Coin,
                                contentDescription = "Coins",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$coins",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isDark) Color(0xFFFDE68A) else Color(0xFF92400E)
                            )
                        }
                    }

                    // Fire Streak Counter Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isDark) Color(0xFF3B1D1D) else Color(0xFFFEE2E2),
                        modifier = Modifier.padding(end = 12.dp).testTag("header_streak_pill")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = AppIcons.Flame,
                                contentDescription = "Streak",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${statistics.currentStreak}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isDark) Color(0xFFFECACA) else Color(0xFF991B1B)
                            )
                        }
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
            // Dynamic themed background watermark overlay based on active theme
            ThemedBackgroundOverlay(themeColor = settings.themeColor, modifier = Modifier.fillMaxSize())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 440.dp)
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 1. Center Branding & Mode Switcher
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = when (selectedMode) {
                                GameMode.KILLER -> "Killer Sudoku"
                                GameMode.TIME_BOMB -> "Time Bomb Sudoku"
                                else -> "Classic Sudoku"
                            },
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            fontSize = 34.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Mode toggle pills
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.padding(vertical = 4.dp).testTag("mode_toggle_pills")
                        ) {
                            Row(modifier = Modifier.padding(4.dp)) {
                                ModePill(
                                    title = "Classic",
                                    icon = AppIcons.Grid,
                                    isSelected = selectedMode == GameMode.CLASSIC,
                                    onClick = { selectedMode = GameMode.CLASSIC }
                                )
                                ModePill(
                                    title = "Killer",
                                    icon = AppIcons.Lightning,
                                    isSelected = selectedMode == GameMode.KILLER,
                                    onClick = { selectedMode = GameMode.KILLER }
                                )
                                ModePill(
                                    title = "Time Bomb",
                                    icon = AppIcons.Bomb,
                                    isSelected = selectedMode == GameMode.TIME_BOMB,
                                    onClick = { selectedMode = GameMode.TIME_BOMB }
                                )
                            }
                        }

                        val modeSub = when (selectedMode) {
                            GameMode.CLASSIC -> "Standard 9x9 grid • Row, column & 3x3 box logic"
                            GameMode.KILLER -> "Dotted sum cages • Combine mental arithmetic & Sudoku rules"
                            GameMode.TIME_BOMB -> "Beat the clock • +10s on correct entry, -30s penalty on mistake"
                        }
                        Text(
                            text = modeSub,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = when (selectedMode) {
                                GameMode.TIME_BOMB -> Color(0xFFEF4444)
                                GameMode.KILLER -> Color(0xFFEA580C)
                                else -> MaterialTheme.colorScheme.primary
                            },
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }

                    // 2. Centered Action Buttons (Continue & New Game)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Continue Button (shown ONLY when an active game exists for the currently selected mode)
                        if (hasActiveGameForMode && activeGameForSelectedMode != null) {
                            val activeState = activeGameForSelectedMode
                            val minutes = activeState.elapsedSeconds / 60
                            val seconds = activeState.elapsedSeconds % 60
                            val isTB = activeState.puzzle.mode == GameMode.TIME_BOMB
                            val modePrefix = when {
                                isTB -> "Time Bomb "
                                activeState.puzzle.mode == GameMode.KILLER -> "Killer "
                                else -> ""
                            }
                            val diffTitle = if (activeState.puzzle.challengeDateKey != null) "Daily" else "$modePrefix${activeState.puzzle.difficulty.displayName}"
                            val speedRating = if (activeState.elapsedSeconds < 180) "Fast" else "Normal"

                            Button(
                                onClick = {
                                    viewModel.resumeGameForMode(selectedMode)
                                    onNavigateToGame()
                                },
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .shadow(8.dp, RoundedCornerShape(18.dp))
                                    .testTag("continue_game_button")
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Continue ${selectedMode.displayName}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Text(
                                        text = if (isTB) "⏱ ${String.format("%02d:%02d", minutes, seconds)} remaining • $diffTitle" else "⏱ ${String.format("%02d:%02d", minutes, seconds)} - $diffTitle • $speedRating",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }

                        // New Game Button with Fire Streak Bonus Badge (+1)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        ) {
                            Button(
                                onClick = { showDifficultySheet = true },
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (hasActiveGameForMode) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
                                    contentColor = if (hasActiveGameForMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("new_game_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = AppIcons.Play,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "New Game",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }

                            // Floating fire badge 🔥+1 on the right corner matching Screenshot 2
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White,
                                shadowElevation = 6.dp,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-8).dp, y = (-10).dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = AppIcons.Flame,
                                        contentDescription = null,
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "+1",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFEF4444)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Difficulty Selection Modal (When user taps "New Game")
    if (showDifficultySheet) {
        DifficultySelectionDialog(
            currentMode = selectedMode,
            onModeChanged = { selectedMode = it },
            onDifficultySelected = { difficulty ->
                showDifficultySheet = false
                viewModel.startNewGame(difficulty, selectedMode)
                onNavigateToGame()
            },
            onDismiss = { showDifficultySheet = false }
        )
    }
}

@Composable
private fun ModePill(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DifficultySelectionDialog(
    currentMode: GameMode,
    onModeChanged: (GameMode) -> Unit,
    onDifficultySelected: (Difficulty) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Select Difficulty",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )

            val isKiller = currentMode == GameMode.KILLER
            val isTimeBomb = currentMode == GameMode.TIME_BOMB

            // Mode indicator pill (reflecting the mode chosen on the Home screen)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = when {
                    isTimeBomb -> Color(0xFFFEE2E2)
                    isKiller -> Color(0xFFFFEDD5)
                    else -> Color(0xFFDBEAFE)
                },
                modifier = Modifier.testTag("modal_selected_mode_badge")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = when {
                            isTimeBomb -> AppIcons.Bomb
                            isKiller -> AppIcons.Lightning
                            else -> AppIcons.Grid
                        },
                        contentDescription = null,
                        tint = when {
                            isTimeBomb -> Color(0xFFEF4444)
                            isKiller -> Color(0xFFEA580C)
                            else -> Color(0xFF2563EB)
                        },
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = when {
                            isTimeBomb -> "Time Bomb Mode"
                            isKiller -> "Killer Sudoku Mode"
                            else -> "Classic Sudoku Mode"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when {
                            isTimeBomb -> Color(0xFFB91C1C)
                            isKiller -> Color(0xFFC2410C)
                            else -> Color(0xFF1D4ED8)
                        }
                    )
                }
            }

            // Easy (Sprout icon)
            DifficultySheetItem(
                title = "Easy",
                description = if (isTimeBomb) "10 min countdown • +10s bonus / -30s penalty" else if (isKiller) "Gentle cages + 14–17 clues" else "Casual, relaxing • 36–40 clues",
                icon = AppIcons.Sprout,
                iconColor = Color(0xFF059669),
                containerColor = Color(0xFFD1FAE5),
                starCount = 1,
                onClick = { onDifficultySelected(Difficulty.EASY) },
                testTag = "difficulty_select_easy"
            )

            // Medium (Target icon)
            DifficultySheetItem(
                title = "Medium",
                description = if (isTimeBomb) "12 min countdown • +10s bonus / -30s penalty" else if (isKiller) "Engaging arithmetic • 8–11 clues" else "Balanced logic • 30–35 clues",
                icon = AppIcons.Target,
                iconColor = Color(0xFF2563EB),
                containerColor = Color(0xFFDBEAFE),
                starCount = 2,
                onClick = { onDifficultySelected(Difficulty.MEDIUM) },
                testTag = "difficulty_select_medium"
            )

            // Hard (Flame icon)
            DifficultySheetItem(
                title = "Hard",
                description = if (isTimeBomb) "15 min countdown • +10s bonus / -30s penalty" else if (isKiller) "Deep cage deductions • 4–6 clues" else "Tactical deduction • 26–29 clues",
                icon = AppIcons.Flame,
                iconColor = Color(0xFFD97706),
                containerColor = Color(0xFFFEF3C7),
                starCount = 3,
                onClick = { onDifficultySelected(Difficulty.HARD) },
                testTag = "difficulty_select_hard"
            )

            // Expert (Crown icon)
            DifficultySheetItem(
                title = "Expert",
                description = if (isTimeBomb) "18 min countdown • +10s bonus / -30s penalty" else if (isKiller) "Pure Killer 45 rule • 0–1 clues" else "Master solver challenge • 22–25 clues",
                icon = AppIcons.Crown,
                iconColor = Color(0xFF9333EA),
                containerColor = Color(0xFFF3E8FF),
                starCount = 4,
                onClick = { onDifficultySelected(Difficulty.EXPERT) },
                testTag = "difficulty_select_expert"
            )
        }
    }
}

@Composable
private fun DifficultySheetItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    containerColor: Color,
    starCount: Int,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                com.example.sudoku.ui.components.CuteIconContainer(
                    icon = icon,
                    contentDescription = null,
                    containerColor = containerColor,
                    iconColor = iconColor,
                    size = 44.dp,
                    iconSize = 22.dp,
                    shape = RoundedCornerShape(14.dp)
                )

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Cute stars row
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                for (i in 1..4) {
                    val filled = i <= starCount
                    Icon(
                        imageVector = AppIcons.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (filled) iconColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SpiderwebBackground(modifier: Modifier = Modifier, isDark: Boolean) {
    val lineColor = if (isDark) Color.White.copy(alpha = 0.03f) else Color(0xFF1E3A8A).copy(alpha = 0.04f)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Concentric web arches originating from top corners
        val center1 = Offset(0f, 0f)
        for (r in 1..8) {
            val radius = r * (w * 0.18f)
            drawCircle(
                color = lineColor,
                center = center1,
                radius = radius,
                style = Stroke(width = 1f)
            )
        }

        val center2 = Offset(w, h)
        for (r in 1..8) {
            val radius = r * (w * 0.18f)
            drawCircle(
                color = lineColor,
                center = center2,
                radius = radius,
                style = Stroke(width = 1f)
            )
        }
    }
}
