package com.example.sudoku.domain.engine

sealed class ValidationResult {
    data object CompleteValid : ValidationResult()
    data class CompleteInvalid(val conflictingIndices: Set<Int>) : ValidationResult()
    data object IncompleteValid : ValidationResult()
    data class IncompleteWithConflicts(val conflictingIndices: Set<Int>) : ValidationResult()
}

class SudokuValidator {

    /**
     * Checks if placing [digit] at [index] is valid on the given board (no row, column, or box collision).
     */
    fun isValidMove(board: IntArray, index: Int, digit: Int): Boolean {
        if (digit !in 1..9 || index !in 0..80) return false
        val row = index / 9
        val col = index % 9

        // Check Row
        val rStart = row * 9
        for (c in 0 until 9) {
            val i = rStart + c
            if (i != index && board[i] == digit) return false
        }

        // Check Column
        for (r in 0 until 9) {
            val i = r * 9 + col
            if (i != index && board[i] == digit) return false
        }

        // Check 3x3 Box
        val boxRow = (row / 3) * 3
        val boxCol = (col / 3) * 3
        for (r in 0 until 3) {
            for (c in 0 until 3) {
                val i = (boxRow + r) * 9 + (boxCol + c)
                if (i != index && board[i] == digit) return false
            }
        }

        return true
    }

    /**
     * Finds all cell indices in the board that are currently involved in a conflict (duplicate digits in row, col, or box).
     */
    fun findAllConflicts(board: IntArray): Set<Int> {
        val conflicts = HashSet<Int>()

        // Rows
        for (r in 0 until 9) {
            val seen = HashMap<Int, MutableList<Int>>()
            for (c in 0 until 9) {
                val i = r * 9 + c
                val v = board[i]
                if (v in 1..9) {
                    seen.getOrPut(v) { ArrayList() }.add(i)
                }
            }
            for (indices in seen.values) {
                if (indices.size > 1) conflicts.addAll(indices)
            }
        }

        // Columns
        for (c in 0 until 9) {
            val seen = HashMap<Int, MutableList<Int>>()
            for (r in 0 until 9) {
                val i = r * 9 + c
                val v = board[i]
                if (v in 1..9) {
                    seen.getOrPut(v) { ArrayList() }.add(i)
                }
            }
            for (indices in seen.values) {
                if (indices.size > 1) conflicts.addAll(indices)
            }
        }

        // 3x3 Boxes
        for (br in 0 until 3) {
            for (bc in 0 until 3) {
                val seen = HashMap<Int, MutableList<Int>>()
                val startRow = br * 3
                val startCol = bc * 3
                for (r in 0 until 3) {
                    for (c in 0 until 3) {
                        val i = (startRow + r) * 9 + (startCol + c)
                        val v = board[i]
                        if (v in 1..9) {
                            seen.getOrPut(v) { ArrayList() }.add(i)
                        }
                    }
                }
                for (indices in seen.values) {
                    if (indices.size > 1) conflicts.addAll(indices)
                }
            }
        }

        return conflicts
    }

    /**
     * Finds all cell indices in Killer cages that violate cage rules (duplicate numbers, sum exceeded, or full cage with wrong sum).
     */
    fun findKillerCageConflicts(board: IntArray, cages: List<com.example.sudoku.data.model.KillerCage>): Set<Int> {
        val cageConflicts = HashSet<Int>()

        for (cage in cages) {
            val filled = ArrayList<Pair<Int, Int>>()
            var currentSum = 0

            for (idx in cage.cellIndices) {
                val v = board[idx]
                if (v in 1..9) {
                    filled.add(idx to v)
                    currentSum += v
                }
            }

            // Check 1: Duplicates inside cage
            val duplicateIndices = filled.groupBy { it.second }
                .filter { it.value.size > 1 }
                .flatMap { it.value.map { p -> p.first } }
            cageConflicts.addAll(duplicateIndices)

            // Check 2: Sum already exceeds target
            if (currentSum > cage.targetSum) {
                cageConflicts.addAll(filled.map { it.first })
            }

            // Check 3: Cage completely filled but sum doesn't match
            if (filled.size == cage.cellIndices.size && currentSum != cage.targetSum) {
                cageConflicts.addAll(filled.map { it.first })
            }
        }

        return cageConflicts
    }

    fun findAllConflicts(board: IntArray, cages: List<com.example.sudoku.data.model.KillerCage> = emptyList()): Set<Int> {
        val standard = findAllConflicts(board).toMutableSet()
        if (cages.isNotEmpty()) {
            standard.addAll(findKillerCageConflicts(board, cages))
        }
        return standard
    }

    /**
     * Checks if board has all 81 cells filled.
     */
    fun isBoardFull(board: IntArray): Boolean {
        for (i in 0 until 81) {
            if (board[i] !in 1..9) return false
        }
        return true
    }

    /**
     * Full evaluation of the board state.
     */
    fun evaluateBoard(board: IntArray, cages: List<com.example.sudoku.data.model.KillerCage> = emptyList()): ValidationResult {
        val conflicts = findAllConflicts(board, cages)
        val full = isBoardFull(board)

        return when {
            full && conflicts.isEmpty() -> ValidationResult.CompleteValid
            full && conflicts.isNotEmpty() -> ValidationResult.CompleteInvalid(conflicts)
            !full && conflicts.isEmpty() -> ValidationResult.IncompleteValid
            else -> ValidationResult.IncompleteWithConflicts(conflicts)
        }
    }
}
