package com.example.sudoku.data.model

data class BoardMove(
    val index: Int,
    val previousValue: Int,
    val newValue: Int,
    val previousNotes: Set<Int>,
    val newNotes: Set<Int>,
    val clearedNotesMap: Map<Int, Set<Int>> = emptyMap()
)

data class GameState(
    val puzzle: SudokuPuzzle,
    val entries: IntArray, // 81 values, user entered + givens
    val notes: List<Set<Int>>, // 81 sets of notes (1-9)
    val elapsedSeconds: Long = 0L,
    val mistakes: Int = 0,
    val hintsUsed: Int = 0,
    val isCompleted: Boolean = false,
    val isPaused: Boolean = false,
    val selectedCellIndex: Int = -1,
    val isNotesMode: Boolean = false,
    val undoStack: List<BoardMove> = emptyList(),
    val redoStack: List<BoardMove> = emptyList()
) {
    fun isGiven(index: Int): Boolean {
        if (index !in 0..80) return false
        return puzzle.givens[index] != 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState
        if (puzzle != other.puzzle) return false
        if (!entries.contentEquals(other.entries)) return false
        if (notes != other.notes) return false
        if (elapsedSeconds != other.elapsedSeconds) return false
        if (mistakes != other.mistakes) return false
        if (hintsUsed != other.hintsUsed) return false
        if (isCompleted != other.isCompleted) return false
        if (isPaused != other.isPaused) return false
        if (selectedCellIndex != other.selectedCellIndex) return false
        if (isNotesMode != other.isNotesMode) return false
        return true
    }

    override fun hashCode(): Int {
        var result = puzzle.hashCode()
        result = 31 * result + entries.contentHashCode()
        result = 31 * result + notes.hashCode()
        result = 31 * result + elapsedSeconds.hashCode()
        result = 31 * result + mistakes
        result = 31 * result + hintsUsed
        result = 31 * result + isCompleted.hashCode()
        return result
    }
}
