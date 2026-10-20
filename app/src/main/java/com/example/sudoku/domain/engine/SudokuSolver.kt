package com.example.sudoku.domain.engine

class SudokuSolver {

    fun isValidPlacement(board: IntArray, index: Int, value: Int): Boolean {
        if (value !in 1..9) return false
        val row = index / 9
        val col = index % 9

        // Check row
        val rowStart = row * 9
        for (c in 0 until 9) {
            val i = rowStart + c
            if (i != index && board[i] == value) return false
        }

        // Check column
        for (r in 0 until 9) {
            val i = r * 9 + col
            if (i != index && board[i] == value) return false
        }

        // Check 3x3 box
        val boxRowStart = (row / 3) * 3
        val boxColStart = (col / 3) * 3
        for (r in 0 until 3) {
            for (c in 0 until 3) {
                val i = (boxRowStart + r) * 9 + (boxColStart + c)
                if (i != index && board[i] == value) return false
            }
        }

        return true
    }

    fun getValidCandidates(board: IntArray, index: Int): List<Int> {
        if (board[index] != 0) return emptyList()
        val candidates = ArrayList<Int>(9)
        for (v in 1..9) {
            if (isValidPlacement(board, index, v)) {
                candidates.add(v)
            }
        }
        return candidates
    }

    /**
     * Counts solutions up to [limit].
     * If limit is 2, stops as soon as 2 solutions are found (guaranteeing uniqueness check is fast).
     */
    fun countSolutions(board: IntArray, limit: Int = 2): Int {
        val workingBoard = board.clone()
        return solveAndCount(workingBoard, limit, 0)
    }

    private fun solveAndCount(board: IntArray, limit: Int, currentCount: Int): Int {
        if (currentCount >= limit) return currentCount

        // Find cell with fewest candidates (MRV heuristic)
        var minCandidates = 10
        var bestIndex = -1
        var bestCandidates: List<Int>? = null

        for (i in 0 until 81) {
            if (board[i] == 0) {
                val candidates = getValidCandidates(board, i)
                if (candidates.isEmpty()) {
                    return currentCount // Dead end, no solution on this branch
                }
                if (candidates.size < minCandidates) {
                    minCandidates = candidates.size
                    bestIndex = i
                    bestCandidates = candidates
                    if (minCandidates == 1) break // Cannot get fewer than 1
                }
            }
        }

        // All cells filled
        if (bestIndex == -1) {
            return currentCount + 1
        }

        var count = currentCount
        for (candidate in bestCandidates!!) {
            board[bestIndex] = candidate
            count = solveAndCount(board, limit, count)
            board[bestIndex] = 0
            if (count >= limit) break
        }

        return count
    }

    /**
     * Finds a single valid solution for the board, or returns null if none exists.
     */
    fun solve(board: IntArray): IntArray? {
        val working = board.clone()
        if (solveHelper(working)) {
            return working
        }
        return null
    }

    private fun solveHelper(board: IntArray): Boolean {
        var minCandidates = 10
        var bestIndex = -1
        var bestCandidates: List<Int>? = null

        for (i in 0 until 81) {
            if (board[i] == 0) {
                val candidates = getValidCandidates(board, i)
                if (candidates.isEmpty()) return false
                if (candidates.size < minCandidates) {
                    minCandidates = candidates.size
                    bestIndex = i
                    bestCandidates = candidates
                    if (minCandidates == 1) break
                }
            }
        }

        if (bestIndex == -1) return true

        for (candidate in bestCandidates!!) {
            board[bestIndex] = candidate
            if (solveHelper(board)) return true
            board[bestIndex] = 0
        }

        return false
    }

    fun isBoardCompleteAndValid(board: IntArray): Boolean {
        for (i in 0 until 81) {
            val v = board[i]
            if (v !in 1..9) return false
            if (!isValidPlacement(board, i, v)) return false
        }
        return true
    }
}
