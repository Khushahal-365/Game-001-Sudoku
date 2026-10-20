package com.example.sudoku.data.model

enum class GameMode(val displayName: String) {
    CLASSIC("Classic"),
    KILLER("Killer"),
    TIME_BOMB("Time Bomb");

    companion object {
        fun fromString(value: String): GameMode {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: CLASSIC
        }
    }
}
