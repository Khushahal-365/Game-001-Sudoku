package com.example.sudoku.data.model

data class DifficultyStats(
    val gamesStarted: Int = 0,
    val gamesWon: Int = 0,
    val bestTimeSeconds: Long = 0L,
    val totalTimeSeconds: Long = 0L
) {
    val winRatePercent: Int
        get() = if (gamesStarted > 0) (gamesWon * 100) / gamesStarted else 0

    val averageTimeSeconds: Long
        get() = if (gamesWon > 0) totalTimeSeconds / gamesWon else 0L
}

data class OverallStatistics(
    val totalStarted: Int = 0,
    val totalWon: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastCompletedDateKey: String? = null,
    val easyStats: DifficultyStats = DifficultyStats(),
    val mediumStats: DifficultyStats = DifficultyStats(),
    val hardStats: DifficultyStats = DifficultyStats(),
    val expertStats: DifficultyStats = DifficultyStats()
) {
    fun statsFor(difficulty: Difficulty): DifficultyStats {
        return when (difficulty) {
            Difficulty.EASY -> easyStats
            Difficulty.MEDIUM -> mediumStats
            Difficulty.HARD -> hardStats
            Difficulty.EXPERT -> expertStats
        }
    }
}
