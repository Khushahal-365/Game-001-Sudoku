package com.example.sudoku.ui.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.sudoku.data.model.AppThemeColor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ThemedBackgroundOverlay(
    themeColor: AppThemeColor,
    modifier: Modifier = Modifier,
    alphaMultiplier: Float = 1.0f
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    // Make watermark clearly visible across the whole screen with 0.16 alpha
    val baseAlpha = 0.16f * alphaMultiplier

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        if (width <= 0 || height <= 0) return@Canvas

        val color = primaryColor.copy(alpha = baseAlpha)
        val strokeWidth = 2.0f

        when (themeColor) {
            AppThemeColor.RED_SPIDER -> drawSpiderWebPattern(color, strokeWidth)
            AppThemeColor.GREEN_LEAF -> drawLeafPattern(color, strokeWidth)
            AppThemeColor.BLUE_OCEAN -> drawOceanWavesPattern(color, strokeWidth)
            AppThemeColor.YELLOW_VOLT -> drawVoltPattern(color, strokeWidth)
            AppThemeColor.PURPLE_COSMOS -> drawCosmosPattern(color, strokeWidth)
            AppThemeColor.ORANGE_FIRE -> drawFirePattern(color, strokeWidth)
            AppThemeColor.PINK_SAKURA -> drawSakuraPattern(color, strokeWidth)
            AppThemeColor.CYAN_NEON -> drawNeonMatrixPattern(color, strokeWidth)
            AppThemeColor.TEAL_JADE -> drawJadePattern(color, strokeWidth)
            AppThemeColor.BROWN_PARCHMENT -> drawParchmentPattern(color, strokeWidth)
            AppThemeColor.INDIGO_ROYAL -> drawRoyalNightPattern(color, strokeWidth)
            AppThemeColor.SLATE_NOIR -> drawSlateNoirPattern(color, strokeWidth)
        }
    }
}

private fun DrawScope.drawSpiderWebPattern(color: Color, strokeWidth: Float) {
    val xStep = size.width / 2.5f
    val yStep = size.height / 3f
    val radius = size.width * 0.35f

    var y = yStep * 0.5f
    while (y < size.height + yStep) {
        var x = xStep * 0.5f
        while (x < size.width + xStep) {
            drawSingleWeb(Offset(x, y), radius, 8, 4, color, strokeWidth)
            x += xStep
        }
        y += yStep
    }
}

private fun DrawScope.drawSingleWeb(
    center: Offset,
    maxRadius: Float,
    spokes: Int,
    rings: Int,
    color: Color,
    strokeWidth: Float
) {
    val angleStep = (2 * PI / spokes).toFloat()
    for (i in 0 until spokes) {
        val angle = i * angleStep
        val endX = center.x + maxRadius * cos(angle)
        val endY = center.y + maxRadius * sin(angle)
        drawLine(color = color, start = center, end = Offset(endX, endY), strokeWidth = strokeWidth)
    }
    for (ring in 1..rings) {
        val radius = maxRadius * (ring.toFloat() / rings)
        val path = Path()
        for (i in 0..spokes) {
            val angle = i * angleStep
            val x = center.x + radius * cos(angle)
            val y = center.y + radius * sin(angle)
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
    }
}

private fun DrawScope.drawLeafPattern(color: Color, strokeWidth: Float) {
    val xStep = size.width / 3f
    val yStep = size.height / 4f

    var y = yStep * 0.5f
    var index = 0
    while (y < size.height) {
        var x = xStep * 0.5f
        while (x < size.width) {
            val scale = 1.0f + (index % 3) * 0.2f
            val path = Path().apply {
                moveTo(x, y - 50f * scale)
                quadraticTo(x + 50f * scale, y - 15f * scale, x, y + 50f * scale)
                quadraticTo(x - 50f * scale, y - 15f * scale, x, y - 50f * scale)
                moveTo(x, y - 45f * scale)
                lineTo(x, y + 45f * scale)
            }
            drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
            x += xStep
            index++
        }
        y += yStep
    }
}

private fun DrawScope.drawOceanWavesPattern(color: Color, strokeWidth: Float) {
    val yStep = size.height / 6f
    var yBase = yStep * 0.5f
    while (yBase < size.height + yStep) {
        val path = Path().apply {
            moveTo(0f, yBase)
            var x = 0f
            val step = size.width / 3f
            while (x <= size.width + step) {
                cubicTo(
                    x + step * 0.25f, yBase - 35f,
                    x + step * 0.75f, yBase + 35f,
                    x + step, yBase
                )
                x += step
            }
        }
        drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
        yBase += yStep
    }
}

private fun DrawScope.drawVoltPattern(color: Color, strokeWidth: Float) {
    val xStep = size.width / 3f
    val yStep = size.height / 3f

    var y = yStep * 0.5f
    while (y < size.height) {
        var x = xStep * 0.5f
        while (x < size.width) {
            val path = Path().apply {
                moveTo(x + 20f, y - 55f)
                lineTo(x - 20f, y - 5f)
                lineTo(x + 8f, y - 5f)
                lineTo(x - 25f, y + 55f)
                lineTo(x + 15f, y + 5f)
                lineTo(x - 8f, y + 5f)
                close()
            }
            drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
            x += xStep
        }
        y += yStep
    }
}

private fun DrawScope.drawCosmosPattern(color: Color, strokeWidth: Float) {
    val xStep = size.width / 3f
    val yStep = size.height / 3f

    var y = yStep * 0.5f
    while (y < size.height) {
        var x = xStep * 0.5f
        while (x < size.width) {
            drawCircle(color = color, center = Offset(x, y), radius = size.width * 0.15f, style = Stroke(width = strokeWidth))
            drawCircle(color = color, center = Offset(x, y), radius = 5f)
            x += xStep
        }
        y += yStep
    }
}

private fun DrawScope.drawFirePattern(color: Color, strokeWidth: Float) {
    val xStep = size.width / 3f
    val yStep = size.height / 3f

    var y = yStep * 0.5f
    while (y < size.height) {
        var x = xStep * 0.5f
        while (x < size.width) {
            val path = Path().apply {
                moveTo(x, y + 50f)
                cubicTo(x - 40f, y + 15f, x - 25f, y - 35f, x, y - 60f)
                cubicTo(x + 25f, y - 35f, x + 40f, y + 15f, x, y + 50f)
            }
            drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
            x += xStep
        }
        y += yStep
    }
}

private fun DrawScope.drawSakuraPattern(color: Color, strokeWidth: Float) {
    val xStep = size.width / 3f
    val yStep = size.height / 3f

    var y = yStep * 0.5f
    while (y < size.height) {
        var x = xStep * 0.5f
        while (x < size.width) {
            val petals = 5
            val angleStep = (2 * PI / petals).toFloat()
            for (i in 0 until petals) {
                val angle = i * angleStep
                val pX = x + 25f * cos(angle)
                val pY = y + 25f * sin(angle)
                drawCircle(color = color, center = Offset(pX, pY), radius = 14f, style = Stroke(width = strokeWidth))
            }
            drawCircle(color = color, center = Offset(x, y), radius = 6f)
            x += xStep
        }
        y += yStep
    }
}

private fun DrawScope.drawNeonMatrixPattern(color: Color, strokeWidth: Float) {
    val gridSize = 60f
    var y = 0f
    while (y < size.height) {
        drawLine(color, Offset(0f, y), Offset(size.width, y), strokeWidth = strokeWidth * 0.8f)
        y += gridSize
    }
    var x = 0f
    while (x < size.width) {
        drawLine(color, Offset(x, 0f), Offset(x, size.height), strokeWidth = strokeWidth * 0.8f)
        x += gridSize
    }
}

private fun DrawScope.drawJadePattern(color: Color, strokeWidth: Float) {
    val xStep = size.width / 3f
    val yStep = size.height / 3f

    var y = yStep * 0.5f
    while (y < size.height) {
        var x = xStep * 0.5f
        while (x < size.width) {
            for (r in 1..3) {
                val radius = r * 30f
                val path = Path().apply {
                    addArc(
                        oval = Rect(x - radius, y - radius, x + radius, y + radius),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 180f
                    )
                }
                drawPath(path, color, style = Stroke(width = strokeWidth))
            }
            x += xStep
        }
        y += yStep
    }
}

private fun DrawScope.drawParchmentPattern(color: Color, strokeWidth: Float) {
    val margin = 20f
    val cornerLen = 80f

    // Frame borders
    drawRect(color = color, topLeft = Offset(margin, margin), size = Size(size.width - margin * 2, size.height - margin * 2), style = Stroke(width = strokeWidth))

    // Corners
    val corners = listOf(
        Offset(margin, margin),
        Offset(size.width - margin, margin),
        Offset(margin, size.height - margin),
        Offset(size.width - margin, size.height - margin)
    )

    corners.forEach { c ->
        val hDir = if (c.x == margin) 1f else -1f
        val vDir = if (c.y == margin) 1f else -1f
        drawLine(color, c, Offset(c.x + cornerLen * hDir, c.y), strokeWidth * 1.5f)
        drawLine(color, c, Offset(c.x, c.y + cornerLen * vDir), strokeWidth * 1.5f)
    }
}

private fun DrawScope.drawRoyalNightPattern(color: Color, strokeWidth: Float) {
    val xStep = size.width / 3f
    val yStep = size.height / 3f

    var y = yStep * 0.5f
    while (y < size.height) {
        var x = xStep * 0.5f
        while (x < size.width) {
            // Star
            drawLine(color, Offset(x - 20f, y), Offset(x + 20f, y), strokeWidth)
            drawLine(color, Offset(x, y - 20f), Offset(x, y + 20f), strokeWidth)
            drawCircle(color, center = Offset(x + 30f, y - 30f), radius = 15f, style = Stroke(width = strokeWidth))
            x += xStep
        }
        y += yStep
    }
}

private fun DrawScope.drawSlateNoirPattern(color: Color, strokeWidth: Float) {
    var offset = -size.width
    val stripeWidth = 50f
    while (offset < size.width + size.height) {
        drawLine(
            color = color,
            start = Offset(offset, 0f),
            end = Offset(offset + size.height, size.height),
            strokeWidth = strokeWidth * 1.2f
        )
        offset += stripeWidth * 2f
    }
}
