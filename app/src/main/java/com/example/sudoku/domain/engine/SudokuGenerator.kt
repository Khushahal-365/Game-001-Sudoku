package com.example.sudoku.domain.engine

import com.example.sudoku.data.model.Difficulty
import com.example.sudoku.data.model.SudokuPuzzle
import java.util.Random

class SudokuGenerator(private val solver: SudokuSolver = SudokuSolver()) {

    fun generate(difficulty: Difficulty, seed: Long = System.currentTimeMillis()): SudokuPuzzle {
        val random = Random(seed)

        // Attempt generation up to 5 times
        for (attempt in 0 until 5) {
            val fullBoard = generateFullBoard(random)
            val solution = fullBoard.clone()
            val puzzleBoard = fullBoard.clone()

            // Random cell removal order
            val cellIndices = (0 until 81).shuffled(kotlin.random.Random(random.nextLong()))

            val targetClues = when (difficulty) {
                Difficulty.EASY -> random.nextInt(difficulty.maxClues - difficulty.minClues + 1) + difficulty.minClues
                Difficulty.MEDIUM -> random.nextInt(difficulty.maxClues - difficulty.minClues + 1) + difficulty.minClues
                Difficulty.HARD -> random.nextInt(difficulty.maxClues - difficulty.minClues + 1) + difficulty.minClues
                Difficulty.EXPERT -> random.nextInt(difficulty.maxClues - difficulty.minClues + 1) + difficulty.minClues
            }

            var currentClues = 81
            for (idx in cellIndices) {
                if (currentClues <= targetClues) break

                val original = puzzleBoard[idx]
                puzzleBoard[idx] = 0

                // Confirm uniqueness: must have exactly 1 solution
                if (solver.countSolutions(puzzleBoard, limit = 2) == 1) {
                    currentClues--
                } else {
                    // Restoring clue because removal resulted in 0 or >1 solutions
                    puzzleBoard[idx] = original
                }
            }

            if (currentClues <= difficulty.maxClues + 2) {
                return SudokuPuzzle(
                    id = "gen_${difficulty.name.lowercase()}_${seed}_$attempt",
                    seed = seed,
                    difficulty = difficulty,
                    givens = puzzleBoard,
                    solution = solution
                )
            }
        }

        // Bounded fallback
        return Fallbacks.getFallbackPuzzle(difficulty, seed)
    }

    fun generateDailyChallenge(dateKey: String): SudokuPuzzle {
        // Deterministic seed based on dateKey (e.g. "2026-10-19")
        val seed = dateKey.hashCode().toLong() and 0xFFFFFFFFL
        val puzzle = generate(Difficulty.MEDIUM, seed)
        return puzzle.copy(
            id = "daily_$dateKey",
            challengeDateKey = dateKey
        )
    }

    private fun generateFullBoard(random: Random): IntArray {
        val board = IntArray(81)

        // Fill diagonal 3x3 blocks (0, 4, 8 in block terms)
        fillBox(board, 0, 0, random)
        fillBox(board, 3, 3, random)
        fillBox(board, 6, 6, random)

        // Solve the remaining cells using backtracking with random choice
        fillRemaining(board, 0, 3, random)
        return board
    }

    private fun fillBox(board: IntArray, startRow: Int, startCol: Int, random: Random) {
        val digits = (1..9).shuffled(kotlin.random.Random(random.nextLong()))
        var idx = 0
        for (r in 0 until 3) {
            for (c in 0 until 3) {
                val index = (startRow + r) * 9 + (startCol + c)
                board[index] = digits[idx++]
            }
        }
    }

    private fun fillRemaining(board: IntArray, row: Int, col: Int, random: Random): Boolean {
        var r = row
        var c = col

        if (c >= 9 && r < 8) {
            r++
            c = 0
        }
        if (r >= 9 && c >= 9) return true
        if (r < 3) {
            if (c < 3) c = 3
        } else if (r < 6) {
            if (c >= 3 && c < 6) c = 6
        } else {
            if (c >= 6) {
                r++
                c = 0
                if (r >= 9) return true
            }
        }

        val index = r * 9 + c
        if (index >= 81) return true
        if (board[index] != 0) {
            return fillRemaining(board, r, c + 1, random)
        }

        val candidates = (1..9).shuffled(kotlin.random.Random(random.nextLong()))
        for (num in candidates) {
            if (solver.isValidPlacement(board, index, num)) {
                board[index] = num
                if (fillRemaining(board, r, c + 1, random)) return true
                board[index] = 0
            }
        }

        return false
    }
}
