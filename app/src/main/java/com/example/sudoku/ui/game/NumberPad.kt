package com.example.sudoku.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudoku.ui.components.AppIcons

@Composable
fun NumberPad(
    remainingCounts: IntArray,
    isNotesMode: Boolean,
    canUndo: Boolean,
    canRedo: Boolean,
    hintsUsed: Int,
    onDigitClick: (Int) -> Unit,
    onEraseClick: () -> Unit,
    onNotesToggle: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onHintClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cute Toolbar actions with pastel squircle badges
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CuteToolButton(
                icon = AppIcons.Undo,
                label = "Undo",
                bgColor = Color(0xFFE0F2FE),
                iconColor = Color(0xFF0284C7),
                enabled = canUndo,
                onClick = onUndoClick,
                testTag = "undo_button"
            )

            CuteToolButton(
                icon = AppIcons.Redo,
                label = "Redo",
                bgColor = Color(0xFFEDE9FE),
                iconColor = Color(0xFF7C3AED),
                enabled = canRedo,
                onClick = onRedoClick,
                testTag = "redo_button"
            )

            CuteToolButton(
                icon = AppIcons.Erase,
                label = "Erase",
                bgColor = Color(0xFFFFE4E6),
                iconColor = Color(0xFFE11D48),
                enabled = true,
                onClick = onEraseClick,
                testTag = "erase_button"
            )

            CuteNotesToggleButton(
                isNotesMode = isNotesMode,
                onClick = onNotesToggle,
                testTag = "notes_button"
            )

            CuteToolButton(
                icon = AppIcons.Lightbulb,
                label = "Hint ($hintsUsed)",
                bgColor = Color(0xFFFEF9C3),
                iconColor = Color(0xFFCA8A04),
                enabled = true,
                badge = if (hintsUsed > 0) "$hintsUsed" else null,
                onClick = onHintClick,
                testTag = "hint_button"
            )
        }

        // Cute 1-9 Number Pad Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (digit in 1..9) {
                val remaining = remainingCounts[digit]
                val isCompleted = remaining == 0

                val containerColor = if (isCompleted) {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                }

                val textColor = if (isCompleted) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .testTag("digit_button_$digit"),
                    shape = RoundedCornerShape(16.dp),
                    color = containerColor,
                    shadowElevation = if (isCompleted) 0.dp else 2.dp,
                    onClick = { if (!isCompleted) onDigitClick(digit) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = digit.toString(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = textColor
                        )

                        // Cute mini badge for remaining count
                        Surface(
                            shape = CircleShape,
                            color = if (isCompleted) Color(0xFF10B981).copy(alpha = 0.15f) else Color.Transparent,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = if (isCompleted) "✓" else "$remaining",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted) Color(0xFF059669) else textColor.copy(alpha = 0.7f),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CuteToolButton(
    icon: ImageVector,
    label: String,
    bgColor: Color,
    iconColor: Color,
    enabled: Boolean,
    badge: String? = null,
    onClick: () -> Unit,
    testTag: String
) {
    val alpha = if (enabled) 1f else 0.35f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (enabled) bgColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .border(
                    width = 1.dp,
                    color = if (enabled) iconColor.copy(alpha = 0.2f) else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (enabled) iconColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
                modifier = Modifier.size(24.dp)
            )

            if (badge != null && enabled) {
                Surface(
                    shape = CircleShape,
                    color = iconColor,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .size(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CuteNotesToggleButton(
    isNotesMode: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val activeBg = Color(0xFF10B981)
    val activeIcon = Color.White
    val inactiveBg = Color(0xFFFEF3C7)
    val inactiveIcon = Color(0xFFD97706)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isNotesMode) activeBg else inactiveBg)
                .border(
                    width = 1.dp,
                    color = if (isNotesMode) Color(0xFF059669) else inactiveIcon.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = AppIcons.Pencil,
                contentDescription = "Notes",
                tint = if (isNotesMode) activeIcon else inactiveIcon,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isNotesMode) activeBg.copy(alpha = 0.15f) else Color.Transparent
        ) {
            Text(
                text = if (isNotesMode) "Notes ON" else "Notes OFF",
                fontSize = 11.sp,
                color = if (isNotesMode) Color(0xFF059669) else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
