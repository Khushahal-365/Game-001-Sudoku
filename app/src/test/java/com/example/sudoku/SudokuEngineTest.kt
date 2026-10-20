package com.example.sudoku

import com.example.sudoku.data.model.Difficulty
import com.example.sudoku.data.model.GameMode
import com.example.sudoku.data.model.KillerCage
import com.example.sudoku.domain.engine.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SudokuEngineTest {

    private lateinit var solver: SudokuSolver
    private lateinit var generator: SudokuGenerator
    private lateinit var killerGenerator: KillerSudokuGenerator
    private lateinit var validator: SudokuValidator

    @Before
    fun setUp() {
        solver = SudokuSolver()
        generator = SudokuGenerator(solver)
        killerGenerator = KillerSudokuGenerator(generator)
        validator = SudokuValidator()
    }

    @Test
    fun testFallbacksAreSolvableAndUnique() {
        for (difficulty in Difficulty.entries) {
            val fallback = Fallbacks.getFallbackPuzzle(difficulty)
            val solutions = solver.countSolutions(fallback.givens, limit = 2)
            assertEquals("Fallback for $difficulty should have exactly 1 solution", 1, solutions)

            val solved = solver.solve(fallback.givens)
            assertNotNull("Fallback should be solvable", solved)
            assertTrue("Solution must be valid and complete", solver.isBoardCompleteAndValid(solved!!))
        }
    }

    @Test
    fun testDailyChallengeIsDeterministic() {
        val dateKey = "2026-10-19"
        val puzzle1 = generator.generateDailyChallenge(dateKey)
        val puzzle2 = generator.generateDailyChallenge(dateKey)

        assertArrayEquals("Givens must match for same date", puzzle1.givens, puzzle2.givens)
        assertArrayEquals("Solutions must match for same date", puzzle1.solution, puzzle2.solution)
        assertEquals("Date key must match", dateKey, puzzle1.challengeDateKey)
    }

    @Test
    fun testGeneratedEasyPuzzleHasUniqueSolution() {
        val puzzle = generator.generate(Difficulty.EASY, seed = 12345L)
        val solutions = solver.countSolutions(puzzle.givens, limit = 2)
        assertEquals("Generated puzzle must have unique solution", 1, solutions)
    }

    @Test
    fun testValidatorDetectsConflictsAndValidMoves() {
        val fallback = Fallbacks.getFallbackPuzzle(Difficulty.EASY)
        val board = fallback.givens.clone()

        // Cell 2 (row 0, col 2) is empty in EASY fallback.
        // Givens in row 0 already have 5, 3, 7.
        // Placing 5 should be invalid
        assertFalse("Placing 5 in row that already has 5 must be invalid", validator.isValidMove(board, 2, 5))
        assertFalse("Placing 3 in row that already has 3 must be invalid", validator.isValidMove(board, 2, 3))
        assertFalse("Placing 7 in row that already has 7 must be invalid", validator.isValidMove(board, 2, 7))

        // True solution value at index 2 is 4
        val solutionVal = fallback.solution[2]
        assertTrue("Placing solution value 4 must be valid move", validator.isValidMove(board, 2, solutionVal))

        // If we force duplicate 5 at index 2, findAllConflicts should detect index 0 and index 2
        board[2] = 5
        val conflicts = validator.findAllConflicts(board)
        assertTrue("Conflict set must contain index 0", conflicts.contains(0))
        assertTrue("Conflict set must contain index 2", conflicts.contains(2))
    }

    @Test
    fun testValidatorEvaluatesCompleteBoard() {
        val fallback = Fallbacks.getFallbackPuzzle(Difficulty.EASY)
        val solvedBoard = fallback.solution.clone()

        val validEval = validator.evaluateBoard(solvedBoard)
        assertTrue("Complete correct board must evaluate to CompleteValid", validEval is ValidationResult.CompleteValid)

        // Corrupt one cell
        solvedBoard[0] = solvedBoard[1]
        val invalidEval = validator.evaluateBoard(solvedBoard)
        assertTrue("Corrupted board must evaluate to CompleteInvalid", invalidEval is ValidationResult.CompleteInvalid)
    }

    @Test
    fun testKillerSudokuGeneratorAndCageValidation() {
        val killerPuzzle = killerGenerator.generate(Difficulty.EASY, seed = 98765L)
        assertEquals("Mode must be KILLER", GameMode.KILLER, killerPuzzle.mode)
        assertTrue("Killer puzzle must have cages", killerPuzzle.cages.isNotEmpty())

        // Verify all 81 cells are accounted for in the cages
        val coveredCells = killerPuzzle.cages.flatMap { it.cellIndices }.toSet()
        assertEquals("All 81 cells must be covered by cages", 81, coveredCells.size)

        // Verify each cage targetSum equals the sum of solution values in that cage
        for (cage in killerPuzzle.cages) {
            val expectedSum = cage.cellIndices.sumOf { killerPuzzle.solution[it] }
            assertEquals("Cage targetSum must match solution sum", expectedSum, cage.targetSum)

            // Verify no duplicate digits inside the cage solution
            val cageValues = cage.cellIndices.map { killerPuzzle.solution[it] }
            assertEquals("No duplicate values in cage", cageValues.size, cageValues.distinct().size)
        }

        // Test cage conflict detection when a duplicate or sum violation is placed
        val board = IntArray(81) { 0 }
        val testCage = KillerCage(id = 1, targetSum = 10, cellIndices = listOf(0, 1))

        // Placing duplicate 5 in both cells of testCage
        board[0] = 5
        board[1] = 5
        val cageConflicts = validator.findKillerCageConflicts(board, listOf(testCage))
        assertTrue("Duplicate numbers in cage must be detected as conflict", cageConflicts.contains(0))
        assertTrue("Duplicate numbers in cage must be detected as conflict", cageConflicts.contains(1))

        // Placing values that exceed target sum (e.g. 6 + 5 = 11 > 10)
        board[0] = 6
        board[1] = 5
        val sumConflicts = validator.findKillerCageConflicts(board, listOf(testCage))
        assertTrue("Sum exceeding target must be detected as conflict", sumConflicts.contains(0))
        assertTrue("Sum exceeding target must be detected as conflict", sumConflicts.contains(1))

        // Placing valid matching sum (4 + 6 = 10)
        board[0] = 4
        board[1] = 6
        val validConflicts = validator.findKillerCageConflicts(board, listOf(testCage))
        assertTrue("Valid sum and distinct numbers should produce no conflicts", validConflicts.isEmpty())
    }

    @Test
    fun testKillerCageDataStructureAndValidationAlgorithms() {
        val killerValidator = KillerSudokuValidator(validator)

        // Test math bounds
        assertEquals(3, KillerCage.minPossibleSum(2)) // 1 + 2 = 3
        assertEquals(17, KillerCage.maxPossibleSum(2)) // 9 + 8 = 17
        assertEquals(6, KillerCage.minPossibleSum(3)) // 1 + 2 + 3 = 6
        assertEquals(24, KillerCage.maxPossibleSum(3)) // 9 + 8 + 7 = 24

        // Test combinations
        val combosFor3In2 = KillerCage.getPossibleCombinations(3, 2)
        assertEquals(1, combosFor3In2.size)
        assertEquals(setOf(1, 2), combosFor3In2[0])

        val combosFor17In2 = KillerCage.getPossibleCombinations(17, 2)
        assertEquals(1, combosFor17In2.size)
        assertEquals(setOf(8, 9), combosFor17In2[0])

        val cage = KillerCage(id = 1, targetSum = 12, cellIndices = listOf(0, 1, 2))
        val board = IntArray(81) { 0 }

        // 1. Partial valid entry: placed 3 at 0
        board[0] = 3
        var status = killerValidator.validateCage(cage, board)
        assertTrue("Partial sum should be valid", status is CageStatus.Valid && !status.isFull)
        assertEquals(3, (status as CageStatus.Valid).currentSum)

        // 2. Duplicate entry: placed another 3 at 1
        board[1] = 3
        status = killerValidator.validateCage(cage, board)
        assertTrue("Duplicate in cage must be detected", status is CageStatus.DuplicateDigits)
        assertEquals(setOf(0, 1), (status as CageStatus.DuplicateDigits).cellIndices)

        // 3. Exceeded sum: placed 8 at 0, 7 at 1 -> 15 > 12
        board[0] = 8
        board[1] = 7
        status = killerValidator.validateCage(cage, board)
        assertTrue("Sum exceeded must be detected", status is CageStatus.SumExceeded)

        // 4. Full cage with wrong sum: 3 + 4 + 2 = 9 != 12
        board[0] = 3
        board[1] = 4
        board[2] = 2
        status = killerValidator.validateCage(cage, board)
        assertTrue("Full cage with wrong sum must be detected", status is CageStatus.WrongSumWhenFull)

        // 5. Perfectly satisfied cage: 3 + 4 + 5 = 12
        board[2] = 5
        status = killerValidator.validateCage(cage, board)
        assertTrue("Proper cage must be Valid and isFull", status is CageStatus.Valid && status.isFull)
        assertTrue("cage.isSatisfied must be true", cage.isSatisfied(board))

        // Test move validity helper
        val freshBoard = IntArray(81) { 0 }
        freshBoard[0] = 3
        // Placing 3 again at index 1 violates unique constraint
        assertFalse(killerValidator.isValidMove(freshBoard, cage, 1, 3))
        // Placing 10 is invalid
        assertFalse(killerValidator.isValidMove(freshBoard, cage, 1, 10))
        // Placing 4 is valid
        assertTrue(killerValidator.isValidMove(freshBoard, cage, 1, 4))
    }
}
