package com.example.sudoku.data.model

enum class Difficulty(val displayName: String, val minClues: Int, val maxClues: Int) {
    EASY("Easy", 36, 40),
    MEDIUM("Medium", 30, 35),
    HARD("Hard", 26, 29),
    EXPERT("Expert", 22, 25);

    companion object {
        fun fromString(value: String): Difficulty {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: MEDIUM
        }
    }
}
