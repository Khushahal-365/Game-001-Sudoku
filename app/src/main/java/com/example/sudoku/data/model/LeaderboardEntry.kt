package com.example.sudoku.data.model

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val score: Int,
    val bestTimeSeconds: Long,
    val winCount: Int,
    val avatarEmoji: String,
    val avatarBgColorHex: Long,
    val isUser: Boolean = false,
    val badge: String = ""
)
