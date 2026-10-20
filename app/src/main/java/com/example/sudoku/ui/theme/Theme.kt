package com.example.sudoku.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.sudoku.data.model.AppThemeColor

fun getThemeColorScheme(themeColor: AppThemeColor, isDark: Boolean): ColorScheme {
    return if (isDark) {
        when (themeColor) {
            AppThemeColor.RED_SPIDER -> darkColorScheme(
                primary = Color(0xFFEF4444),
                onPrimary = Color.White,
                primaryContainer = Color(0xFF7F1D1D),
                onPrimaryContainer = Color(0xFFFCA5A5),
                background = Color(0xFF0F172A),
                surface = Color(0xFF1E293B),
                surfaceVariant = Color(0xFF334155),
                onSurface = Color(0xFFF8FAFC),
                onSurfaceVariant = Color(0xFFCBD5E1),
                outline = Color(0xFF475569)
            )
            AppThemeColor.GREEN_LEAF -> darkColorScheme(
                primary = Color(0xFF10B981),
                onPrimary = Color.White,
                primaryContainer = Color(0xFF064E3B),
                onPrimaryContainer = Color(0xFFA7F3D0),
                background = Color(0xFF022C22),
                surface = Color(0xFF064E3B),
                surfaceVariant = Color(0xFF115E59),
                onSurface = Color(0xFFF0FDF4),
                onSurfaceVariant = Color(0xFF99F6E4),
                outline = Color(0xFF0D9488)
            )
            AppThemeColor.BLUE_OCEAN -> darkColorScheme(
                primary = Color(0xFF3B82F6),
                onPrimary = Color.White,
                primaryContainer = Color(0xFF1E3A8A),
                onPrimaryContainer = Color(0xFFBFDBFE),
                background = Color(0xFF0F172A),
                surface = Color(0xFF1E293B),
                surfaceVariant = Color(0xFF334155),
                onSurface = Color(0xFFF8FAFC),
                onSurfaceVariant = Color(0xFFCBD5E1),
                outline = Color(0xFF475569)
            )
            AppThemeColor.YELLOW_VOLT -> darkColorScheme(
                primary = Color(0xFFF59E0B),
                onPrimary = Color.Black,
                primaryContainer = Color(0xFF78350F),
                onPrimaryContainer = Color(0xFFFDE68A),
                background = Color(0xFF18181B),
                surface = Color(0xFF27272A),
                surfaceVariant = Color(0xFF3F3F46),
                onSurface = Color(0xFFFAFAFA),
                onSurfaceVariant = Color(0xFFE4E4E7),
                outline = Color(0xFF52525B)
            )
            AppThemeColor.PURPLE_COSMOS -> darkColorScheme(
                primary = Color(0xFF8B5CF6),
                onPrimary = Color.White,
                primaryContainer = Color(0xFF4C1D95),
                onPrimaryContainer = Color(0xFFDDD6FE),
                background = Color(0xFF0F172A),
                surface = Color(0xFF1E1B4B),
                surfaceVariant = Color(0xFF312E81),
                onSurface = Color(0xFFF8FAFC),
                onSurfaceVariant = Color(0xFFC7D2FE),
                outline = Color(0xFF4338CA)
            )
            AppThemeColor.ORANGE_FIRE -> darkColorScheme(
                primary = Color(0xFFF97316),
                onPrimary = Color.White,
                primaryContainer = Color(0xFF7C2D12),
                onPrimaryContainer = Color(0xFFFED7AA),
                background = Color(0xFF1C1917),
                surface = Color(0xFF292524),
                surfaceVariant = Color(0xFF44403C),
                onSurface = Color(0xFFFAAF90),
                onSurfaceVariant = Color(0xFFE7E5E4),
                outline = Color(0xFF78350F)
            )
            AppThemeColor.PINK_SAKURA -> darkColorScheme(
                primary = Color(0xFFF43F5E),
                onPrimary = Color.White,
                primaryContainer = Color(0xFF881337),
                onPrimaryContainer = Color(0xFFFECDD3),
                background = Color(0xFF18181B),
                surface = Color(0xFF27272A),
                surfaceVariant = Color(0xFF3F3F46),
                onSurface = Color(0xFFFAFAFA),
                onSurfaceVariant = Color(0xFFE4E4E7),
                outline = Color(0xFF52525B)
            )
            AppThemeColor.CYAN_NEON -> darkColorScheme(
                primary = Color(0xFF06B6D4),
                onPrimary = Color.Black,
                primaryContainer = Color(0xFF164E63),
                onPrimaryContainer = Color(0xFFA5F3FC),
                background = Color(0xFF083344),
                surface = Color(0xFF164E63),
                surfaceVariant = Color(0xFF155E75),
                onSurface = Color(0xFFECFEFF),
                onSurfaceVariant = Color(0xFFCFFAFE),
                outline = Color(0xFF0891B2)
            )
            AppThemeColor.TEAL_JADE -> darkColorScheme(
                primary = Color(0xFF14B8A6),
                onPrimary = Color.Black,
                primaryContainer = Color(0xFF134E4A),
                onPrimaryContainer = Color(0xFF99F6E4),
                background = Color(0xFF042F2C),
                surface = Color(0xFF134E4A),
                surfaceVariant = Color(0xFF115E59),
                onSurface = Color(0xFFF0FDFA),
                onSurfaceVariant = Color(0xFFCCFBF1),
                outline = Color(0xFF0D9488)
            )
            AppThemeColor.BROWN_PARCHMENT -> darkColorScheme(
                primary = Color(0xFFF59E0B),
                onPrimary = Color.Black,
                primaryContainer = Color(0xFF451A03),
                onPrimaryContainer = Color(0xFFFDE68A),
                background = Color(0xFF1C1917),
                surface = Color(0xFF292524),
                surfaceVariant = Color(0xFF44403C),
                onSurface = Color(0xFFFAFAF9),
                onSurfaceVariant = Color(0xFFE7E5E4),
                outline = Color(0xFF78350F)
            )
            AppThemeColor.INDIGO_ROYAL -> darkColorScheme(
                primary = Color(0xFF6366F1),
                onPrimary = Color.White,
                primaryContainer = Color(0xFF312E81),
                onPrimaryContainer = Color(0xFFC7D2FE),
                background = Color(0xFF0F172A),
                surface = Color(0xFF1E293B),
                surfaceVariant = Color(0xFF334155),
                onSurface = Color(0xFFF8FAFC),
                onSurfaceVariant = Color(0xFFCBD5E1),
                outline = Color(0xFF4F46E5)
            )
            AppThemeColor.SLATE_NOIR -> darkColorScheme(
                primary = Color(0xFF94A3B8),
                onPrimary = Color.Black,
                primaryContainer = Color(0xFF1E293B),
                onPrimaryContainer = Color(0xFFF1F5F9),
                background = Color(0xFF090D16),
                surface = Color(0xFF172033),
                surfaceVariant = Color(0xFF334155),
                onSurface = Color(0xFFF8FAFC),
                onSurfaceVariant = Color(0xFFCBD5E1),
                outline = Color(0xFF475569)
            )
        }
    } else {
        when (themeColor) {
            AppThemeColor.RED_SPIDER -> lightColorScheme(
                primary = Color(0xFFDC2626),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFEE2E2),
                onPrimaryContainer = Color(0xFF991B1B),
                background = Color(0xFFFEF2F2),
                surface = Color.White,
                surfaceVariant = Color(0xFFFECACA),
                onSurface = Color(0xFF450A0A),
                onSurfaceVariant = Color(0xFF7F1D1D),
                outline = Color(0xFFFCA5A5)
            )
            AppThemeColor.GREEN_LEAF -> lightColorScheme(
                primary = Color(0xFF059669),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFD1FAE5),
                onPrimaryContainer = Color(0xFF065F46),
                background = Color(0xFFF0FDF4),
                surface = Color.White,
                surfaceVariant = Color(0xFFA7F3D0),
                onSurface = Color(0xFF022C22),
                onSurfaceVariant = Color(0xFF064E3B),
                outline = Color(0xFF6EE7B7)
            )
            AppThemeColor.BLUE_OCEAN -> lightColorScheme(
                primary = Color(0xFF2563EB),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFDBEAFE),
                onPrimaryContainer = Color(0xFF1E40AF),
                background = Color(0xFFEFF6FF),
                surface = Color.White,
                surfaceVariant = Color(0xFFBFDBFE),
                onSurface = Color(0xFF1E3A8A),
                onSurfaceVariant = Color(0xFF1E40AF),
                outline = Color(0xFF93C5FD)
            )
            AppThemeColor.YELLOW_VOLT -> lightColorScheme(
                primary = Color(0xFFD97706),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFEF3C7),
                onPrimaryContainer = Color(0xFF92400E),
                background = Color(0xFFFFFBEB),
                surface = Color.White,
                surfaceVariant = Color(0xFFFDE68A),
                onSurface = Color(0xFF451A03),
                onSurfaceVariant = Color(0xFF78350F),
                outline = Color(0xFFFCD34D)
            )
            AppThemeColor.PURPLE_COSMOS -> lightColorScheme(
                primary = Color(0xFF7C3AED),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFEDE9FE),
                onPrimaryContainer = Color(0xFF5B21B6),
                background = Color(0xFFF5F3FF),
                surface = Color.White,
                surfaceVariant = Color(0xFFDDD6FE),
                onSurface = Color(0xFF2E1065),
                onSurfaceVariant = Color(0xFF4C1D95),
                outline = Color(0xFFC4B5FD)
            )
            AppThemeColor.ORANGE_FIRE -> lightColorScheme(
                primary = Color(0xFFEA580C),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFFEDD5),
                onPrimaryContainer = Color(0xFF9A3412),
                background = Color(0xFFFFF7ED),
                surface = Color.White,
                surfaceVariant = Color(0xFFFED7AA),
                onSurface = Color(0xFF431407),
                onSurfaceVariant = Color(0xFF7C2D12),
                outline = Color(0xFFFDBA74)
            )
            AppThemeColor.PINK_SAKURA -> lightColorScheme(
                primary = Color(0xFFE11D48),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFFE4E6),
                onPrimaryContainer = Color(0xFF9F1239),
                background = Color(0xFFFFF1F2),
                surface = Color.White,
                surfaceVariant = Color(0xFFFECDD3),
                onSurface = Color(0xFF4C0519),
                onSurfaceVariant = Color(0xFF881337),
                outline = Color(0xFFFDA4AF)
            )
            AppThemeColor.CYAN_NEON -> lightColorScheme(
                primary = Color(0xFF0891B2),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFCFFAFE),
                onPrimaryContainer = Color(0xFF155E75),
                background = Color(0xFFECFEFF),
                surface = Color.White,
                surfaceVariant = Color(0xFFA5F3FC),
                onSurface = Color(0xFF083344),
                onSurfaceVariant = Color(0xFF164E63),
                outline = Color(0xFF67E8F9)
            )
            AppThemeColor.TEAL_JADE -> lightColorScheme(
                primary = Color(0xFF0D9488),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFCCFBF1),
                onPrimaryContainer = Color(0xFF115E59),
                background = Color(0xFFF0FDFA),
                surface = Color.White,
                surfaceVariant = Color(0xFF99F6E4),
                onSurface = Color(0xFF042F2C),
                onSurfaceVariant = Color(0xFF134E4A),
                outline = Color(0xFF5EEAD4)
            )
            AppThemeColor.BROWN_PARCHMENT -> lightColorScheme(
                primary = Color(0xFF78350F),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFFEF3C7),
                onPrimaryContainer = Color(0xFF451A03),
                background = Color(0xFFFFFBEB),
                surface = Color(0xFFFFF7ED),
                surfaceVariant = Color(0xFFFDE68A),
                onSurface = Color(0xFF451A03),
                onSurfaceVariant = Color(0xFF78350F),
                outline = Color(0xFFFCD34D)
            )
            AppThemeColor.INDIGO_ROYAL -> lightColorScheme(
                primary = Color(0xFF4F46E5),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFE0E7FF),
                onPrimaryContainer = Color(0xFF3730A3),
                background = Color(0xFFEEF2FF),
                surface = Color.White,
                surfaceVariant = Color(0xFFC7D2FE),
                onSurface = Color(0xFF1E1B4B),
                onSurfaceVariant = Color(0xFF312E81),
                outline = Color(0xFFA5B4FC)
            )
            AppThemeColor.SLATE_NOIR -> lightColorScheme(
                primary = Color(0xFF475569),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFF1F5F9),
                onPrimaryContainer = Color(0xFF1E293B),
                background = Color(0xFFF8FAFC),
                surface = Color.White,
                surfaceVariant = Color(0xFFE2E8F0),
                onSurface = Color(0xFF0F172A),
                onSurfaceVariant = Color(0xFF334155),
                outline = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
fun SudokuTheme(
    themeColor: AppThemeColor = AppThemeColor.BLUE_OCEAN,
    isDarkMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = getThemeColorScheme(themeColor, isDarkMode)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkMode
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
