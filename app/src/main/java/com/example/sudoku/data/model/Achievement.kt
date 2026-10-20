package com.example.sudoku.data.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean = false,
    val unlockedAtTimestamp: Long? = null
) {
    companion object {
        val ALL_ACHIEVEMENTS = listOf(
            Achievement("first_step", "First Step", "Complete your very first Sudoku puzzle", "star"),
            Achievement("easy_master", "Gentle Start", "Win a game on Easy difficulty", "check"),
            Achievement("medium_master", "Sharp Mind", "Win a game on Medium difficulty", "psychology"),
            Achievement("hard_master", "Puzzle Master", "Win a game on Hard difficulty", "military_tech"),
            Achievement("expert_master", "Grandmaster", "Conquer an Expert difficulty puzzle", "diamond"),
            Achievement("daily_devotee", "Daily Devotee", "Complete today's Daily Challenge", "today"),
            Achievement("streak_3", "Consistency", "Maintain a 3-day Daily Challenge streak", "local_fire_department"),
            Achievement("streak_7", "Unstoppable", "Reach a 7-day Daily Challenge streak", "bolt"),
            Achievement("flawless", "Flawless Logic", "Complete any puzzle with zero mistakes", "verified"),
            Achievement("no_hints", "Self Reliant", "Complete a puzzle without using any hints", "lightbulb")
        )
    }
}
