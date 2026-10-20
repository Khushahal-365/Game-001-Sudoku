package com.example.sudoku.ui.daily

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudoku.data.model.GameMode
import com.example.sudoku.ui.components.AppIcons
import com.example.sudoku.ui.components.CuteIconContainer
import com.example.sudoku.ui.game.GameViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sudoku.ui.theme.components.ThemedBackgroundOverlay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyChallengeScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    // Mode State: CLASSIC vs KILLER
    var selectedMode by remember { mutableStateOf(GameMode.CLASSIC) }

    // Calendar Navigation State
    val todayCal = remember { Calendar.getInstance() }
    val realTodayYear = remember { todayCal.get(Calendar.YEAR) }
    val realTodayMonth = remember { todayCal.get(Calendar.MONTH) }
    val realTodayDay = remember { todayCal.get(Calendar.DAY_OF_MONTH) }

    var viewCal by remember {
        mutableStateOf(Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) })
    }

    val currentYear = viewCal.get(Calendar.YEAR)
    val currentMonth = viewCal.get(Calendar.MONTH)

    val isViewingCurrentMonth = currentYear == realTodayYear && currentMonth == realTodayMonth

    var selectedDay by remember(currentYear, currentMonth) {
        mutableIntStateOf(
            if (isViewingCurrentMonth) realTodayDay else 1
        )
    }

    // Days in current viewed month
    val daysInMonth = remember(currentYear, currentMonth) {
        viewCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    // First day offset (0 = Monday, ..., 6 = Sunday)
    val firstDayOffset = remember(currentYear, currentMonth) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, currentMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        when (dayOfWeek) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> 0
        }
    }

    val monthName = remember(currentYear, currentMonth) {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.US)
        sdf.format(viewCal.time)
    }

    val completedDates = remember(selectedMode, currentYear, currentMonth) {
        viewModel.getCompletedDailyDates(selectedMode)
    }

    // Count completed trophies in this viewed month
    val completedThisMonth = remember(completedDates, currentYear, currentMonth) {
        val prefix = String.format("%04d-%02d", currentYear, currentMonth + 1)
        completedDates.count { it.startsWith(prefix) }
    }

    val selectedDateKey = remember(currentYear, currentMonth, selectedDay) {
        String.format("%04d-%02d-%02d", currentYear, currentMonth + 1, selectedDay)
    }
    val isSelectedDayCompleted = completedDates.contains(selectedDateKey)

    val isSelectedDayFuture = remember(currentYear, currentMonth, selectedDay, realTodayYear, realTodayMonth, realTodayDay) {
        if (currentYear > realTodayYear) true
        else if (currentYear < realTodayYear) false
        else if (currentMonth > realTodayMonth) true
        else if (currentMonth < realTodayMonth) false
        else selectedDay > realTodayDay
    }

    val canAdvanceMonth = remember(currentYear, currentMonth, realTodayYear, realTodayMonth) {
        (currentYear < realTodayYear) || (currentYear == realTodayYear && currentMonth < realTodayMonth)
    }

    val selectedFullDateText = remember(currentYear, currentMonth, selectedDay) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, currentMonth)
            set(Calendar.DAY_OF_MONTH, selectedDay)
        }
        val sdf = SimpleDateFormat("EEEE, MMM d, yyyy", Locale.US)
        sdf.format(cal.time)
    }

    // Mode Colors
    val isKiller = selectedMode == GameMode.KILLER
    val themeColor = if (isKiller) Color(0xFFEA580C) else Color(0xFF2563EB)
    val themeBgColor = if (isKiller) Color(0xFFFFEDD5) else Color(0xFFDBEAFE)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        ThemedBackgroundOverlay(
            themeColor = settings.themeColor,
            modifier = Modifier.fillMaxSize()
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 720.dp)
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
        ) {
            val isCompact = maxWidth < 540.dp

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Top Header Banner with Trophy Illustration & Mode Selector
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                if (isKiller) listOf(
                                    Color(0xFFC2410C),
                                    Color(0xFFEA580C),
                                    Color(0xFFF97316)
                                ) else listOf(
                                    Color(0xFF1E40AF),
                                    Color(0xFF2563EB),
                                    Color(0xFF3B82F6)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Header Navigation Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CuteIconContainer(
                                icon = AppIcons.Back,
                                contentDescription = "Back",
                                containerColor = Color.White.copy(alpha = 0.2f),
                                iconColor = Color.White,
                                size = 38.dp,
                                iconSize = 20.dp,
                                shape = CircleShape,
                                onClick = onNavigateBack
                            )

                            Text(
                                text = "Daily Challenge Calendar",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )

                            // Placeholder for balance
                            Spacer(modifier = Modifier.size(38.dp))
                        }

                        // Mode Selector Pills inside Banner
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = Color.Black.copy(alpha = 0.2f),
                            modifier = Modifier.testTag("daily_mode_selector")
                        ) {
                            Row(modifier = Modifier.padding(4.dp)) {
                                DailyModePill(
                                    title = "Classic Daily",
                                    icon = AppIcons.Grid,
                                    isSelected = selectedMode == GameMode.CLASSIC,
                                    onClick = { selectedMode = GameMode.CLASSIC }
                                )
                                DailyModePill(
                                    title = "Killer Daily ⚡",
                                    icon = AppIcons.Lightning,
                                    isSelected = selectedMode == GameMode.KILLER,
                                    onClick = { selectedMode = GameMode.KILLER }
                                )
                            }
                        }

                        // Banner Trophy & Monthly Summary
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isKiller) "Killer Sudoku ⚡" else "Classic Sudoku",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$completedThisMonth / $daysInMonth Trophies",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "Earn coins & maintain streak!",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .padding(vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                DailyTrophyIllustration(modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }

                // 2. Interactive Calendar Panel
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Month Navigation Row (< October 2026 > Today)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Previous Month Button
                        CuteIconContainer(
                            icon = AppIcons.Back,
                            contentDescription = "Previous Month",
                            containerColor = Color(0xFFF1F5F9),
                            iconColor = Color(0xFF1E293B),
                            size = 36.dp,
                            iconSize = 18.dp,
                            shape = CircleShape,
                            onClick = {
                                val next = (viewCal.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
                                viewCal = next
                            }
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = monthName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF0F172A)
                            )

                            if (!isViewingCurrentMonth) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = themeBgColor,
                                    modifier = Modifier.clickable {
                                        viewCal = Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) }
                                    }
                                ) {
                                    Text(
                                        text = "Today",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = themeColor,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        // Next Month Button
                        CuteIconContainer(
                            icon = AppIcons.Forward,
                            contentDescription = "Next Month",
                            containerColor = if (canAdvanceMonth) Color(0xFFF1F5F9) else Color(0xFFF1F5F9).copy(alpha = 0.4f),
                            iconColor = if (canAdvanceMonth) Color(0xFF1E293B) else Color(0xFF94A3B8),
                            size = 36.dp,
                            iconSize = 18.dp,
                            shape = CircleShape,
                            onClick = {
                                if (canAdvanceMonth) {
                                    val next = (viewCal.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
                                    viewCal = next
                                }
                            }
                        )
                    }

                    // Days of Week Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        val dayNames = listOf("M", "T", "W", "T", "F", "S", "S")
                        dayNames.forEach { dayName ->
                            Text(
                                text = dayName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(36.dp)
                            )
                        }
                    }

                    // Calendar Days Grid
                    val totalCells = firstDayOffset + daysInMonth
                    val numRows = (totalCells + 6) / 7

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (row in 0 until numRows) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (col in 0 until 7) {
                                    val cellIndex = row * 7 + col
                                    val day = cellIndex - firstDayOffset + 1

                                    if (day in 1..daysInMonth) {
                                        val isSelected = day == selectedDay
                                        val isToday = isViewingCurrentMonth && day == realTodayDay
                                        val isFutureDay = if (currentYear > realTodayYear) true
                                                          else if (currentYear < realTodayYear) false
                                                          else if (currentMonth > realTodayMonth) true
                                                          else if (currentMonth < realTodayMonth) false
                                                          else day > realTodayDay
                                        val dateKey = String.format("%04d-%02d-%02d", currentYear, currentMonth + 1, day)
                                        val isCompleted = completedDates.contains(dateKey)

                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    when {
                                                        isSelected -> if (isFutureDay) Color(0xFF94A3B8) else themeColor
                                                        isCompleted -> Color(0xFFFEF3C7)
                                                        isToday -> Color(0xFFF1F5F9)
                                                        isFutureDay -> Color(0xFFF8FAFC)
                                                        else -> Color.Transparent
                                                    }
                                                )
                                                .border(
                                                    width = if (isToday && !isSelected) 2.dp else 0.dp,
                                                    color = if (isToday && !isSelected) themeColor else Color.Transparent,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .clickable { selectedDay = day }
                                                .testTag("calendar_day_$day"),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = day.toString(),
                                                    fontSize = 14.sp,
                                                    fontWeight = if (isSelected || isToday) FontWeight.Black else FontWeight.Medium,
                                                    color = when {
                                                        isSelected -> Color.White
                                                        isFutureDay -> Color(0xFFCBD5E1)
                                                        isCompleted -> Color(0xFFD97706)
                                                        isToday -> themeColor
                                                        else -> Color(0xFF334155)
                                                    }
                                                )

                                                if (isFutureDay) {
                                                    Icon(
                                                        imageVector = AppIcons.Lock,
                                                        contentDescription = "Locked",
                                                        tint = if (isSelected) Color.White else Color(0xFF94A3B8),
                                                        modifier = Modifier.size(9.dp)
                                                    )
                                                } else if (isCompleted) {
                                                    Icon(
                                                        imageVector = AppIcons.Star,
                                                        contentDescription = null,
                                                        tint = if (isSelected) Color.White else Color(0xFFF59E0B),
                                                        modifier = Modifier.size(10.dp)
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        Box(modifier = Modifier.size(38.dp))
                                    }
                                }
                            }
                        }
                    }

                    // 3. Selected Day Action Card
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = selectedFullDateText,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = if (isSelectedDayFuture) "Locked • Challenge coming soon!"
                                               else if (isKiller) "Killer Sum Cages • Medium Difficulty"
                                               else "Classic 9x9 • Medium Difficulty",
                                        fontSize = 12.sp,
                                        color = if (isSelectedDayFuture) Color(0xFFEF4444) else Color(0xFF64748B)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = when {
                                        isSelectedDayFuture -> Color(0xFFF1F5F9)
                                        isSelectedDayCompleted -> Color(0xFFD1FAE5)
                                        else -> themeBgColor
                                    }
                                ) {
                                    Text(
                                        text = when {
                                            isSelectedDayFuture -> "Locked 🔒"
                                            isSelectedDayCompleted -> "Completed ✓"
                                            else -> "+100 🪙"
                                        },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isSelectedDayFuture -> Color(0xFF64748B)
                                            isSelectedDayCompleted -> Color(0xFF059669)
                                            else -> themeColor
                                        },
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            // Big Play / Replay / Locked Button
                            Button(
                                onClick = {
                                    if (!isSelectedDayFuture) {
                                        viewModel.startDailyChallengeForDate(selectedDateKey, selectedMode)
                                        onNavigateToGame()
                                    }
                                },
                                enabled = !isSelectedDayFuture,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when {
                                        isSelectedDayFuture -> Color(0xFFE2E8F0)
                                        isSelectedDayCompleted -> Color(0xFF10B981)
                                        else -> themeColor
                                    },
                                    disabledContainerColor = Color(0xFFE2E8F0),
                                    contentColor = Color.White,
                                    disabledContentColor = Color(0xFF94A3B8)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("play_daily_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = when {
                                            isSelectedDayFuture -> AppIcons.Lock
                                            isSelectedDayCompleted -> AppIcons.Refresh
                                            else -> AppIcons.Play
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = when {
                                            isSelectedDayFuture -> "Locked (Unlocks $selectedDay $monthName)"
                                            isSelectedDayCompleted -> "Replay Daily Challenge"
                                            else -> "Play Daily Challenge"
                                        },
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyModePill(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color.White else Color.Transparent,
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
                modifier = Modifier.size(15.dp),
                tint = if (isSelected) Color(0xFF0F172A) else Color.White
            )
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                color = if (isSelected) Color(0xFF0F172A) else Color.White
            )
        }
    }
}
