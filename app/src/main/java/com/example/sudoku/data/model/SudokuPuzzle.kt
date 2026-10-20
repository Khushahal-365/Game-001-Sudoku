package com.example.sudoku.data.model

data class SudokuPuzzle(
    val id: String,
    val seed: Long,
    val difficulty: Difficulty,
    val givens: IntArray, // 81 values, 0 for empty
    val solution: IntArray, // 81 values, 1-9
    val challengeDateKey: String? = null,
    val mode: GameMode = GameMode.CLASSIC,
    val cages: List<KillerCage> = emptyList(),
    val generatorVersion: Int = 1
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SudokuPuzzle
        if (id != other.id) return false
        if (!givens.contentEquals(other.givens)) return false
        if (!solution.contentEquals(other.solution)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + givens.contentHashCode()
        result = 31 * result + solution.contentHashCode()
        return result
    }
}
