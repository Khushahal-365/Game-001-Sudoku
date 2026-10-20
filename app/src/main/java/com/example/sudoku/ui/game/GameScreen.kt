package com.example.sudoku.ui.game

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.sudoku.ui.components.AppIcons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sudoku.ui.theme.components.ThemedBackgroundOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val conflictingIndices by viewModel.conflictingIndices.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val isGameOver by viewModel.isGameOver.collectAsStateWithLifecycle()
    val coins by viewModel.coins.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showAbandonDialog by remember { mutableStateOf(false) }

    fun triggerHaptic() {
        if (!settings.vibrateOnInput) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                @Suppress("DEPRECATION")
                val v = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                v?.vibrate(25L)
            }
        } catch (_: Exception) {}
    }

    BackHandler {
        if (gameState?.isCompleted == false) {
            showAbandonDialog = true
        } else {
            onNavigateBack()
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag("game_loading_indicator")
                )
                Text(
                    text = "Generating unique puzzle...",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        return
    }

    val state = gameState
    if (state == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("No active game found", style = MaterialTheme.typography.titleLarge)
                Button(onClick = onNavigateBack) {
                    Text("Return Home")
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val isKiller = state.puzzle.mode == com.example.sudoku.data.model.GameMode.KILLER
                        val isTimeBomb = state.puzzle.mode == com.example.sudoku.data.model.GameMode.TIME_BOMB
                        val isDaily = state.puzzle.challengeDateKey != null
                        val (diffIcon, diffIconColor, diffBgColor) = when {
                            isDaily -> Triple(AppIcons.Date, Color(0xFFD97706), Color(0xFFFEF3C7))
                            isTimeBomb -> Triple(AppIcons.Bomb, Color(0xFFEF4444), Color(0xFFFEE2E2))
                            isKiller -> Triple(AppIcons.Lightning, Color(0xFFEA580C), Color(0xFFFFEDD5))
                            state.puzzle.difficulty == com.example.sudoku.data.model.Difficulty.EASY -> Triple(AppIcons.Sprout, Color(0xFF059669), Color(0xFFD1FAE5))
                            state.puzzle.difficulty == com.example.sudoku.data.model.Difficulty.MEDIUM -> Triple(AppIcons.Target, Color(0xFF2563EB), Color(0xFFDBEAFE))
                            state.puzzle.difficulty == com.example.sudoku.data.model.Difficulty.HARD -> Triple(AppIcons.Flame, Color(0xFFD97706), Color(0xFFFEF3C7))
                            else -> Triple(AppIcons.Crown, Color(0xFF9333EA), Color(0xFFF3E8FF))
                        }

                        val modePrefix = when {
                            isTimeBomb -> "Time Bomb "
                            isKiller -> "Killer "
                            else -> ""
                        }
                        val label = if (isDaily) "Daily Challenge" else "$modePrefix${state.puzzle.difficulty.displayName}"

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = diffBgColor
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = diffIcon,
                                    contentDescription = null,
                                    tint = diffIconColor,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = diffIconColor
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable {
                                if (!state.isCompleted) {
                                    showAbandonDialog = true
                                } else {
                                    onNavigateBack()
                                }
                            }
                            .testTag("back_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = AppIcons.Back,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    if (settings.showTimer) {
                        val isTB = state.puzzle.mode == com.example.sudoku.data.model.GameMode.TIME_BOMB
                        val isWarning = isTB && state.elapsedSeconds <= 60L
                        val timerBgColor = when {
                            isWarning -> Color(0xFFEF4444)
                            isTB -> Color(0xFFFEF3C7)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        }
                        val timerTextColor = when {
                            isWarning -> Color.White
                            isTB -> Color(0xFF92400E)
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                        val timerIconColor = when {
                            isWarning -> Color.White
                            isTB -> Color(0xFFD97706)
                            else -> MaterialTheme.colorScheme.primary
                        }

                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = timerBgColor,
                            onClick = {
                                if (state.isPaused) viewModel.resumeGame() else viewModel.pauseGame()
                            },
                            modifier = Modifier.testTag("timer_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = if (isTB) AppIcons.Bomb else AppIcons.Clock,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp),
                                    tint = timerIconColor
                                )
                                Text(
                                    text = formatTime(state.elapsedSeconds),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = timerTextColor
                                )
                                Icon(
                                    imageVector = if (state.isPaused) AppIcons.Play else AppIcons.Pause,
                                    contentDescription = if (state.isPaused) "Resume" else "Pause",
                                    modifier = Modifier.size(14.dp),
                                    tint = timerTextColor
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp, start = 4.dp)
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable(onClick = onNavigateToSettings)
                            .testTag("settings_button"),
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
                    .padding(horizontal = 12.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
            // Mistakes & Cage status bar
            val cageStatus = if (state.puzzle.mode == com.example.sudoku.data.model.GameMode.KILLER) viewModel.getSelectedCageStatus() else null

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (settings.highlightConflicts) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (state.mistakes > 0) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.testTag("mistakes_counter")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = AppIcons.Heart,
                                contentDescription = null,
                                tint = if (state.mistakes > 0) MaterialTheme.colorScheme.error else Color(0xFFEF4444),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Mistakes: ${state.mistakes}/3",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.mistakes > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                if (cageStatus != null) {
                    val (badgeText, badgeBg, badgeTextColor) = when (cageStatus) {
                        is com.example.sudoku.domain.engine.CageStatus.DuplicateDigits -> Triple(
                            "Duplicate in cage!",
                            MaterialTheme.colorScheme.errorContainer,
                            MaterialTheme.colorScheme.error
                        )
                        is com.example.sudoku.domain.engine.CageStatus.SumExceeded -> Triple(
                            "Sum ${cageStatus.currentSum} > ${cageStatus.targetSum}",
                            MaterialTheme.colorScheme.errorContainer,
                            MaterialTheme.colorScheme.error
                        )
                        is com.example.sudoku.domain.engine.CageStatus.WrongSumWhenFull -> Triple(
                            "Sum ${cageStatus.currentSum} != ${cageStatus.targetSum}",
                            MaterialTheme.colorScheme.errorContainer,
                            MaterialTheme.colorScheme.error
                        )
                        is com.example.sudoku.domain.engine.CageStatus.ImpossibleRemainingSum -> Triple(
                            "Sum ${cageStatus.remainingSum} impossible",
                            MaterialTheme.colorScheme.errorContainer,
                            MaterialTheme.colorScheme.error
                        )
                        is com.example.sudoku.domain.engine.CageStatus.Valid -> if (cageStatus.isFull) {
                            Triple(
                                "✓ Cage Sum: ${cageStatus.targetSum} / ${cageStatus.targetSum}",
                                Color(0xFFD1FAE5),
                                Color(0xFF065F46)
                            )
                        } else {
                            Triple(
                                "Cage Sum: ${cageStatus.currentSum} / ${cageStatus.targetSum}",
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = badgeBg
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = AppIcons.Lightning,
                                contentDescription = null,
                                tint = badgeTextColor,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = badgeText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeTextColor
                            )
                        }
                    }
                } else if (state.puzzle.challengeDateKey != null) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = AppIcons.Date,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = state.puzzle.challengeDateKey,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Sudoku 9x9 Board
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp),
                contentAlignment = Alignment.Center
            ) {
                SudokuBoard(
                    gameState = state,
                    highlightSameDigits = settings.highlightSameDigits,
                    highlightConflicts = settings.highlightConflicts,
                    conflictingIndices = conflictingIndices,
                    onCellClick = { index ->
                        triggerHaptic()
                        viewModel.selectCell(index)
                    }
                )

                // Pause Cover
                if (state.isPaused) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Game Paused",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Button(
                                onClick = { viewModel.resumeGame() },
                                modifier = Modifier.testTag("resume_button")
                            ) {
                                Icon(AppIcons.Play, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Resume")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // NumberPad & Action Toolbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
            ) {
                NumberPad(
                    remainingCounts = viewModel.getRemainingCounts(),
                    isNotesMode = state.isNotesMode,
                    canUndo = state.undoStack.isNotEmpty(),
                    canRedo = state.redoStack.isNotEmpty(),
                    hintsUsed = state.hintsUsed,
                    onDigitClick = { digit ->
                        triggerHaptic()
                        viewModel.inputDigit(digit)
                    },
                    onEraseClick = {
                        triggerHaptic()
                        viewModel.eraseCell()
                    },
                    onNotesToggle = {
                        triggerHaptic()
                        viewModel.toggleNotesMode()
                    },
                    onUndoClick = {
                        triggerHaptic()
                        viewModel.undo()
                    },
                    onRedoClick = {
                        triggerHaptic()
                        viewModel.redo()
                    },
                    onHintClick = {
                        triggerHaptic()
                        viewModel.useHint()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

    // Abandon Confirmation Dialog
    if (showAbandonDialog) {
        AlertDialog(
            onDismissRequest = { showAbandonDialog = false },
            title = { Text("Leave Game?") },
            text = { Text("Your progress is automatically saved. You can continue later from the Home screen.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAbandonDialog = false
                        onNavigateBack()
                    },
                    modifier = Modifier.testTag("confirm_leave_button")
                ) {
                    Text("Save & Exit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAbandonDialog = false }) {
                    Text("Stay")
                }
            }
        )
    }

    // Victory Celebration Dialog
    if (state.isCompleted) {
        Dialog(onDismissRequest = { /* stay on victory until user acts */ }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("victory_dialog")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(36.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = AppIcons.Trophy,
                            contentDescription = "Victory",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "Puzzle Solved!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Difficulty", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = if (state.puzzle.challengeDateKey != null) "Daily Challenge" else state.puzzle.difficulty.displayName,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Time", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = formatTime(state.elapsedSeconds),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Mistakes", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = state.mistakes.toString(),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Hints Used", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = state.hintsUsed.toString(),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.restartGame()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(AppIcons.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Replay")
                        }

                        Button(
                            onClick = {
                                viewModel.startNewGame(state.puzzle.difficulty)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("New Game")
                        }
                    }

                    TextButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back to Home")
                    }
                }
            }
        }
    }
    // Time Bomb Game Over Dialog
    if (isGameOver) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("time_bomb_game_over_dialog")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEE2E2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = AppIcons.Bomb,
                            contentDescription = "Game Over",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "BOOM! Time Expired 💣💥",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFB91C1C),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "The bomb countdown reached zero before you finished the puzzle!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.buyTimeBombExtraTime() },
                            enabled = coins >= 50,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2563EB),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(AppIcons.Clock, contentDescription = null, modifier = Modifier.size(18.dp))
                                Text("+3 Minutes Extra Time (50 🪙)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.dismissGameOver()
                                viewModel.restartGame()
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(AppIcons.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                                Text("Restart Game", fontWeight = FontWeight.Bold)
                            }
                        }

                        TextButton(
                            onClick = {
                                viewModel.dismissGameOver()
                                viewModel.abandonGame()
                                onNavigateBack()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back to Home", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}


private fun formatTime(totalSeconds: Long): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
