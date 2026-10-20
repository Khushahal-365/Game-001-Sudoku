package com.example.sudoku.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudoku.data.model.GameState
import com.example.sudoku.data.model.KillerCage
import com.example.sudoku.ui.theme.*

@Composable
fun SudokuBoard(
    gameState: GameState,
    highlightSameDigits: Boolean,
    highlightConflicts: Boolean,
    conflictingIndices: Set<Int> = emptySet(),
    onCellClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = gameState.selectedCellIndex
    val selectedRow = if (selectedIndex in 0..80) selectedIndex / 9 else -1
    val selectedCol = if (selectedIndex in 0..80) selectedIndex % 9 else -1
    val selectedBoxRow = if (selectedRow != -1) (selectedRow / 3) * 3 else -1
    val selectedBoxCol = if (selectedCol != -1) (selectedCol / 3) * 3 else -1

    val selectedValue = if (selectedIndex in 0..80) gameState.entries[selectedIndex] else 0

    val isDark = MaterialTheme.colorScheme.background.red < 0.5f

    // Killer Sudoku Cages mapping
    val cages = gameState.puzzle.cages
    val cageOfCell = remember(cages) {
        val map = HashMap<Int, KillerCage>()
        cages.forEach { cage ->
            cage.cellIndices.forEach { idx ->
                map[idx] = cage
            }
        }
        map
    }
    val selectedCage = if (selectedIndex in 0..80) cageOfCell[selectedIndex] else null

    val boardBorderColor = MaterialTheme.colorScheme.outline
    val blockBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.9f)
    val cellBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
    val cageDashedColor = if (isDark) Color(0xFF60A5FA).copy(alpha = 0.7f) else Color(0xFF334155).copy(alpha = 0.85f)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, boardBorderColor, RoundedCornerShape(12.dp))
            .padding(2.dp)
            .testTag("sudoku_board")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            for (row in 0 until 9) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    for (col in 0 until 9) {
                        val index = row * 9 + col
                        val isGiven = gameState.isGiven(index)
                        val value = gameState.entries[index]
                        val solutionValue = gameState.puzzle.solution[index]
                        val isConflict = highlightConflicts && value != 0 && (value != solutionValue || index in conflictingIndices)
                        val isSelected = index == selectedIndex
                        val isSameDigit = highlightSameDigits && selectedValue in 1..9 && value == selectedValue
                        val isInSelectedArea = row == selectedRow || col == selectedCol ||
                                ((row / 3) * 3 == selectedBoxRow && (col / 3) * 3 == selectedBoxCol)
                        val isSameCageAsSelected = selectedCage != null && selectedCage.contains(index)

                        // Border thickness for 3x3 blocks
                        val borderEnd = if (col % 3 == 2 && col < 8) 2.dp else 0.5.dp
                        val borderBottom = if (row % 3 == 2 && row < 8) 2.dp else 0.5.dp

                        // Cell background logic
                        val cellBg = when {
                            isSelected -> MaterialTheme.colorScheme.primaryContainer
                            isConflict -> MaterialTheme.colorScheme.errorContainer
                            isSameDigit -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                            isSameCageAsSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                            isInSelectedArea -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else -> Color.Transparent
                        }

                        val cellTextColor = when {
                            isConflict -> MaterialTheme.colorScheme.error
                            isGiven -> MaterialTheme.colorScheme.onSurface
                            else -> MaterialTheme.colorScheme.primary
                        }

                        val notes = gameState.notes[index]
                        val cellCage = cageOfCell[index]
                        val isCageTopLeft = cellCage != null && cellCage.topLeftCellIndex == index

                        val cellAccessibilityLabel = buildString {
                            append("Row ${row + 1}, Column ${col + 1}. ")
                            if (cellCage != null) {
                                append("Killer cage sum ${cellCage.targetSum}. ")
                            }
                            if (value != 0) {
                                append("Value $value")
                                if (isGiven) append(", starting clue")
                            } else {
                                append("Empty")
                                if (notes.isNotEmpty()) {
                                    append(", notes: ${notes.sorted().joinToString(", ")}")
                                }
                            }
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(cellBg)
                                .drawBehind {
                                    // Draw dashed borders for Killer Sudoku cages
                                    if (cellCage != null) {
                                        val strokeWidthPx = 1.6.dp.toPx()
                                        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(7f, 5f), 0f)
                                        val inset = 2.5.dp.toPx()

                                        val hasTopEdge = row == 0 || cageOfCell[(row - 1) * 9 + col] != cellCage
                                        val hasBottomEdge = row == 8 || cageOfCell[(row + 1) * 9 + col] != cellCage
                                        val hasLeftEdge = col == 0 || cageOfCell[row * 9 + (col - 1)] != cellCage
                                        val hasRightEdge = col == 8 || cageOfCell[row * 9 + (col + 1)] != cellCage

                                        val startX = inset
                                        val endX = size.width - inset
                                        val startY = inset
                                        val endY = size.height - inset

                                        if (hasTopEdge) {
                                            drawLine(
                                                color = cageDashedColor,
                                                start = Offset(startX, startY),
                                                end = Offset(endX, startY),
                                                strokeWidth = strokeWidthPx,
                                                pathEffect = dashEffect
                                            )
                                        }
                                        if (hasBottomEdge) {
                                            drawLine(
                                                color = cageDashedColor,
                                                start = Offset(startX, endY),
                                                end = Offset(endX, endY),
                                                strokeWidth = strokeWidthPx,
                                                pathEffect = dashEffect
                                            )
                                        }
                                        if (hasLeftEdge) {
                                            drawLine(
                                                color = cageDashedColor,
                                                start = Offset(startX, startY),
                                                end = Offset(startX, endY),
                                                strokeWidth = strokeWidthPx,
                                                pathEffect = dashEffect
                                            )
                                        }
                                        if (hasRightEdge) {
                                            drawLine(
                                                color = cageDashedColor,
                                                start = Offset(endX, startY),
                                                end = Offset(endX, endY),
                                                strokeWidth = strokeWidthPx,
                                                pathEffect = dashEffect
                                            )
                                        }
                                    }
                                }
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { onCellClick(index) }
                                )
                                .semantics { contentDescription = cellAccessibilityLabel }
                                .testTag("cell_${row}_${col}")
                        ) {
                            // Killer Sudoku Cage Target Sum label (top-left of cage)
                            if (isCageTopLeft) {
                                Text(
                                    text = cellCage.targetSum.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDark) Color(0xFF93C5FD) else Color(0xFF1E293B),
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(start = 3.dp, top = 2.dp)
                                )
                            }

                            if (value != 0) {
                                Text(
                                    text = value.toString(),
                                    fontSize = 24.sp,
                                    fontWeight = if (isGiven) FontWeight.ExtraBold else FontWeight.Bold,
                                    color = cellTextColor,
                                    textAlign = TextAlign.Center
                                )
                            } else if (notes.isNotEmpty()) {
                                // 3x3 mini grid for pencil notes
                                NotesGrid(notes = notes, isDark = isDark, selectedDigit = selectedValue)
                            }

                            // Block and cell dividers
                            if (borderEnd > 0.5.dp) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .width(borderEnd)
                                        .fillMaxHeight()
                                        .background(blockBorderColor)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .width(0.5.dp)
                                        .fillMaxHeight()
                                        .background(cellBorderColor)
                                )
                            }

                            if (borderBottom > 0.5.dp) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .height(borderBottom)
                                        .fillMaxWidth()
                                        .background(blockBorderColor)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .height(0.5.dp)
                                        .fillMaxWidth()
                                        .background(cellBorderColor)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotesGrid(notes: Set<Int>, isDark: Boolean, selectedDigit: Int = 0) {
    val noteColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
    val matchNoteColor = if (isDark) Color(0xFF60A5FA) else Color(0xFF2563EB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 1.dp, end = 1.dp, top = 8.dp, bottom = 1.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        for (r in 0 until 3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (c in 0 until 3) {
                    val digit = r * 3 + c + 1
                    val hasDigit = notes.contains(digit)
                    val isMatch = hasDigit && selectedDigit == digit && selectedDigit in 1..9

                    Text(
                        text = if (hasDigit) digit.toString() else " ",
                        fontSize = 10.sp,
                        fontWeight = if (isMatch) FontWeight.Black else FontWeight.Bold,
                        color = if (isMatch) matchNoteColor else noteColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(11.dp)
                    )
                }
            }
        }
    }
}
