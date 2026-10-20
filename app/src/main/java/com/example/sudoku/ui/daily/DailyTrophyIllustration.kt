package com.example.sudoku.ui.daily

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun DailyTrophyIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Soft radial glowing aura behind trophy
        val auraCenter = Offset(w * 0.5f, h * 0.52f)
        val auraRadius = w * 0.45f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF60A5FA).copy(alpha = 0.45f),
                    Color(0xFF38BDF8).copy(alpha = 0.25f),
                    Color.Transparent
                ),
                center = auraCenter,
                radius = auraRadius
            ),
            center = auraCenter,
            radius = auraRadius
        )

        // 2. Bokeh sparkles
        val sparkles = listOf(
            Triple(0.25f, 0.38f, 3.5f),
            Triple(0.78f, 0.42f, 3.0f),
            Triple(0.20f, 0.58f, 4.0f),
            Triple(0.82f, 0.62f, 3.2f),
            Triple(0.30f, 0.75f, 2.5f),
            Triple(0.72f, 0.78f, 2.8f),
            Triple(0.50f, 0.28f, 2.0f)
        )
        for ((sx, sy, sr) in sparkles) {
            drawCircle(
                color = Color.White.copy(alpha = 0.75f),
                center = Offset(w * sx, h * sy),
                radius = sr * (w / 120f)
            )
        }

        // 3. Trophy Geometry
        val centerX = w * 0.5f
        val cupTopY = h * 0.32f
        val cupHeight = h * 0.28f
        val cupBottomY = cupTopY + cupHeight
        val cupTopRadius = w * 0.28f
        val cupBottomRadius = w * 0.12f

        // Pedestal base
        val baseTopY = cupBottomY + h * 0.12f
        val baseBottomY = baseTopY + h * 0.08f
        val baseTopWidth = w * 0.28f
        val baseBottomWidth = w * 0.36f

        // Base gradient
        val baseBrush = Brush.linearGradient(
            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A), Color(0xFF334155), Color(0xFF0F172A)),
            start = Offset(centerX - baseBottomWidth, baseTopY),
            end = Offset(centerX + baseBottomWidth, baseBottomY)
        )

        // Lower base platform
        val basePath = Path().apply {
            moveTo(centerX - baseTopWidth, baseTopY)
            lineTo(centerX + baseTopWidth, baseTopY)
            lineTo(centerX + baseBottomWidth, baseBottomY)
            lineTo(centerX - baseBottomWidth, baseBottomY)
            close()
        }
        drawPath(basePath, baseBrush)

        // Base gold/silver rim highlight
        drawLine(
            brush = Brush.horizontalGradient(
                listOf(Color(0xFF475569), Color(0xFFE2E8F0), Color(0xFF94A3B8), Color(0xFF334155))
            ),
            start = Offset(centerX - baseTopWidth, baseTopY),
            end = Offset(centerX + baseTopWidth, baseTopY),
            strokeWidth = 3f
        )

        // Stem connecting cup to base
        val stemWidth = w * 0.07f
        val stemTopY = cupBottomY
        val stemPath = Path().apply {
            moveTo(centerX - stemWidth, stemTopY)
            lineTo(centerX + stemWidth, stemTopY)
            lineTo(centerX + stemWidth * 1.3f, baseTopY)
            lineTo(centerX - stemWidth * 1.3f, baseTopY)
            close()
        }
        val stemBrush = Brush.linearGradient(
            colors = listOf(Color(0xFF64748B), Color(0xFFF1F5F9), Color(0xFF334155), Color(0xFFCBD5E1)),
            start = Offset(centerX - stemWidth, stemTopY),
            end = Offset(centerX + stemWidth, stemTopY)
        )
        drawPath(stemPath, stemBrush)

        // Stem middle ring
        val stemRingY = stemTopY + (baseTopY - stemTopY) * 0.45f
        drawOval(
            brush = Brush.horizontalGradient(listOf(Color(0xFF475569), Color(0xFFF8FAFC), Color(0xFF475569))),
            topLeft = Offset(centerX - stemWidth * 1.5f, stemRingY - h * 0.015f),
            size = Size(stemWidth * 3f, h * 0.03f)
        )

        // Left and Right Handles
        val handlePathLeft = Path().apply {
            moveTo(centerX - cupTopRadius * 0.85f, cupTopY + cupHeight * 0.15f)
            cubicTo(
                centerX - cupTopRadius * 1.7f, cupTopY - cupHeight * 0.05f,
                centerX - cupTopRadius * 1.8f, cupTopY + cupHeight * 0.85f,
                centerX - cupBottomRadius * 1.0f, cupBottomY - cupHeight * 0.05f
            )
        }
        val handlePathRight = Path().apply {
            moveTo(centerX + cupTopRadius * 0.85f, cupTopY + cupHeight * 0.15f)
            cubicTo(
                centerX + cupTopRadius * 1.7f, cupTopY - cupHeight * 0.05f,
                centerX + cupTopRadius * 1.8f, cupTopY + cupHeight * 0.85f,
                centerX + cupBottomRadius * 1.0f, cupBottomY - cupHeight * 0.05f
            )
        }

        val handleStroke = Stroke(
            width = w * 0.045f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        val handleBrushLeft = Brush.linearGradient(
            colors = listOf(Color(0xFF94A3B8), Color(0xFFF8FAFC), Color(0xFF334155), Color(0xFFCBD5E1)),
            start = Offset(centerX - cupTopRadius * 1.8f, cupTopY),
            end = Offset(centerX - cupTopRadius * 0.8f, cupBottomY)
        )
        val handleBrushRight = Brush.linearGradient(
            colors = listOf(Color(0xFFCBD5E1), Color(0xFF334155), Color(0xFFF8FAFC), Color(0xFF94A3B8)),
            start = Offset(centerX + cupTopRadius * 0.8f, cupTopY),
            end = Offset(centerX + cupTopRadius * 1.8f, cupBottomY)
        )

        drawPath(handlePathLeft, handleBrushLeft, style = handleStroke)
        drawPath(handlePathRight, handleBrushRight, style = handleStroke)

        // Cup Body Path
        val cupPath = Path().apply {
            moveTo(centerX - cupTopRadius, cupTopY)
            lineTo(centerX + cupTopRadius, cupTopY)
            cubicTo(
                centerX + cupTopRadius * 0.95f, cupTopY + cupHeight * 0.55f,
                centerX + cupBottomRadius * 1.3f, cupBottomY - cupHeight * 0.15f,
                centerX + cupBottomRadius, cupBottomY
            )
            lineTo(centerX - cupBottomRadius, cupBottomY)
            cubicTo(
                centerX - cupBottomRadius * 1.3f, cupBottomY - cupHeight * 0.15f,
                centerX - cupTopRadius * 0.95f, cupTopY + cupHeight * 0.55f,
                centerX - cupTopRadius, cupTopY
            )
            close()
        }

        // Metallic Cup Gradient (Silver/Cobalt reflections)
        val cupBrush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF334155),
                Color(0xFF64748B),
                Color(0xFFE2E8F0),
                Color(0xFFFFFFFF),
                Color(0xFF94A3B8),
                Color(0xFF1E293B),
                Color(0xFF475569),
                Color(0xFFF1F5F9),
                Color(0xFF334155)
            ),
            startX = centerX - cupTopRadius,
            endX = centerX + cupTopRadius
        )
        drawPath(cupPath, cupBrush)

        // Top Rim Ellipse
        val rimHeight = h * 0.045f
        drawOval(
            brush = Brush.horizontalGradient(
                colors = listOf(Color(0xFF334155), Color(0xFFF8FAFC), Color(0xFF94A3B8), Color(0xFFE2E8F0), Color(0xFF1E293B))
            ),
            topLeft = Offset(centerX - cupTopRadius, cupTopY - rimHeight * 0.5f),
            size = Size(cupTopRadius * 2f, rimHeight)
        )

        // Inner Rim Shadow
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B)),
                center = Offset(centerX, cupTopY),
                radius = cupTopRadius * 0.85f
            ),
            topLeft = Offset(centerX - cupTopRadius * 0.85f, cupTopY - rimHeight * 0.35f),
            size = Size(cupTopRadius * 1.7f, rimHeight * 0.7f)
        )

        // Shiny reflection streaks on cup front
        val shinePath = Path().apply {
            moveTo(centerX - cupTopRadius * 0.25f, cupTopY + rimHeight * 0.5f)
            lineTo(centerX - cupTopRadius * 0.10f, cupTopY + rimHeight * 0.5f)
            lineTo(centerX - cupBottomRadius * 0.20f, cupBottomY - h * 0.02f)
            lineTo(centerX - cupBottomRadius * 0.35f, cupBottomY - h * 0.02f)
            close()
        }
        drawPath(
            shinePath,
            Brush.verticalGradient(
                listOf(Color.White.copy(alpha = 0.85f), Color.White.copy(alpha = 0.15f))
            )
        )
    }
}
