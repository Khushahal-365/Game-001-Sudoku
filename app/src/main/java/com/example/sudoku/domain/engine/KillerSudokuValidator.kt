package com.example.sudoku.domain.engine

import com.example.sudoku.data.model.KillerCage

sealed class CageStatus {
    data class Valid(val currentSum: Int, val targetSum: Int, val isFull: Boolean) : CageStatus()
    data class DuplicateDigits(val duplicates: Set<Int>, val cellIndices: Set<Int>) : CageStatus()
    data class SumExceeded(val currentSum: Int, val targetSum: Int, val cellIndices: Set<Int>) : CageStatus()
    data class WrongSumWhenFull(val currentSum: Int, val targetSum: Int, val cellIndices: Set<Int>) : CageStatus()
    data class ImpossibleRemainingSum(val remainingSum: Int, val emptyCells: Int, val cellIndices: Set<Int>) : CageStatus()
}

class KillerSudokuValidator(private val baseValidator: SudokuValidator = SudokuValidator()) {

    /**
     * Comprehensive validation of a single cage against current board entries.
     */
    fun validateCage(cage: KillerCage, board: IntArray): CageStatus {
        val filled = ArrayList<Pair<Int, Int>>() // index to value
        val seen = HashMap<Int, MutableList<Int>>()
        var sum = 0

        for (idx in cage.cellIndices) {
            val v = board[idx]
            if (v in 1..9) {
                filled.add(idx to v)
                seen.getOrPut(v) { ArrayList() }.add(idx)
                sum += v
            }
        }

        // 1. Unique constraint: No repeated digits in a cage
        val duplicates = seen.filter { it.value.size > 1 }
        if (duplicates.isNotEmpty()) {
            val dupValues = duplicates.keys
            val dupIndices = duplicates.values.flatten().toSet()
            return CageStatus.DuplicateDigits(dupValues, dupIndices)
        }

        // 2. Sum constraint: Sum cannot exceed target
        if (sum > cage.targetSum) {
            return CageStatus.SumExceeded(sum, cage.targetSum, filled.map { it.first }.toSet())
        }

        // 3. Full cage sum constraint: When full, sum must strictly match targetSum
        val emptyCount = cage.size - filled.size
        if (emptyCount == 0) {
            return if (sum == cage.targetSum) {
                CageStatus.Valid(sum, cage.targetSum, isFull = true)
            } else {
                CageStatus.WrongSumWhenFull(sum, cage.targetSum, cage.cellIndices.toSet())
            }
        }

        // 4. Mathematical feasibility for remaining unfilled cells
        val remainingSum = cage.targetSum - sum
        val minPossibleRemaining = KillerCage.minPossibleSum(emptyCount)
        val maxPossibleRemaining = KillerCage.maxPossibleSum(emptyCount)

        if (remainingSum < minPossibleRemaining || remainingSum > maxPossibleRemaining) {
            return CageStatus.ImpossibleRemainingSum(remainingSum, emptyCount, filled.map { it.first }.toSet())
        }

        return CageStatus.Valid(sum, cage.targetSum, isFull = false)
    }

    /**
     * Checks if placing [digit] at [index] obeys both Sudoku and Killer cage rules.
     */
    fun isValidMove(board: IntArray, cage: KillerCage?, index: Int, digit: Int): Boolean {
        if (!baseValidator.isValidMove(board, index, digit)) return false
        if (cage == null) return true

        val placed = cage.placedValues(board)
        // Unique constraint in cage: cannot reuse digit
        if (placed.contains(digit)) return false

        val currentSum = cage.currentSum(board)
        val newSum = currentSum + digit

        // Sum cannot exceed target
        if (newSum > cage.targetSum) return false

        val unfilledAfterMove = cage.unfilledCellIndices(board).filter { it != index }
        if (unfilledAfterMove.isEmpty()) {
            return newSum == cage.targetSum
        }

        val remainingSum = cage.targetSum - newSum
        val remainingCount = unfilledAfterMove.size
        if (remainingSum < KillerCage.minPossibleSum(remainingCount)) return false
        if (remainingSum > KillerCage.maxPossibleSum(remainingCount)) return false

        return true
    }

    /**
     * Collects all cell indices across all cages that violate any cage constraint.
     */
    fun getAllCageConflictIndices(board: IntArray, cages: List<KillerCage>): Set<Int> {
        val conflicts = HashSet<Int>()
        for (cage in cages) {
            when (val status = validateCage(cage, board)) {
                is CageStatus.DuplicateDigits -> conflicts.addAll(status.cellIndices)
                is CageStatus.SumExceeded -> conflicts.addAll(status.cellIndices)
                is CageStatus.WrongSumWhenFull -> conflicts.addAll(status.cellIndices)
                is CageStatus.ImpossibleRemainingSum -> conflicts.addAll(status.cellIndices)
                is CageStatus.Valid -> Unit
            }
        }
        return conflicts
    }

    /**
     * Confirms the entire Killer Sudoku board is complete and valid.
     */
    fun isKillerBoardCompleteAndValid(board: IntArray, cages: List<KillerCage>): Boolean {
        if (!baseValidator.isBoardFull(board)) return false
        if (baseValidator.findAllConflicts(board).isNotEmpty()) return false

        for (cage in cages) {
            if (!cage.isSatisfied(board)) return false
        }
        return true
    }
}
