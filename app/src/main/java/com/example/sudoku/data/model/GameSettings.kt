package com.example.sudoku.data.model

enum class AppThemeColor(
    val displayName: String,
    val identityLabel: String,
    val iconEmoji: String,
    val primaryHex: Long
) {
    RED_SPIDER("Spider Red", "Hero Web", "🕸️", 0xFFDC2626),
    GREEN_LEAF("Forest Leaf", "Botanical Nature", "🍃", 0xFF059669),
    BLUE_OCEAN("Ocean Waves", "Deep Sapphire", "🌊", 0xFF2563EB),
    YELLOW_VOLT("Cyber Volt", "Electric Energy", "⚡", 0xFFD97706),
    PURPLE_COSMOS("Cosmos Void", "Galaxy Nebula", "🌌", 0xFF7C3AED),
    ORANGE_FIRE("Phoenix Fire", "Fiery Sunset", "🔥", 0xFFEA580C),
    PINK_SAKURA("Sakura Bloom", "Cherry Blossom", "🌸", 0xFFE11D48),
    CYAN_NEON("Neon Matrix", "Futuristic Ice", "🌐", 0xFF0891B2),
    TEAL_JADE("Tropical Jade", "Lagoon Waves", "🌿", 0xFF0D9488),
    BROWN_PARCHMENT("Vintage Wood", "Antique Cedar", "📜", 0xFF78350F),
    INDIGO_ROYAL("Midnight Royal", "Starlight Night", "🌙", 0xFF4F46E5),
    SLATE_NOIR("Monochrome Noir", "Minimal Slate", "🎬", 0xFF475569);

    companion object {
        fun fromString(value: String): AppThemeColor {
            return try {
                valueOf(value)
            } catch (e: Exception) {
                BLUE_OCEAN
            }
        }
    }
}

data class GameSettings(
    val themeColor: AppThemeColor = AppThemeColor.BLUE_OCEAN,
    val isDarkMode: Boolean = false,
    val highlightSameDigits: Boolean = true,
    val highlightConflicts: Boolean = true,
    val vibrateOnInput: Boolean = true,
    val showTimer: Boolean = true,
    val autoClearNotes: Boolean = true,
    val autoFillNotesEnabled: Boolean = false
)
