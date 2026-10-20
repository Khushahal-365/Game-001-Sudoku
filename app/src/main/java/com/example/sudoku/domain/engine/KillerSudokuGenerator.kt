package com.example.sudoku.domain.engine

import com.example.sudoku.data.model.Difficulty
import com.example.sudoku.data.model.GameMode
import com.example.sudoku.data.model.KillerCage
import com.example.sudoku.data.model.SudokuPuzzle
import java.util.Random

class KillerSudokuGenerator(
    private val classicGenerator: SudokuGenerator = SudokuGenerator()
) {

    fun generate(difficulty: Difficulty, seed: Long = System.currentTimeMillis()): SudokuPuzzle {
        val random = Random(seed)

        // Generate a base full 9x9 solution
        val basePuzzle = classicGenerator.generate(difficulty, seed)
        val solution = basePuzzle.solution.clone()

        // Partition 81 cells into cages
        val cages = partitionIntoCages(solution, random)

        // Decide how many givens to retain based on difficulty
        val givenCount = when (difficulty) {
            Difficulty.EASY -> random.nextInt(4) + 14   // 14-17 givens
            Difficulty.MEDIUM -> random.nextInt(4) + 8  // 8-11 givens
            Difficulty.HARD -> random.nextInt(3) + 4    // 4-6 givens
            Difficulty.EXPERT -> random.nextInt(2)      // 0-1 givens
        }

        val givens = IntArray(81) { 0 }
        val candidateIndices = (0 until 81).shuffled(kotlin.random.Random(random.nextLong()))
        for (i in 0 until givenCount) {
            val idx = candidateIndices[i]
            givens[idx] = solution[idx]
        }

        return SudokuPuzzle(
            id = "killer_${difficulty.name.lowercase()}_${seed}",
            seed = seed,
            difficulty = difficulty,
            givens = givens,
            solution = solution,
            mode = GameMode.KILLER,
            cages = cages
        )
    }

    fun generateDailyChallenge(dateKey: String): SudokuPuzzle {
        val seed = dateKey.hashCode().toLong()
        val puzzle = generate(Difficulty.MEDIUM, seed)
        return puzzle.copy(challengeDateKey = dateKey)
    }

    private fun partitionIntoCages(solution: IntArray, random: Random): List<KillerCage> {
        val cageIdOfCell = IntArray(81) { -1 }
        val cages = ArrayList<KillerCage>()
        var nextCageId = 1

        val allIndices = (0 until 81).shuffled(kotlin.random.Random(random.nextLong())).toMutableList()

        for (startIndex in allIndices) {
            if (cageIdOfCell[startIndex] != -1) continue

            // Desired cage size: mostly 2 or 3, sometimes 4
            val targetSize = when (random.nextInt(10)) {
                in 0..4 -> 2 // 50%
                in 5..8 -> 3 // 40%
                else -> 4    // 10%
            }

            val cageCells = mutableListOf(startIndex)
            val cageValues = mutableSetOf(solution[startIndex])
            cageIdOfCell[startIndex] = nextCageId

            // Grow cage to adjacent unassigned cells
            while (cageCells.size < targetSize) {
                // Find all valid neighbors
                val neighbors = ArrayList<Int>()
                for (cell in cageCells) {
                    val r = cell / 9
                    val c = cell % 9
                    val adj = listOf(
                        if (r > 0) (r - 1) * 9 + c else -1,
                        if (r < 8) (r + 1) * 9 + c else -1,
                        if (c > 0) r * 9 + (c - 1) else -1,
                        if (c < 8) r * 9 + (c + 1) else -1
                    )
                    for (nb in adj) {
                        if (nb != -1 && cageIdOfCell[nb] == -1 && !cageCells.contains(nb)) {
                            // Constraint: Numbers cannot repeat within a cage
                            if (!cageValues.contains(solution[nb])) {
                                neighbors.add(nb)
                            }
                        }
                    }
                }

                if (neighbors.isEmpty()) break

                val chosen = neighbors[random.nextInt(neighbors.size)]
                cageCells.add(chosen)
                cageValues.add(solution[chosen])
                cageIdOfCell[chosen] = nextCageId
            }

            val sum = cageCells.sumOf { solution[it] }
            cages.add(KillerCage(id = nextCageId++, targetSum = sum, cellIndices = cageCells))
        }

        // Post-process: If any single-cell cage remains, merge it into an adjacent cage if no duplicate
        val resultCages = ArrayList<KillerCage>()
        val singletons = cages.filter { it.cellIndices.size == 1 }
        val multiCells = cages.filter { it.cellIndices.size > 1 }.toMutableList()

        for (single in singletons) {
            val cell = single.cellIndices[0]
            val valInCell = solution[cell]
            val r = cell / 9
            val c = cell % 9
            val adj = listOf(
                if (r > 0) (r - 1) * 9 + c else -1,
                if (r < 8) (r + 1) * 9 + c else -1,
                if (c > 0) r * 9 + (c - 1) else -1,
                if (c < 8) r * 9 + (c + 1) else -1
            ).filter { it != -1 }

            var merged = false
            for (neighbor in adj) {
                val targetCageIdx = multiCells.indexOfFirst { it.cellIndices.contains(neighbor) }
                if (targetCageIdx != -1) {
                    val targetCage = multiCells[targetCageIdx]
                    if (targetCage.cellIndices.size < 5) {
                        val hasSameVal = targetCage.cellIndices.any { solution[it] == valInCell }
                        if (!hasSameVal) {
                            multiCells[targetCageIdx] = targetCage.copy(
                                targetSum = targetCage.targetSum + valInCell,
                                cellIndices = targetCage.cellIndices + cell
                            )
                            merged = true
                            break
                        }
                    }
                }
            }
            if (!merged) {
                resultCages.add(single)
            }
        }
        resultCages.addAll(multiCells)

        return resultCages
    }
}
