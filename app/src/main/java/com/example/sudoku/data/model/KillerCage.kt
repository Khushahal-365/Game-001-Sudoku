package com.example.sudoku.data.model

data class KillerCage(
    val id: Int,
    val targetSum: Int,
    val cellIndices: List<Int>
) {
    val size: Int get() = cellIndices.size

    val topLeftCellIndex: Int by lazy {
        cellIndices.minWithOrNull(compareBy({ it / 9 }, { it % 9 })) ?: (cellIndices.firstOrNull() ?: 0)
    }

    fun contains(index: Int): Boolean = cellIndices.contains(index)

    /**
     * Returns all placed non-zero digits currently on the board for this cage.
     */
    fun placedValues(board: IntArray): List<Int> {
        val list = ArrayList<Int>(cellIndices.size)
        for (idx in cellIndices) {
            val v = board[idx]
            if (v in 1..9) list.add(v)
        }
        return list
    }

    /**
     * Sum of currently placed digits in this cage.
     */
    fun currentSum(board: IntArray): Int {
        var sum = 0
        for (idx in cellIndices) {
            val v = board[idx]
            if (v in 1..9) sum += v
        }
        return sum
    }

    /**
     * Unfilled cell indices in this cage.
     */
    fun unfilledCellIndices(board: IntArray): List<Int> {
        return cellIndices.filter { board[it] !in 1..9 }
    }

    /**
     * True if all cells in this cage have non-zero digits.
     */
    fun isFull(board: IntArray): Boolean {
        for (idx in cellIndices) {
            if (board[idx] !in 1..9) return false
        }
        return true
    }

    /**
     * True if any duplicate digits are placed in this cage (violates unique constraint).
     */
    fun hasDuplicates(board: IntArray): Boolean {
        val seen = HashSet<Int>()
        for (idx in cellIndices) {
            val v = board[idx]
            if (v in 1..9) {
                if (!seen.add(v)) return true
            }
        }
        return false
    }

    /**
     * True if the current sum already exceeds targetSum.
     */
    fun isSumExceeded(board: IntArray): Boolean {
        return currentSum(board) > targetSum
    }

    /**
     * True if the cage is completely filled, has no duplicates, and its sum equals targetSum.
     */
    fun isSatisfied(board: IntArray): Boolean {
        return isFull(board) && !hasDuplicates(board) && currentSum(board) == targetSum
    }

    companion object {
        fun minPossibleSum(size: Int): Int {
            if (size <= 0) return 0
            return (size * (size + 1)) / 2
        }

        fun maxPossibleSum(size: Int): Int {
            if (size <= 0) return 0
            var sum = 0
            for (i in 0 until size) {
                sum += (9 - i)
            }
            return sum
        }

        /**
         * Returns all possible distinct digit sets of [size] that sum to [targetSum].
         */
        fun getPossibleCombinations(targetSum: Int, size: Int): List<Set<Int>> {
            val results = ArrayList<Set<Int>>()
            fun search(startDigit: Int, remainingCount: Int, remainingSum: Int, current: MutableList<Int>) {
                if (remainingCount == 0) {
                    if (remainingSum == 0) {
                        results.add(current.toSet())
                    }
                    return
                }
                if (remainingSum <= 0 || startDigit > 9) return

                val minSum = (startDigit until startDigit + remainingCount).sum()
                if (minSum > remainingSum) return

                for (d in startDigit..9) {
                    current.add(d)
                    search(d + 1, remainingCount - 1, remainingSum - d, current)
                    current.removeAt(current.size - 1)
                }
            }
            search(1, size, targetSum, ArrayList())
            return results
        }
    }
}
