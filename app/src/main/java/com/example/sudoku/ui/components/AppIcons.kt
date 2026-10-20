package com.example.sudoku.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Super Cute & Rounded Open Source Icon Collection.
 * Designed with bubbly proportions, extra-rounded terminals, soft curves,
 * and adorable, friendly geometry.
 */
object AppIcons {

    // --- Navigation & Direction ---

    val Back: ImageVector = ImageVector.Builder(
        name = "CuteBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(19f, 10.5f)
            horizontalLineTo(7.5f)
            lineTo(11.2f, 6.8f)
            curveTo(11.6f, 6.4f, 11.6f, 5.7f, 11.2f, 5.3f)
            curveTo(10.8f, 4.9f, 10.1f, 4.9f, 9.7f, 5.3f)
            lineTo(4.3f, 10.7f)
            curveTo(3.7f, 11.3f, 3.7f, 12.3f, 4.3f, 12.9f)
            lineTo(9.7f, 18.3f)
            curveTo(10.1f, 18.7f, 10.8f, 18.7f, 11.2f, 18.3f)
            curveTo(11.6f, 17.9f, 11.6f, 17.2f, 11.2f, 16.8f)
            lineTo(7.5f, 13.5f)
            horizontalLineTo(19f)
            curveTo(19.8f, 13.5f, 20.5f, 12.8f, 20.5f, 12f)
            curveTo(20.5f, 11.2f, 19.8f, 10.5f, 19f, 10.5f)
            close()
        }
    }.build()

    val Forward: ImageVector = ImageVector.Builder(
        name = "CuteForward",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(5f, 13.5f)
            horizontalLineTo(16.5f)
            lineTo(12.8f, 17.2f)
            curveTo(12.4f, 17.6f, 12.4f, 18.3f, 12.8f, 18.7f)
            curveTo(13.2f, 19.1f, 13.9f, 19.1f, 14.3f, 18.7f)
            lineTo(19.7f, 13.3f)
            curveTo(20.3f, 12.7f, 20.3f, 11.7f, 19.7f, 11.1f)
            lineTo(14.3f, 5.7f)
            curveTo(13.9f, 5.3f, 13.2f, 5.3f, 12.8f, 5.7f)
            curveTo(12.4f, 6.1f, 12.4f, 6.8f, 12.8f, 7.2f)
            lineTo(16.5f, 10.5f)
            horizontalLineTo(5f)
            curveTo(4.2f, 10.5f, 3.5f, 11.2f, 3.5f, 12f)
            curveTo(3.5f, 12.8f, 4.2f, 13.5f, 5f, 13.5f)
            close()
        }
    }.build()

    // --- Cute Game Toolbar Actions ---

    val Undo: ImageVector = ImageVector.Builder(
        name = "CuteUndo",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12.5f, 7.5f)
            curveTo(9.3f, 7.5f, 6.5f, 8.8f, 4.8f, 10.9f)
            lineTo(3.7f, 9.8f)
            curveTo(3f, 9.1f, 1.8f, 9.6f, 1.8f, 10.6f)
            verticalLineTo(15.5f)
            curveTo(1.8f, 16.3f, 2.5f, 17f, 3.3f, 17f)
            horizontalLineTo(8.2f)
            curveTo(9.2f, 17f, 9.7f, 15.8f, 9f, 15.1f)
            lineTo(7.3f, 13.4f)
            curveTo(8.6f, 11.9f, 10.4f, 11f, 12.5f, 11f)
            curveTo(15.8f, 11f, 18.5f, 13.7f, 18.5f, 17f)
            curveTo(18.5f, 17.8f, 19.2f, 18.5f, 20f, 18.5f)
            curveTo(20.8f, 18.5f, 21.5f, 17.8f, 21.5f, 17f)
            curveTo(21.5f, 12f, 17.5f, 7.5f, 12.5f, 7.5f)
            close()
        }
    }.build()

    val Redo: ImageVector = ImageVector.Builder(
        name = "CuteRedo",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(11.5f, 7.5f)
            curveTo(16.5f, 7.5f, 20.5f, 12f, 20.5f, 17f)
            curveTo(20.5f, 17.8f, 19.8f, 18.5f, 19f, 18.5f)
            curveTo(18.2f, 18.5f, 17.5f, 17.8f, 17.5f, 17f)
            curveTo(17.5f, 13.7f, 14.8f, 11f, 11.5f, 11f)
            curveTo(9.4f, 11f, 7.6f, 11.9f, 6.3f, 13.4f)
            lineTo(4.6f, 15.1f)
            curveTo(3.9f, 15.8f, 4.4f, 17f, 5.4f, 17f)
            horizontalLineTo(10.3f)
            curveTo(11.1f, 17f, 11.8f, 16.3f, 11.8f, 15.5f)
            verticalLineTo(10.6f)
            curveTo(11.8f, 9.6f, 10.6f, 9.1f, 9.9f, 9.8f)
            lineTo(8.8f, 10.9f)
            curveTo(7.1f, 8.8f, 4.3f, 7.5f, 11.5f, 7.5f)
            close()
        }
    }.build()

    val Erase: ImageVector = ImageVector.Builder(
        name = "CuteErase",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(16.2f, 3.2f)
            curveTo(16.8f, 2.6f, 17.8f, 2.6f, 18.4f, 3.2f)
            lineTo(21.4f, 6.2f)
            curveTo(22f, 6.8f, 22f, 7.8f, 21.4f, 8.4f)
            lineTo(13.6f, 16.2f)
            curveTo(13.2f, 16.6f, 12.6f, 16.9f, 12f, 16.9f)
            horizontalLineTo(6.4f)
            lineTo(4.2f, 14.7f)
            curveTo(3.6f, 14.1f, 3.6f, 13.1f, 4.2f, 12.5f)
            lineTo(16.2f, 3.2f)
            close()
            moveTo(2.5f, 19.5f)
            curveTo(2.5f, 18.9f, 2.9f, 18.5f, 3.5f, 18.5f)
            horizontalLineTo(18.5f)
            curveTo(19.1f, 18.5f, 19.5f, 18.9f, 19.5f, 19.5f)
            curveTo(19.5f, 20.1f, 19.1f, 20.5f, 18.5f, 20.5f)
            horizontalLineTo(3.5f)
            curveTo(2.9f, 20.5f, 2.5f, 20.1f, 2.5f, 19.5f)
            close()
        }
    }.build()

    val Pencil: ImageVector = ImageVector.Builder(
        name = "CutePencil",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(18f, 2.5f)
            curveTo(18.8f, 2.5f, 19.6f, 2.8f, 20.2f, 3.4f)
            lineTo(21.1f, 4.3f)
            curveTo(22.3f, 5.5f, 22.3f, 7.5f, 21.1f, 8.7f)
            lineTo(9.5f, 20.3f)
            curveTo(9.2f, 20.6f, 8.7f, 20.8f, 8.3f, 20.9f)
            lineTo(3.8f, 21.9f)
            curveTo(3.1f, 22.1f, 2.4f, 21.4f, 2.6f, 20.7f)
            lineTo(3.6f, 16.2f)
            curveTo(3.7f, 15.8f, 3.9f, 15.3f, 4.2f, 15f)
            lineTo(15.8f, 3.4f)
            curveTo(16.4f, 2.8f, 17.2f, 2.5f, 18f, 2.5f)
            close()
            moveTo(14.8f, 7.4f)
            lineTo(17.1f, 9.7f)
            lineTo(18.6f, 8.2f)
            lineTo(16.3f, 5.9f)
            lineTo(14.8f, 7.4f)
            close()
        }
    }.build()

    val Lightbulb: ImageVector = ImageVector.Builder(
        name = "CuteLightbulb",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2.5f)
            curveTo(7.9f, 2.5f, 4.8f, 5.6f, 4.8f, 9.8f)
            curveTo(4.8f, 12.3f, 6.2f, 14.5f, 8.2f, 15.8f)
            verticalLineTo(17.2f)
            curveTo(8.2f, 17.8f, 8.7f, 18.2f, 9.2f, 18.2f)
            horizontalLineTo(14.8f)
            curveTo(15.3f, 18.2f, 15.8f, 17.8f, 15.8f, 17.2f)
            verticalLineTo(15.8f)
            curveTo(17.8f, 14.5f, 19.2f, 12.3f, 19.2f, 9.8f)
            curveTo(19.2f, 5.6f, 16.1f, 2.5f, 12f, 2.5f)
            close()
            moveTo(9.5f, 19.8f)
            curveTo(9.5f, 20.9f, 10.4f, 21.8f, 11.5f, 21.8f)
            horizontalLineTo(12.5f)
            curveTo(13.6f, 21.8f, 14.5f, 20.9f, 14.5f, 19.8f)
            verticalLineTo(19.2f)
            horizontalLineTo(9.5f)
            verticalLineTo(19.8f)
            close()
            moveTo(11.2f, 7.8f)
            curveTo(11.2f, 7.4f, 11.6f, 7f, 12f, 7f)
            curveTo(12.4f, 7f, 12.8f, 7.4f, 12.8f, 7.8f)
            verticalLineTo(11.2f)
            curveTo(12.8f, 11.6f, 12.4f, 12f, 12f, 12f)
            curveTo(11.6f, 12f, 11.2f, 11.6f, 11.2f, 11.2f)
            verticalLineTo(7.8f)
            close()
        }
    }.build()

    val Lock: ImageVector = ImageVector.Builder(
        name = "CuteLock",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(17f, 9f)
            horizontalLineTo(16f)
            verticalLineTo(6f)
            curveTo(16f, 3.8f, 14.2f, 2f, 12f, 2f)
            curveTo(9.8f, 2f, 8f, 3.8f, 8f, 6f)
            verticalLineTo(9f)
            horizontalLineTo(7f)
            curveTo(5.3f, 9f, 4f, 10.3f, 4f, 12f)
            verticalLineTo(19f)
            curveTo(4f, 20.7f, 5.3f, 22f, 7f, 22f)
            horizontalLineTo(17f)
            curveTo(18.7f, 22f, 20f, 20.7f, 20f, 19f)
            verticalLineTo(12f)
            curveTo(20f, 10.3f, 18.7f, 9f, 17f, 9f)
            close()
            moveTo(10f, 6f)
            curveTo(10f, 4.9f, 10.9f, 4f, 12f, 4f)
            curveTo(13.1f, 4f, 14f, 4.9f, 14f, 6f)
            verticalLineTo(9f)
            horizontalLineTo(10f)
            verticalLineTo(6f)
            close()
            moveTo(12f, 17f)
            curveTo(11.2f, 17f, 10.5f, 16.3f, 10.5f, 15.5f)
            curveTo(10.5f, 14.7f, 11.2f, 14f, 12f, 14f)
            curveTo(12.8f, 14f, 13.5f, 14.7f, 13.5f, 15.5f)
            curveTo(13.5f, 16.3f, 12.8f, 17f, 12f, 17f)
            close()
        }
    }.build()

    val Bomb: ImageVector = ImageVector.Builder(
        name = "CuteBomb",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(19.8f, 4.2f)
            curveTo(20.2f, 3.8f, 20.8f, 3.8f, 21.2f, 4.2f)
            curveTo(21.6f, 4.6f, 21.6f, 5.2f, 21.2f, 5.6f)
            lineTo(19f, 7.8f)
            curveTo(18.6f, 8.2f, 18f, 8.2f, 17.6f, 7.8f)
            curveTo(17.2f, 7.4f, 17.2f, 6.8f, 17.6f, 6.4f)
            lineTo(19.8f, 4.2f)
            close()
            moveTo(11.5f, 6f)
            curveTo(6.8f, 6f, 3f, 9.8f, 3f, 14.5f)
            curveTo(3f, 19.2f, 6.8f, 23f, 11.5f, 23f)
            curveTo(16.2f, 23f, 20f, 19.2f, 20f, 14.5f)
            curveTo(20f, 9.8f, 16.2f, 6f, 11.5f, 6f)
            close()
            moveTo(8.5f, 11.5f)
            curveTo(8.5f, 10.7f, 9.2f, 10f, 10f, 10f)
            curveTo(10.8f, 10f, 11.5f, 10.7f, 11.5f, 11.5f)
            curveTo(11.5f, 12.3f, 10.8f, 13f, 10f, 13f)
            curveTo(9.2f, 13f, 8.5f, 12.3f, 8.5f, 11.5f)
            close()
        }
    }.build()

    // --- Cute Nav Bar Icons ---

    val Home: ImageVector = ImageVector.Builder(
        name = "CuteHome",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(11.3f, 2.8f)
            curveTo(11.7f, 2.4f, 12.3f, 2.4f, 12.7f, 2.8f)
            lineTo(20.5f, 9.4f)
            curveTo(21.1f, 9.9f, 21.1f, 10.8f, 20.6f, 11.4f)
            curveTo(20.1f, 12f, 19.2f, 12f, 18.6f, 11.5f)
            lineTo(18.5f, 11.4f)
            verticalLineTo(19.5f)
            curveTo(18.5f, 20.6f, 17.6f, 21.5f, 16.5f, 21.5f)
            horizontalLineTo(14.5f)
            curveTo(13.9f, 21.5f, 13.5f, 21.1f, 13.5f, 20.5f)
            verticalLineTo(16.5f)
            curveTo(13.5f, 15.7f, 12.8f, 15f, 12f, 15f)
            curveTo(11.2f, 15f, 10.5f, 15.7f, 10.5f, 16.5f)
            verticalLineTo(20.5f)
            curveTo(10.5f, 21.1f, 10.1f, 21.5f, 9.5f, 21.5f)
            horizontalLineTo(7.5f)
            curveTo(6.4f, 21.5f, 5.5f, 20.6f, 5.5f, 19.5f)
            verticalLineTo(11.4f)
            lineTo(5.4f, 11.5f)
            curveTo(4.8f, 12f, 3.9f, 12f, 3.4f, 11.4f)
            curveTo(2.9f, 10.8f, 2.9f, 9.9f, 3.5f, 9.4f)
            lineTo(11.3f, 2.8f)
            close()
        }
    }.build()

    val Date: ImageVector = ImageVector.Builder(
        name = "CuteCalendar",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(18f, 4f)
            horizontalLineTo(17.5f)
            verticalLineTo(3f)
            curveTo(17.5f, 2.2f, 16.8f, 1.5f, 16f, 1.5f)
            curveTo(15.2f, 1.5f, 14.5f, 2.2f, 14.5f, 3f)
            verticalLineTo(4f)
            horizontalLineTo(9.5f)
            verticalLineTo(3f)
            curveTo(9.5f, 2.2f, 8.8f, 1.5f, 8f, 1.5f)
            curveTo(7.2f, 1.5f, 6.5f, 2.2f, 6.5f, 3f)
            verticalLineTo(4f)
            horizontalLineTo(6f)
            curveTo(4.3f, 4f, 3f, 5.3f, 3f, 7f)
            verticalLineTo(19f)
            curveTo(3f, 20.7f, 4.3f, 22f, 6f, 22f)
            horizontalLineTo(18f)
            curveTo(19.7f, 22f, 21f, 20.7f, 21f, 19f)
            verticalLineTo(7f)
            curveTo(21f, 5.3f, 19.7f, 4f, 18f, 4f)
            close()
            moveTo(19f, 19f)
            curveTo(19f, 19.6f, 18.6f, 20f, 18f, 20f)
            horizontalLineTo(6f)
            curveTo(5.4f, 20f, 5f, 19.6f, 5f, 19f)
            verticalLineTo(9f)
            horizontalLineTo(19f)
            verticalLineTo(19f)
            close()
            moveTo(12f, 11.5f)
            lineTo(12.7f, 13.1f)
            lineTo(14.5f, 13.3f)
            lineTo(13.2f, 14.5f)
            lineTo(13.5f, 16.2f)
            lineTo(12f, 15.3f)
            lineTo(10.5f, 16.2f)
            lineTo(10.8f, 14.5f)
            lineTo(9.5f, 13.3f)
            lineTo(11.3f, 13.1f)
            close()
        }
    }.build()

    val Leaderboard: ImageVector = ImageVector.Builder(
        name = "CuteLeaderboard",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(4.5f, 10f)
            curveTo(3.7f, 10f, 3f, 10.7f, 3f, 11.5f)
            verticalLineTo(19.5f)
            curveTo(3f, 20.3f, 3.7f, 21f, 4.5f, 21f)
            horizontalLineTo(7.5f)
            curveTo(8.3f, 21f, 9f, 20.3f, 9f, 19.5f)
            verticalLineTo(11.5f)
            curveTo(9f, 10.7f, 8.3f, 10f, 7.5f, 10f)
            horizontalLineTo(4.5f)
            close()
            moveTo(10.5f, 4.5f)
            curveTo(9.7f, 4.5f, 9f, 5.2f, 9f, 6f)
            verticalLineTo(19.5f)
            curveTo(9f, 20.3f, 9.7f, 21f, 10.5f, 21f)
            horizontalLineTo(13.5f)
            curveTo(14.3f, 21f, 15f, 20.3f, 15f, 19.5f)
            verticalLineTo(6f)
            curveTo(15f, 5.2f, 14.3f, 4.5f, 13.5f, 4.5f)
            horizontalLineTo(10.5f)
            close()
            moveTo(16.5f, 13f)
            curveTo(15.7f, 13f, 15f, 13.7f, 15f, 14.5f)
            verticalLineTo(19.5f)
            curveTo(15f, 20.3f, 15.7f, 21f, 16.5f, 21f)
            horizontalLineTo(19.5f)
            curveTo(20.3f, 21f, 21f, 20.3f, 21f, 19.5f)
            verticalLineTo(14.5f)
            curveTo(21f, 13.7f, 20.3f, 13f, 19.5f, 13f)
            horizontalLineTo(16.5f)
            close()
        }
    }.build()

    val Person: ImageVector = ImageVector.Builder(
        name = "CutePerson",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2f)
            curveTo(9.5f, 2f, 7.5f, 4f, 7.5f, 6.5f)
            curveTo(7.5f, 9f, 9.5f, 11f, 12f, 11f)
            curveTo(14.5f, 11f, 16.5f, 9f, 16.5f, 6.5f)
            curveTo(16.5f, 4f, 14.5f, 2f, 12f, 2f)
            close()
            moveTo(12f, 13f)
            curveTo(8.5f, 13f, 4f, 14.7f, 4f, 17.8f)
            verticalLineTo(19.5f)
            curveTo(4f, 20.6f, 4.9f, 21.5f, 6f, 21.5f)
            horizontalLineTo(18f)
            curveTo(19.1f, 21.5f, 20f, 20.6f, 20f, 19.5f)
            verticalLineTo(17.8f)
            curveTo(20f, 14.7f, 15.5f, 13f, 12f, 13f)
            close()
        }
    }.build()

    // --- Cute Gamification Badges ---

    val Flame: ImageVector = ImageVector.Builder(
        name = "CuteFlame",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(13.6f, 2.3f)
            curveTo(13.2f, 1.9f, 12.5f, 2.1f, 12.3f, 2.6f)
            curveTo(11f, 5.2f, 8.2f, 7.8f, 8.2f, 11.2f)
            curveTo(8.2f, 11.8f, 8.4f, 12.4f, 8.6f, 13f)
            curveTo(7.2f, 11.6f, 6.4f, 9.6f, 6.4f, 8.2f)
            curveTo(6.4f, 7.7f, 5.7f, 7.4f, 5.3f, 7.8f)
            curveTo(4.2f, 9.3f, 3.5f, 11.4f, 3.5f, 13.8f)
            curveTo(3.5f, 18.2f, 7.3f, 21.8f, 12f, 21.8f)
            curveTo(16.7f, 21.8f, 20.5f, 18.2f, 20.5f, 13.8f)
            curveTo(20.5f, 8.4f, 16.1f, 4.4f, 13.6f, 2.3f)
            close()
            moveTo(12f, 18.5f)
            curveTo(10f, 18.5f, 8.5f, 16.8f, 8.5f, 14.8f)
            curveTo(8.5f, 13.4f, 9.2f, 12.2f, 10.3f, 11.4f)
            curveTo(10.7f, 12.4f, 11.4f, 13.2f, 12.4f, 13.6f)
            curveTo(12.7f, 14.8f, 13.5f, 15.8f, 14.5f, 16.4f)
            curveTo(14.2f, 17.6f, 13.2f, 18.5f, 12f, 18.5f)
            close()
        }
    }.build()

    val Coin: ImageVector = ImageVector.Builder(
        name = "CuteCoin",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        // Layer 1: Dark Amber Outer 3D Rim
        path(fill = SolidColor(Color(0xFFD97706))) {
            moveTo(12f, 22f)
            curveTo(6.48f, 22f, 2f, 17.52f, 2f, 12f)
            curveTo(2f, 6.48f, 6.48f, 2f, 12f, 2f)
            curveTo(17.52f, 2f, 22f, 6.48f, 22f, 12f)
            curveTo(22f, 17.52f, 17.52f, 22f, 12f, 22f)
            close()
        }

        // Layer 2: Main Golden Yellow Coin Face
        path(fill = SolidColor(Color(0xFFF59E0B))) {
            moveTo(12f, 20.8f)
            curveTo(7.14f, 20.8f, 3.2f, 16.86f, 3.2f, 12f)
            curveTo(3.2f, 7.14f, 7.14f, 3.2f, 12f, 3.2f)
            curveTo(16.86f, 3.2f, 20.8f, 7.14f, 20.8f, 12f)
            curveTo(20.8f, 16.86f, 16.86f, 20.8f, 12f, 20.8f)
            close()
        }

        // Layer 3: Inner Bevel Ring (Bright Metallic Yellow)
        path(
            stroke = SolidColor(Color(0xFFFEF08A)),
            strokeLineWidth = 1.4f
        ) {
            moveTo(12f, 18.2f)
            curveTo(8.58f, 18.2f, 5.8f, 15.42f, 5.8f, 12f)
            curveTo(5.8f, 8.58f, 8.58f, 5.8f, 12f, 5.8f)
            curveTo(15.42f, 5.8f, 18.2f, 8.58f, 18.2f, 12f)
            curveTo(18.2f, 15.42f, 15.42f, 18.2f, 12f, 18.2f)
            close()
        }

        // Layer 4: Center Embossed Star Emblem (Rich Golden Brown)
        path(fill = SolidColor(Color(0xFF92400E))) {
            moveTo(12f, 7.5f)
            lineTo(13.2f, 10.8f)
            lineTo(16.5f, 12f)
            lineTo(13.2f, 13.2f)
            lineTo(12f, 16.5f)
            lineTo(10.8f, 13.2f)
            lineTo(7.5f, 12f)
            lineTo(10.8f, 10.8f)
            close()
        }

        // Layer 5: Specular Light Glint Highlight
        path(fill = SolidColor(Color.White.copy(alpha = 0.85f))) {
            moveTo(8.5f, 5.5f)
            curveTo(7f, 7f, 6f, 9f, 6f, 11f)
            curveTo(6.8f, 9.8f, 8f, 8.8f, 9.5f, 8f)
            curveTo(10f, 7f, 9.5f, 6f, 8.5f, 5.5f)
            close()
        }
    }.build()

    val Crown: ImageVector = ImageVector.Builder(
        name = "CuteCrown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(5f, 16f)
            lineTo(3.2f, 7.5f)
            curveTo(3f, 6.8f, 3.8f, 6.2f, 4.4f, 6.6f)
            lineTo(8.5f, 9.5f)
            lineTo(11.3f, 4.6f)
            curveTo(11.6f, 4f, 12.4f, 4f, 12.7f, 4.6f)
            lineTo(15.5f, 9.5f)
            lineTo(19.6f, 6.6f)
            curveTo(20.2f, 6.2f, 21f, 6.8f, 20.8f, 7.5f)
            lineTo(19f, 16f)
            curveTo(19f, 16.6f, 18.6f, 17f, 18f, 17f)
            horizontalLineTo(6f)
            curveTo(5.4f, 17f, 5f, 16.6f, 5f, 16f)
            close()
            moveTo(5f, 18.5f)
            curveTo(4.4f, 18.5f, 4f, 18.9f, 4f, 19.5f)
            curveTo(4f, 20.1f, 4.4f, 20.5f, 5f, 20.5f)
            horizontalLineTo(19f)
            curveTo(19.6f, 20.5f, 20f, 20.1f, 20f, 19.5f)
            curveTo(20f, 18.9f, 19.6f, 18.5f, 19f, 18.5f)
            horizontalLineTo(5f)
            close()
        }
    }.build()

    val Trophy: ImageVector = ImageVector.Builder(
        name = "CuteTrophy",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(18.5f, 4f)
            horizontalLineTo(17f)
            verticalLineTo(3f)
            curveTo(17f, 2.4f, 16.6f, 2f, 16f, 2f)
            horizontalLineTo(8f)
            curveTo(7.4f, 2f, 7f, 2.4f, 7f, 3f)
            verticalLineTo(4f)
            horizontalLineTo(5.5f)
            curveTo(4.1f, 4f, 3f, 5.1f, 3f, 6.5f)
            curveTo(3f, 9.5f, 5.2f, 12f, 8f, 12.8f)
            curveTo(8.7f, 14.2f, 9.8f, 15.3f, 11.2f, 15.8f)
            verticalLineTo(18f)
            horizontalLineTo(8.5f)
            curveTo(7.9f, 18f, 7.5f, 18.4f, 7.5f, 19f)
            curveTo(7.5f, 19.6f, 7.9f, 20f, 8.5f, 20f)
            horizontalLineTo(15.5f)
            curveTo(16.1f, 20f, 16.5f, 19.6f, 16.5f, 19f)
            curveTo(16.5f, 18.4f, 16.1f, 18f, 15.5f, 18f)
            horizontalLineTo(12.8f)
            verticalLineTo(15.8f)
            curveTo(14.2f, 15.3f, 15.3f, 14.2f, 16f, 12.8f)
            curveTo(18.8f, 12f, 21f, 9.5f, 21f, 6.5f)
            curveTo(21f, 5.1f, 19.9f, 4f, 18.5f, 4f)
            close()
            moveTo(5f, 6.8f)
            horizontalLineTo(7f)
            verticalLineTo(10.5f)
            curveTo(5.8f, 10f, 5f, 8.5f, 5f, 6.8f)
            close()
            moveTo(19f, 6.8f)
            curveTo(19f, 8.5f, 18.2f, 10f, 17f, 10.5f)
            verticalLineTo(6.8f)
            horizontalLineTo(19f)
            close()
        }
    }.build()

    val Star: ImageVector = ImageVector.Builder(
        name = "CuteStar",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2.2f)
            curveTo(12.4f, 2.2f, 12.8f, 2.5f, 12.9f, 2.9f)
            lineTo(15.2f, 8.1f)
            lineTo(20.8f, 8.7f)
            curveTo(21.3f, 8.8f, 21.6f, 9.3f, 21.3f, 9.7f)
            lineTo(17.1f, 13.5f)
            lineTo(18.3f, 19.1f)
            curveTo(18.4f, 19.6f, 17.9f, 20f, 17.5f, 19.7f)
            lineTo(12f, 16.7f)
            lineTo(6.5f, 19.7f)
            curveTo(6.1f, 20f, 5.6f, 19.6f, 5.7f, 19.1f)
            lineTo(6.9f, 13.5f)
            lineTo(2.7f, 9.7f)
            curveTo(2.4f, 9.3f, 2.7f, 8.8f, 3.2f, 8.7f)
            lineTo(8.8f, 8.1f)
            lineTo(11.1f, 2.9f)
            curveTo(11.2f, 2.5f, 11.6f, 2.2f, 12f, 2.2f)
            close()
        }
    }.build()

    val CheckCircle: ImageVector = ImageVector.Builder(
        name = "CuteCheckCircle",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2f)
            curveTo(6.5f, 2f, 2f, 6.5f, 2f, 12f)
            curveTo(2f, 17.5f, 6.5f, 22f, 12f, 22f)
            curveTo(17.5f, 22f, 22f, 17.5f, 22f, 12f)
            curveTo(22f, 6.5f, 17.5f, 2f, 12f, 2f)
            close()
            moveTo(10.2f, 16.3f)
            curveTo(9.8f, 16.7f, 9.2f, 16.7f, 8.8f, 16.3f)
            lineTo(5.8f, 13.3f)
            curveTo(5.4f, 12.9f, 5.4f, 12.3f, 5.8f, 11.9f)
            curveTo(6.2f, 11.5f, 6.8f, 11.5f, 7.2f, 11.9f)
            lineTo(9.5f, 14.2f)
            lineTo(16.8f, 6.9f)
            curveTo(17.2f, 6.5f, 17.8f, 6.5f, 18.2f, 6.9f)
            curveTo(18.6f, 7.3f, 18.6f, 7.9f, 18.2f, 8.3f)
            lineTo(10.2f, 16.3f)
            close()
        }
    }.build()

    val Check: ImageVector = ImageVector.Builder(
        name = "CuteCheck",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(9f, 16.2f)
            lineTo(4.8f, 12f)
            curveTo(4.4f, 11.6f, 3.8f, 11.6f, 3.4f, 12f)
            curveTo(3f, 12.4f, 3f, 13f, 3.4f, 13.4f)
            lineTo(8.3f, 18.3f)
            curveTo(8.7f, 18.7f, 9.3f, 18.7f, 9.7f, 18.3f)
            lineTo(20.6f, 7.4f)
            curveTo(21f, 7f, 21f, 6.4f, 20.6f, 6f)
            curveTo(20.2f, 5.6f, 19.6f, 5.6f, 19.2f, 6f)
            lineTo(9f, 16.2f)
            close()
        }
    }.build()

    // --- Controls & Playback ---

    val Play: ImageVector = ImageVector.Builder(
        name = "CutePlay",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(8.5f, 6.2f)
            curveTo(7.7f, 5.7f, 6.8f, 6.3f, 6.8f, 7.2f)
            verticalLineTo(16.8f)
            curveTo(6.8f, 17.7f, 7.7f, 18.3f, 8.5f, 17.8f)
            lineTo(17.8f, 13f)
            curveTo(18.6f, 12.6f, 18.6f, 11.4f, 17.8f, 11f)
            lineTo(8.5f, 6.2f)
            close()
        }
    }.build()

    val Pause: ImageVector = ImageVector.Builder(
        name = "CutePause",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(7.5f, 5f)
            curveTo(6.7f, 5f, 6f, 5.7f, 6f, 6.5f)
            verticalLineTo(17.5f)
            curveTo(6f, 18.3f, 6.7f, 19f, 7.5f, 19f)
            curveTo(8.3f, 19f, 9f, 18.3f, 9f, 17.5f)
            verticalLineTo(6.5f)
            curveTo(9f, 5.7f, 8.3f, 5f, 7.5f, 5f)
            close()
            moveTo(16.5f, 5f)
            curveTo(15.7f, 5f, 15f, 5.7f, 15f, 6.5f)
            verticalLineTo(17.5f)
            curveTo(15f, 18.3f, 15.7f, 19f, 16.5f, 19f)
            curveTo(17.3f, 19f, 18f, 18.3f, 18f, 17.5f)
            verticalLineTo(6.5f)
            curveTo(18f, 5.7f, 17.3f, 5f, 16.5f, 5f)
            close()
        }
    }.build()

    val Refresh: ImageVector = ImageVector.Builder(
        name = "CuteRefresh",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(17.6f, 6.4f)
            curveTo(16.2f, 4.9f, 14.2f, 4f, 12f, 4f)
            curveTo(7.6f, 4f, 4f, 7.6f, 4f, 12f)
            curveTo(4f, 16.4f, 7.6f, 20f, 12f, 20f)
            curveTo(15.7f, 20f, 18.8f, 17.5f, 19.7f, 14f)
            curveTo(19.9f, 13.5f, 19.5f, 13f, 19f, 13f)
            curveTo(18.6f, 13f, 18.2f, 13.3f, 18.1f, 13.7f)
            curveTo(17.4f, 16.4f, 14.9f, 18.4f, 12f, 18.4f)
            curveTo(8.5f, 18.4f, 5.6f, 15.5f, 5.6f, 12f)
            curveTo(5.6f, 8.5f, 8.5f, 5.6f, 12f, 5.6f)
            curveTo(13.8f, 5.6f, 15.4f, 6.3f, 16.5f, 7.5f)
            lineTo(14.9f, 9.1f)
            curveTo(14.3f, 9.7f, 14.7f, 10.8f, 15.6f, 10.8f)
            horizontalLineTo(20f)
            curveTo(20.5f, 10.8f, 21f, 10.4f, 21f, 9.8f)
            verticalLineTo(5.4f)
            curveTo(21f, 4.5f, 19.9f, 4.1f, 19.3f, 4.7f)
            lineTo(17.6f, 6.4f)
            close()
        }
    }.build()

    val Settings: ImageVector = ImageVector.Builder(
        name = "CuteSettings",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(19.4f, 13f)
            curveTo(19.5f, 12.7f, 19.5f, 12.3f, 19.5f, 12f)
            curveTo(19.5f, 11.7f, 19.5f, 11.3f, 19.4f, 11f)
            lineTo(21.5f, 9.4f)
            curveTo(21.7f, 9.2f, 21.8f, 8.9f, 21.7f, 8.7f)
            lineTo(19.7f, 5.3f)
            curveTo(19.5f, 5.1f, 19.3f, 5f, 19f, 5.1f)
            lineTo(16.6f, 6.1f)
            curveTo(16f, 5.7f, 15.5f, 5.3f, 14.9f, 5.1f)
            lineTo(14.5f, 2.4f)
            curveTo(14.5f, 2.2f, 14.3f, 2f, 14f, 2f)
            horizontalLineTo(10f)
            curveTo(9.8f, 2f, 9.5f, 2.2f, 9.5f, 2.4f)
            lineTo(9.1f, 5.1f)
            curveTo(8.5f, 5.3f, 8f, 5.7f, 7.4f, 6.1f)
            lineTo(5f, 5.1f)
            curveTo(4.7f, 5f, 4.5f, 5.1f, 4.3f, 5.3f)
            lineTo(2.3f, 8.7f)
            curveTo(2.2f, 8.9f, 2.3f, 9.2f, 2.5f, 9.4f)
            lineTo(4.6f, 11f)
            curveTo(4.5f, 11.3f, 4.5f, 11.7f, 4.5f, 12f)
            curveTo(4.5f, 12.3f, 4.5f, 12.7f, 4.6f, 13f)
            lineTo(2.5f, 14.6f)
            curveTo(2.3f, 14.8f, 2.2f, 15.1f, 2.3f, 15.3f)
            lineTo(4.3f, 18.7f)
            curveTo(4.5f, 18.9f, 4.7f, 19f, 5f, 18.9f)
            lineTo(7.4f, 17.9f)
            curveTo(8f, 18.3f, 8.5f, 18.7f, 9.1f, 18.9f)
            lineTo(9.5f, 21.6f)
            curveTo(9.5f, 21.8f, 9.8f, 22f, 10f, 22f)
            horizontalLineTo(14f)
            curveTo(14.3f, 22f, 14.5f, 21.8f, 14.5f, 21.6f)
            lineTo(14.9f, 18.9f)
            curveTo(15.5f, 18.7f, 16f, 18.3f, 16.6f, 17.9f)
            lineTo(19f, 18.9f)
            curveTo(19.3f, 19f, 19.5f, 18.9f, 19.7f, 18.7f)
            lineTo(21.7f, 15.3f)
            curveTo(21.8f, 15.1f, 21.7f, 14.8f, 21.5f, 14.6f)
            lineTo(19.4f, 13f)
            close()
            moveTo(12f, 15.5f)
            curveTo(10.1f, 15.5f, 8.5f, 13.9f, 8.5f, 12f)
            curveTo(8.5f, 10.1f, 10.1f, 8.5f, 12f, 8.5f)
            curveTo(13.9f, 8.5f, 15.5f, 10.1f, 15.5f, 12f)
            curveTo(15.5f, 13.9f, 13.9f, 15.5f, 12f, 15.5f)
            close()
        }
    }.build()

    val Info: ImageVector = ImageVector.Builder(
        name = "CuteInfo",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2f)
            curveTo(6.5f, 2f, 2f, 6.5f, 2f, 12f)
            curveTo(2f, 17.5f, 6.5f, 22f, 12f, 22f)
            curveTo(17.5f, 22f, 22f, 17.5f, 22f, 12f)
            curveTo(22f, 6.5f, 17.5f, 2f, 12f, 2f)
            close()
            moveTo(12f, 17f)
            curveTo(11.4f, 17f, 11f, 16.6f, 11f, 16f)
            verticalLineTo(12f)
            curveTo(11f, 11.4f, 11.4f, 11f, 12f, 11f)
            curveTo(12.6f, 11f, 13f, 11.4f, 13f, 12f)
            verticalLineTo(16f)
            curveTo(13f, 16.6f, 12.6f, 17f, 12f, 17f)
            close()
            moveTo(12f, 9f)
            curveTo(11.3f, 9f, 10.8f, 8.5f, 10.8f, 7.8f)
            curveTo(10.8f, 7.1f, 11.3f, 6.5f, 12f, 6.5f)
            curveTo(12.7f, 6.5f, 13.2f, 7.1f, 13.2f, 7.8f)
            curveTo(13.2f, 8.5f, 12.7f, 9f, 12f, 9f)
            close()
        }
    }.build()

    // --- New Cute Icons (Everywhere!) ---

    val Clock: ImageVector = ImageVector.Builder(
        name = "CuteClock",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            // Cute bubbly clock
            moveTo(12f, 2f)
            curveTo(6.5f, 2f, 2f, 6.5f, 2f, 12f)
            curveTo(2f, 17.5f, 6.5f, 22f, 12f, 22f)
            curveTo(17.5f, 22f, 22f, 17.5f, 22f, 12f)
            curveTo(22f, 6.5f, 17.5f, 2f, 12f, 2f)
            close()
            moveTo(12f, 20f)
            curveTo(7.6f, 20f, 4f, 16.4f, 4f, 12f)
            curveTo(4f, 7.6f, 7.6f, 4f, 12f, 4f)
            curveTo(16.4f, 4f, 20f, 7.6f, 20f, 12f)
            curveTo(20f, 16.4f, 16.4f, 20f, 12f, 20f)
            close()
            // Rounded hour and minute hands
            moveTo(12.8f, 7f)
            curveTo(12.8f, 6.6f, 12.4f, 6.2f, 12f, 6.2f)
            curveTo(11.6f, 6.2f, 11.2f, 6.6f, 11.2f, 7f)
            verticalLineTo(12f)
            curveTo(11.2f, 12.4f, 11.4f, 12.8f, 11.8f, 13f)
            lineTo(15.2f, 15f)
            curveTo(15.6f, 15.2f, 16.1f, 15.1f, 16.3f, 14.7f)
            curveTo(16.5f, 14.3f, 16.4f, 13.8f, 16f, 13.6f)
            lineTo(12.8f, 11.7f)
            verticalLineTo(7f)
            close()
        }
    }.build()

    val Heart: ImageVector = ImageVector.Builder(
        name = "CuteHeart",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 21.3f)
            curveTo(11.6f, 21.3f, 11.2f, 21.1f, 10.9f, 20.8f)
            curveTo(5.2f, 15.5f, 2f, 12.2f, 2f, 8.2f)
            curveTo(2f, 4.9f, 4.5f, 2.4f, 7.8f, 2.4f)
            curveTo(9.7f, 2.4f, 11.3f, 3.2f, 12f, 4.4f)
            curveTo(12.7f, 3.2f, 14.3f, 2.4f, 16.2f, 2.4f)
            curveTo(19.5f, 2.4f, 22f, 4.9f, 22f, 8.2f)
            curveTo(22f, 12.2f, 18.8f, 15.5f, 13.1f, 20.8f)
            curveTo(12.8f, 21.1f, 12.4f, 21.3f, 12f, 21.3f)
            close()
        }
    }.build()

    val Lightning: ImageVector = ImageVector.Builder(
        name = "CuteLightning",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            // Chubby rounded lightning bolt
            moveTo(12.8f, 2.2f)
            curveTo(12.2f, 1.8f, 11.4f, 2.2f, 11.3f, 2.9f)
            lineTo(8.2f, 11.5f)
            curveTo(8f, 12.1f, 8.5f, 12.8f, 9.1f, 12.8f)
            horizontalLineTo(12.5f)
            lineTo(10.2f, 20.8f)
            curveTo(9.9f, 21.8f, 11.3f, 22.4f, 11.8f, 21.5f)
            lineTo(16.5f, 12.5f)
            curveTo(16.9f, 11.8f, 16.4f, 11f, 15.6f, 11f)
            horizontalLineTo(12.8f)
            lineTo(14.5f, 3.2f)
            curveTo(14.7f, 2.5f, 13.5f, 1.9f, 12.8f, 2.2f)
            close()
        }
    }.build()

    val Sprout: ImageVector = ImageVector.Builder(
        name = "CuteSprout",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            // Cute plant sprout for Easy difficulty
            moveTo(20.5f, 5.5f)
            curveTo(15.5f, 5.5f, 12.5f, 9.5f, 12f, 12.2f)
            curveTo(11.5f, 9.5f, 8.5f, 5.5f, 3.5f, 5.5f)
            curveTo(3.2f, 5.5f, 3f, 5.7f, 3f, 6f)
            curveTo(3f, 11f, 7f, 14.5f, 11f, 14.8f)
            verticalLineTo(19.5f)
            curveTo(11f, 20.1f, 11.4f, 20.5f, 12f, 20.5f)
            curveTo(12.6f, 20.5f, 13f, 20.1f, 13f, 19.5f)
            verticalLineTo(14.8f)
            curveTo(17f, 14.5f, 21f, 11f, 21f, 6f)
            curveTo(21f, 5.7f, 20.8f, 5.5f, 20.5f, 5.5f)
            close()
        }
    }.build()

    val Target: ImageVector = ImageVector.Builder(
        name = "CuteTarget",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2f)
            curveTo(6.5f, 2f, 2f, 6.5f, 2f, 12f)
            curveTo(2f, 17.5f, 6.5f, 22f, 12f, 22f)
            curveTo(17.5f, 22f, 22f, 17.5f, 22f, 12f)
            curveTo(22f, 6.5f, 17.5f, 2f, 12f, 2f)
            close()
            moveTo(12f, 18f)
            curveTo(8.7f, 18f, 6f, 15.3f, 6f, 12f)
            curveTo(6f, 8.7f, 8.7f, 6f, 12f, 6f)
            curveTo(15.3f, 6f, 18f, 8.7f, 18f, 12f)
            curveTo(18f, 15.3f, 15.3f, 18f, 12f, 18f)
            close()
            moveTo(12f, 9.5f)
            curveTo(10.6f, 9.5f, 9.5f, 10.6f, 9.5f, 12f)
            curveTo(9.5f, 13.4f, 10.6f, 14.5f, 12f, 14.5f)
            curveTo(13.4f, 14.5f, 14.5f, 13.4f, 14.5f, 12f)
            curveTo(14.5f, 10.6f, 13.4f, 9.5f, 12f, 9.5f)
            close()
        }
    }.build()

    val Diamond: ImageVector = ImageVector.Builder(
        name = "CuteDiamond",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            // Cute sparkling gem
            moveTo(12f, 2.5f)
            lineTo(18.5f, 8.5f)
            lineTo(12.7f, 21.2f)
            curveTo(12.4f, 21.7f, 11.6f, 21.7f, 11.3f, 21.2f)
            lineTo(5.5f, 8.5f)
            lineTo(12f, 2.5f)
            close()
            moveTo(12f, 5.2f)
            lineTo(8.3f, 8.5f)
            horizontalLineTo(15.7f)
            lineTo(12f, 5.2f)
            close()
        }
    }.build()

    val Grid: ImageVector = ImageVector.Builder(
        name = "CuteGrid",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            // Cute rounded 3x3 puzzle grid
            moveTo(7.5f, 3f)
            curveTo(5f, 3f, 3f, 5f, 3f, 7.5f)
            verticalLineTo(16.5f)
            curveTo(3f, 19f, 5f, 21f, 7.5f, 21f)
            horizontalLineTo(16.5f)
            curveTo(19f, 21f, 21f, 19f, 21f, 16.5f)
            verticalLineTo(7.5f)
            curveTo(21f, 5f, 19f, 3f, 16.5f, 3f)
            horizontalLineTo(7.5f)
            close()
            // Rounded inner grid cells
            moveTo(5.5f, 7.5f)
            curveTo(5.5f, 6.4f, 6.4f, 5.5f, 7.5f, 5.5f)
            horizontalLineTo(8.5f)
            verticalLineTo(8.5f)
            horizontalLineTo(5.5f)
            verticalLineTo(7.5f)
            close()
            moveTo(10.5f, 5.5f)
            horizontalLineTo(13.5f)
            verticalLineTo(8.5f)
            horizontalLineTo(10.5f)
            verticalLineTo(5.5f)
            close()
            moveTo(15.5f, 5.5f)
            horizontalLineTo(16.5f)
            curveTo(17.6f, 5.5f, 18.5f, 6.4f, 18.5f, 7.5f)
            verticalLineTo(8.5f)
            horizontalLineTo(15.5f)
            verticalLineTo(5.5f)
            close()
            moveTo(5.5f, 10.5f)
            horizontalLineTo(8.5f)
            verticalLineTo(13.5f)
            horizontalLineTo(5.5f)
            verticalLineTo(10.5f)
            close()
            moveTo(10.5f, 10.5f)
            horizontalLineTo(13.5f)
            verticalLineTo(13.5f)
            horizontalLineTo(10.5f)
            verticalLineTo(10.5f)
            close()
            moveTo(15.5f, 10.5f)
            horizontalLineTo(18.5f)
            verticalLineTo(13.5f)
            horizontalLineTo(15.5f)
            verticalLineTo(10.5f)
            close()
            moveTo(5.5f, 15.5f)
            horizontalLineTo(8.5f)
            verticalLineTo(18.5f)
            horizontalLineTo(7.5f)
            curveTo(6.4f, 18.5f, 5.5f, 17.6f, 5.5f, 16.5f)
            verticalLineTo(15.5f)
            close()
            moveTo(10.5f, 15.5f)
            horizontalLineTo(13.5f)
            verticalLineTo(18.5f)
            horizontalLineTo(10.5f)
            verticalLineTo(15.5f)
            close()
            moveTo(15.5f, 15.5f)
            horizontalLineTo(18.5f)
            verticalLineTo(16.5f)
            curveTo(18.5f, 17.6f, 17.6f, 18.5f, 16.5f, 18.5f)
            horizontalLineTo(15.5f)
            verticalLineTo(15.5f)
            close()
        }
    }.build()

    val Sound: ImageVector = ImageVector.Builder(
        name = "CuteSound",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(4.5f, 9f)
            horizontalLineTo(7.2f)
            lineTo(11.4f, 5.2f)
            curveTo(12f, 4.7f, 13f, 5.1f, 13f, 6f)
            verticalLineTo(18f)
            curveTo(13f, 18.9f, 12f, 19.3f, 11.4f, 18.8f)
            lineTo(7.2f, 15f)
            horizontalLineTo(4.5f)
            curveTo(3.7f, 15f, 3f, 14.3f, 3f, 13.5f)
            verticalLineTo(10.5f)
            curveTo(3f, 9.7f, 3.7f, 9f, 4.5f, 9f)
            close()
            moveTo(16.5f, 8.5f)
            curveTo(17.8f, 9.6f, 18.5f, 10.8f, 18.5f, 12f)
            curveTo(18.5f, 13.2f, 17.8f, 14.4f, 16.5f, 15.5f)
            curveTo(16.1f, 15.9f, 15.4f, 15.9f, 15f, 15.5f)
            curveTo(14.6f, 15.1f, 14.6f, 14.4f, 15f, 14f)
            curveTo(15.8f, 13.3f, 16.2f, 12.6f, 16.2f, 12f)
            curveTo(16.2f, 11.4f, 15.8f, 10.7f, 15f, 10f)
            curveTo(14.6f, 9.6f, 14.6f, 8.9f, 15f, 8.5f)
            curveTo(15.4f, 8.1f, 16.1f, 8.1f, 16.5f, 8.5f)
            close()
        }
    }.build()

    val Vibrate: ImageVector = ImageVector.Builder(
        name = "CuteVibrate",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            // Cute phone in middle
            moveTo(8.5f, 4f)
            curveTo(7.7f, 4f, 7f, 4.7f, 7f, 5.5f)
            verticalLineTo(18.5f)
            curveTo(7f, 19.3f, 7.7f, 20f, 8.5f, 20f)
            horizontalLineTo(15.5f)
            curveTo(16.3f, 20f, 17f, 19.3f, 17f, 18.5f)
            verticalLineTo(5.5f)
            curveTo(17f, 4.7f, 16.3f, 4f, 15.5f, 4f)
            horizontalLineTo(8.5f)
            close()
            // Vibration waves
            moveTo(3.5f, 8.5f)
            curveTo(4.1f, 8.5f, 4.5f, 8.9f, 4.5f, 9.5f)
            verticalLineTo(14.5f)
            curveTo(4.5f, 15.1f, 4.1f, 15.5f, 3.5f, 15.5f)
            curveTo(2.9f, 15.5f, 2.5f, 15.1f, 2.5f, 14.5f)
            verticalLineTo(9.5f)
            curveTo(2.5f, 8.9f, 2.9f, 8.5f, 3.5f, 8.5f)
            close()
            moveTo(20.5f, 8.5f)
            curveTo(21.1f, 8.5f, 21.5f, 8.9f, 21.5f, 9.5f)
            verticalLineTo(14.5f)
            curveTo(21.5f, 15.1f, 21.1f, 15.5f, 20.5f, 15.5f)
            curveTo(19.9f, 15.5f, 19.5f, 15.1f, 19.5f, 14.5f)
            verticalLineTo(9.5f)
            curveTo(19.5f, 8.9f, 19.9f, 8.5f, 20.5f, 8.5f)
            close()
        }
    }.build()

    val Palette: ImageVector = ImageVector.Builder(
        name = "CutePalette",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(12f, 2f)
            curveTo(6.5f, 2f, 2f, 6.5f, 2f, 12f)
            curveTo(2f, 17.5f, 6.5f, 22f, 12f, 22f)
            curveTo(13.4f, 22f, 14.5f, 20.9f, 14.5f, 19.5f)
            curveTo(14.5f, 18.8f, 14.2f, 18.2f, 13.8f, 17.8f)
            curveTo(13.4f, 17.4f, 13.2f, 16.9f, 13.2f, 16.5f)
            curveTo(13.2f, 15.4f, 14.1f, 14.5f, 15.2f, 14.5f)
            horizontalLineTo(17.5f)
            curveTo(19.9f, 14.5f, 22f, 12.4f, 22f, 10f)
            curveTo(22f, 5.6f, 17.5f, 2f, 12f, 2f)
            close()
            // Cute paint spots
            moveTo(6.5f, 11.5f)
            curveTo(5.7f, 11.5f, 5f, 10.8f, 5f, 10f)
            curveTo(5f, 9.2f, 5.7f, 8.5f, 6.5f, 8.5f)
            curveTo(7.3f, 8.5f, 8f, 9.2f, 8f, 10f)
            curveTo(8f, 10.8f, 7.3f, 11.5f, 6.5f, 11.5f)
            close()
            moveTo(9.5f, 7.5f)
            curveTo(8.7f, 7.5f, 8f, 6.8f, 8f, 6f)
            curveTo(8f, 5.2f, 8.7f, 4.5f, 9.5f, 4.5f)
            curveTo(10.3f, 4.5f, 11f, 5.2f, 11f, 6f)
            curveTo(11f, 6.8f, 10.3f, 7.5f, 9.5f, 7.5f)
            close()
            moveTo(14.5f, 7.5f)
            curveTo(13.7f, 7.5f, 13f, 6.8f, 13f, 6f)
            curveTo(13f, 5.2f, 13.7f, 4.5f, 14.5f, 4.5f)
            curveTo(15.3f, 4.5f, 16f, 5.2f, 16f, 6f)
            curveTo(16f, 6.8f, 15.3f, 7.5f, 14.5f, 7.5f)
            close()
            moveTo(17.5f, 11.5f)
            curveTo(16.7f, 11.5f, 16f, 10.8f, 16f, 10f)
            curveTo(16f, 9.2f, 16.7f, 8.5f, 17.5f, 8.5f)
            curveTo(18.3f, 8.5f, 19f, 9.2f, 19f, 10f)
            curveTo(19f, 10.8f, 18.3f, 11.5f, 17.5f, 11.5f)
            close()
        }
    }.build()

    val Trash: ImageVector = ImageVector.Builder(
        name = "CuteTrash",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(18.5f, 6f)
            horizontalLineTo(15.5f)
            lineTo(15.1f, 4.2f)
            curveTo(14.9f, 3.5f, 14.2f, 3f, 13.5f, 3f)
            horizontalLineTo(10.5f)
            curveTo(9.8f, 3f, 9.1f, 3.5f, 8.9f, 4.2f)
            lineTo(8.5f, 6f)
            horizontalLineTo(5.5f)
            curveTo(4.7f, 6f, 4f, 6.7f, 4f, 7.5f)
            curveTo(4f, 8.3f, 4.7f, 9f, 5.5f, 9f)
            horizontalLineTo(6f)
            verticalLineTo(18.5f)
            curveTo(6f, 20.4f, 7.6f, 22f, 9.5f, 22f)
            horizontalLineTo(14.5f)
            curveTo(16.4f, 22f, 18f, 20.4f, 18f, 18.5f)
            verticalLineTo(9f)
            horizontalLineTo(18.5f)
            curveTo(19.3f, 9f, 20f, 8.3f, 20f, 7.5f)
            curveTo(20f, 6.7f, 19.3f, 6f, 18.5f, 6f)
            close()
        }
    }.build()

    val Close: ImageVector = ImageVector.Builder(
        name = "CuteClose",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            moveTo(18.3f, 5.7f)
            curveTo(17.9f, 5.3f, 17.2f, 5.3f, 16.8f, 5.7f)
            lineTo(12f, 10.5f)
            lineTo(7.2f, 5.7f)
            curveTo(6.8f, 5.3f, 6.1f, 5.3f, 5.7f, 5.7f)
            curveTo(5.3f, 6.1f, 5.3f, 6.8f, 5.7f, 7.2f)
            lineTo(10.5f, 12f)
            lineTo(5.7f, 16.8f)
            curveTo(5.3f, 17.2f, 5.3f, 17.9f, 5.7f, 18.3f)
            curveTo(6.1f, 18.7f, 6.8f, 18.7f, 7.2f, 18.3f)
            lineTo(12f, 13.5f)
            lineTo(16.8f, 18.3f)
            curveTo(17.2f, 18.7f, 17.9f, 18.7f, 18.3f, 18.3f)
            curveTo(18.7f, 17.9f, 18.7f, 17.2f, 18.3f, 16.8f)
            lineTo(13.5f, 12f)
            lineTo(18.3f, 7.2f)
            curveTo(18.7f, 6.8f, 18.7f, 6.1f, 18.3f, 5.7f)
            close()
        }
    }.build()
}

/**
 * Super Cute Rounded Icon Container with pastel bubbly styling.
 */
@Composable
fun CuteIconContainer(
    icon: ImageVector,
    contentDescription: String?,
    containerColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    iconSize: Dp = 20.dp,
    shape: Shape = RoundedCornerShape(14.dp),
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(containerColor)
            .border(width = 1.dp, color = iconColor.copy(alpha = 0.15f), shape = shape)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}
