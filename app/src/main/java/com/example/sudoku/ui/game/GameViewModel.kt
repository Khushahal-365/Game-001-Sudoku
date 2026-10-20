package com.example.sudoku.ui.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku.data.local.PreferencesManager
import com.example.sudoku.data.model.*
import com.example.sudoku.domain.engine.KillerSudokuGenerator
import com.example.sudoku.domain.engine.KillerSudokuValidator
import com.example.sudoku.domain.engine.SudokuGenerator
import com.example.sudoku.domain.engine.SudokuSolver
import com.example.sudoku.domain.engine.SudokuValidator
import com.example.sudoku.domain.engine.ValidationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsManager = PreferencesManager(application)
    private val generator = SudokuGenerator()
    private val killerGenerator = KillerSudokuGenerator(generator)
    private val solver = SudokuSolver()
    private val validator = SudokuValidator()
    private val killerValidator = KillerSudokuValidator(validator)

    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _conflictingIndices = MutableStateFlow<Set<Int>>(emptySet())
    val conflictingIndices: StateFlow<Set<Int>> = _conflictingIndices.asStateFlow()

    private val _settings = MutableStateFlow(prefsManager.loadSettings())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    private val _statistics = MutableStateFlow(prefsManager.loadStatistics())
    val statistics: StateFlow<OverallStatistics> = _statistics.asStateFlow()

    private val _unlockedAchievements = MutableStateFlow(prefsManager.getUnlockedAchievements())
    val unlockedAchievements: StateFlow<Map<String, Long>> = _unlockedAchievements.asStateFlow()

    private val _coins = MutableStateFlow(prefsManager.getCoins())
    val coins: StateFlow<Int> = _coins.asStateFlow()

    private val _playerName = MutableStateFlow(prefsManager.getPlayerName())
    val playerName: StateFlow<String> = _playerName.asStateFlow()

    private val _playerAvatarIndex = MutableStateFlow(prefsManager.getPlayerAvatarIndex())
    val playerAvatarIndex: StateFlow<Int> = _playerAvatarIndex.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver.asStateFlow()

    private val _activeGames = MutableStateFlow<Map<GameMode, GameState>>(prefsManager.loadAllActiveGames())
    val activeGames: StateFlow<Map<GameMode, GameState>> = _activeGames.asStateFlow()

    private var timerJob: Job? = null

    init {
        // Check for existing active games per mode
        val allGames = prefsManager.loadAllActiveGames()
        _activeGames.value = allGames
        val saved = prefsManager.loadActiveGame(GameMode.CLASSIC) ?: allGames.values.firstOrNull()
        if (saved != null && !saved.isCompleted) {
            _gameState.value = saved
            _conflictingIndices.value = validator.findAllConflicts(saved.entries, saved.puzzle.cages)
            startTimer()
        }
    }

    fun hasSavedGame(): Boolean {
        val current = _gameState.value
        return current != null && !current.isCompleted
    }

    fun resumeGameForMode(mode: GameMode) {
        val saved = prefsManager.loadActiveGame(mode) ?: _activeGames.value[mode]
        if (saved != null && !saved.isCompleted) {
            _gameState.value = saved
            _conflictingIndices.value = validator.findAllConflicts(saved.entries, saved.puzzle.cages)
            startTimer()
        }
    }

    fun refreshData() {
        _settings.value = prefsManager.loadSettings()
        _statistics.value = prefsManager.loadStatistics()
        _unlockedAchievements.value = prefsManager.getUnlockedAchievements()
        _coins.value = prefsManager.getCoins()
        _playerName.value = prefsManager.getPlayerName()
        _playerAvatarIndex.value = prefsManager.getPlayerAvatarIndex()
        _activeGames.value = prefsManager.loadAllActiveGames()
    }

    fun setPlayerName(name: String) {
        val clean = name.trim().ifEmpty { "You" }
        prefsManager.setPlayerName(clean)
        _playerName.value = clean
    }

    fun setPlayerAvatarIndex(index: Int) {
        prefsManager.setPlayerAvatarIndex(index)
        _playerAvatarIndex.value = index
    }

    fun startNewGame(difficulty: Difficulty, mode: GameMode = GameMode.CLASSIC) {
        viewModelScope.launch {
            _isLoading.value = true
            stopTimer()

            val puzzle = withContext(Dispatchers.Default) {
                val raw = if (mode == GameMode.KILLER) {
                    killerGenerator.generate(difficulty)
                } else {
                    generator.generate(difficulty)
                }
                raw.copy(mode = mode)
            }

            val entries = puzzle.givens.clone()
            val notes = List(81) { emptySet<Int>() }

            val initialSeconds = if (mode == GameMode.TIME_BOMB) {
                when (difficulty) {
                    Difficulty.EASY -> 600L
                    Difficulty.MEDIUM -> 720L
                    Difficulty.HARD -> 900L
                    Difficulty.EXPERT -> 1080L
                }
            } else 0L

            _isGameOver.value = false

            val newState = GameState(
                puzzle = puzzle,
                entries = entries,
                notes = notes,
                elapsedSeconds = initialSeconds,
                mistakes = 0,
                hintsUsed = 0,
                isCompleted = false,
                isPaused = false,
                selectedCellIndex = -1,
                isNotesMode = false
            )

            _gameState.value = newState
            _conflictingIndices.value = emptySet()
            _activeGames.update { it + (mode to newState) }
            prefsManager.recordGameStarted(difficulty)
            if (_settings.value.autoFillNotesEnabled) {
                autoFillNotes()
            } else {
                prefsManager.saveActiveGame(newState)
            }
            _statistics.value = prefsManager.loadStatistics()

            _isLoading.value = false
            startTimer()
        }
    }

    fun getSelectedCageSumInfo(): Pair<Int, Int>? {
        val current = _gameState.value ?: return null
        val idx = current.selectedCellIndex
        if (idx !in 0..80) return null
        val cage = current.puzzle.cages.find { it.contains(idx) } ?: return null
        val sum = cage.cellIndices.sumOf { current.entries[it] }
        return sum to cage.targetSum
    }

    fun getSelectedCageStatus(): com.example.sudoku.domain.engine.CageStatus? {
        val current = _gameState.value ?: return null
        val idx = current.selectedCellIndex
        if (idx !in 0..80) return null
        val cage = current.puzzle.cages.find { it.contains(idx) } ?: return null
        return killerValidator.validateCage(cage, current.entries)
    }

    fun isTodayDailyCompleted(mode: GameMode = GameMode.CLASSIC): Boolean {
        val today = getTodayUtcDateKey()
        return prefsManager.getCompletedDailyDates(mode).contains(today)
    }

    fun getCompletedDailyDates(mode: GameMode = GameMode.CLASSIC): Set<String> {
        return prefsManager.getCompletedDailyDates(mode)
    }

    fun startDailyChallenge(mode: GameMode = GameMode.CLASSIC) {
        startDailyChallengeForDate(getTodayUtcDateKey(), mode)
    }

    fun startDailyChallengeForDate(dateKey: String, mode: GameMode = GameMode.CLASSIC) {
        val todayKey = getTodayUtcDateKey()
        if (dateKey > todayKey) {
            return
        }
        val current = _gameState.value
        if (current != null && current.puzzle.challengeDateKey == dateKey && current.puzzle.mode == mode && !current.isCompleted) {
            startTimer()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            stopTimer()

            val puzzle = withContext(Dispatchers.Default) {
                val raw = if (mode == GameMode.KILLER) {
                    killerGenerator.generateDailyChallenge(dateKey)
                } else {
                    generator.generateDailyChallenge(dateKey)
                }
                raw.copy(mode = mode)
            }

            val entries = puzzle.givens.clone()
            val notes = List(81) { emptySet<Int>() }

            val initialSeconds = if (mode == GameMode.TIME_BOMB) 720L else 0L

            _isGameOver.value = false

            val newState = GameState(
                puzzle = puzzle,
                entries = entries,
                notes = notes,
                elapsedSeconds = initialSeconds,
                mistakes = 0,
                hintsUsed = 0,
                isCompleted = false,
                isPaused = false,
                selectedCellIndex = -1,
                isNotesMode = false
            )

            _gameState.value = newState
            _conflictingIndices.value = emptySet()
            _activeGames.update { it + (mode to newState) }
            prefsManager.recordGameStarted(puzzle.difficulty)
            if (_settings.value.autoFillNotesEnabled) {
                autoFillNotes()
            } else {
                prefsManager.saveActiveGame(newState)
            }
            _statistics.value = prefsManager.loadStatistics()

            _isLoading.value = false
            startTimer()
        }
    }

    fun selectCell(index: Int) {
        if (_isGameOver.value) return
        _gameState.update { current ->
            current?.copy(selectedCellIndex = index)
        }
    }

    fun toggleNotesMode() {
        if (_isGameOver.value) return
        _gameState.update { current ->
            current?.copy(isNotesMode = !current.isNotesMode)
        }
    }

    fun inputDigit(digit: Int) {
        val current = _gameState.value ?: return
        if (_isGameOver.value || current.isCompleted || current.isPaused) return
        val index = current.selectedCellIndex
        if (index !in 0..80) return
        if (current.isGiven(index)) return

        if (current.isNotesMode) {
            // Toggle pencil mark
            val currentNotes = current.notes[index].toMutableSet()
            if (currentNotes.contains(digit)) {
                currentNotes.remove(digit)
            } else {
                currentNotes.add(digit)
            }
            val newNotesList = current.notes.toMutableList()
            newNotesList[index] = currentNotes

            val move = BoardMove(
                index = index,
                previousValue = current.entries[index],
                newValue = current.entries[index],
                previousNotes = current.notes[index],
                newNotes = currentNotes
            )

            val updated = current.copy(
                notes = newNotesList,
                undoStack = current.undoStack + move,
                redoStack = emptyList()
            )
            _gameState.value = updated
            prefsManager.saveActiveGame(updated)
            return
        }

        // Normal digit entry
        val previousVal = current.entries[index]
        if (previousVal == digit) {
            // Re-tapping same digit can clear it
            eraseCell()
            return
        }

        val isMistake = digit != current.puzzle.solution[index]
        val newEntries = current.entries.clone()
        newEntries[index] = digit

        val previousNotes = current.notes[index]
        val newNotesList = current.notes.toMutableList()
        newNotesList[index] = emptySet() // Clear notes in this cell when digit entered

        val clearedMap = if (_settings.value.autoClearNotes && !isMistake) {
            clearNotesInRelatedCells(newNotesList, index, digit)
        } else {
            emptyMap()
        }

        val move = BoardMove(
            index = index,
            previousValue = previousVal,
            newValue = digit,
            previousNotes = previousNotes,
            newNotes = emptySet(),
            clearedNotesMap = clearedMap
        )

        var newMistakes = current.mistakes
        if (isMistake && _settings.value.highlightConflicts) {
            newMistakes++
        }

        var newSeconds = current.elapsedSeconds
        if (current.puzzle.mode == GameMode.TIME_BOMB) {
            if (isMistake) {
                newSeconds = (newSeconds - 30L).coerceAtLeast(0L)
                if (newSeconds == 0L) {
                    _isGameOver.value = true
                    stopTimer()
                }
            } else {
                newSeconds += 10L
            }
        }

        val updated = current.copy(
            entries = newEntries,
            notes = newNotesList,
            elapsedSeconds = newSeconds,
            mistakes = newMistakes,
            undoStack = current.undoStack + move,
            redoStack = emptyList()
        )

        setUpdatedGameState(updated, checkCompletion = true)
    }

    fun eraseCell() {
        val current = _gameState.value ?: return
        if (_isGameOver.value || current.isCompleted || current.isPaused) return
        val index = current.selectedCellIndex
        if (index !in 0..80) return
        if (current.isGiven(index)) return

        val prevVal = current.entries[index]
        val prevNotes = current.notes[index]
        if (prevVal == 0 && prevNotes.isEmpty()) return

        val newEntries = current.entries.clone()
        newEntries[index] = 0

        val newNotes = current.notes.toMutableList()
        newNotes[index] = emptySet()

        val move = BoardMove(
            index = index,
            previousValue = prevVal,
            newValue = 0,
            previousNotes = prevNotes,
            newNotes = emptySet()
        )

        val updated = current.copy(
            entries = newEntries,
            notes = newNotes,
            undoStack = current.undoStack + move,
            redoStack = emptyList()
        )
        setUpdatedGameState(updated, checkCompletion = false)
    }

    fun undo() {
        val current = _gameState.value ?: return
        if (_isGameOver.value || current.isCompleted || current.isPaused) return
        if (current.undoStack.isEmpty()) return

        val move = current.undoStack.last()
        val newUndo = current.undoStack.dropLast(1)
        val newRedo = current.redoStack + move

        val newEntries = current.entries.clone()
        newEntries[move.index] = move.previousValue

        val newNotes = current.notes.toMutableList()
        newNotes[move.index] = move.previousNotes

        // Restore notes in related cells that were auto-cleared
        move.clearedNotesMap.forEach { (cellIdx, clearedSet) ->
            if (cellIdx in 0..80) {
                newNotes[cellIdx] = newNotes[cellIdx] + clearedSet
            }
        }

        val updated = current.copy(
            entries = newEntries,
            notes = newNotes,
            selectedCellIndex = move.index,
            undoStack = newUndo,
            redoStack = newRedo
        )
        setUpdatedGameState(updated, checkCompletion = false)
    }

    fun redo() {
        val current = _gameState.value ?: return
        if (_isGameOver.value || current.isCompleted || current.isPaused) return
        if (current.redoStack.isEmpty()) return

        val move = current.redoStack.last()
        val newRedo = current.redoStack.dropLast(1)
        val newUndo = current.undoStack + move

        val newEntries = current.entries.clone()
        newEntries[move.index] = move.newValue

        val newNotes = current.notes.toMutableList()
        newNotes[move.index] = move.newNotes

        // Re-clear notes in related cells
        move.clearedNotesMap.forEach { (cellIdx, clearedSet) ->
            if (cellIdx in 0..80) {
                newNotes[cellIdx] = newNotes[cellIdx] - clearedSet
            }
        }

        val updated = current.copy(
            entries = newEntries,
            notes = newNotes,
            selectedCellIndex = move.index,
            undoStack = newUndo,
            redoStack = newRedo
        )
        setUpdatedGameState(updated, checkCompletion = true)
    }

    fun useHint() {
        val current = _gameState.value ?: return
        if (_isGameOver.value || current.isCompleted || current.isPaused) return
        var index = current.selectedCellIndex

        // If no cell selected or cell is already correct given, pick first empty/incorrect cell
        if (index !in 0..80 || current.isGiven(index) || current.entries[index] == current.puzzle.solution[index]) {
            index = (0 until 81).firstOrNull { i ->
                !current.isGiven(i) && current.entries[i] != current.puzzle.solution[i]
            } ?: -1
        }
        if (index == -1) return

        val correctVal = current.puzzle.solution[index]
        val prevVal = current.entries[index]
        val prevNotes = current.notes[index]

        val newEntries = current.entries.clone()
        newEntries[index] = correctVal

        val newNotes = current.notes.toMutableList()
        newNotes[index] = emptySet()

        if (_settings.value.autoClearNotes) {
            clearNotesInRelatedCells(newNotes, index, correctVal)
        }

        val move = BoardMove(
            index = index,
            previousValue = prevVal,
            newValue = correctVal,
            previousNotes = prevNotes,
            newNotes = emptySet()
        )

        val updated = current.copy(
            entries = newEntries,
            notes = newNotes,
            hintsUsed = current.hintsUsed + 1,
            selectedCellIndex = index,
            undoStack = current.undoStack + move,
            redoStack = emptyList()
        )

        setUpdatedGameState(updated, checkCompletion = true)
    }

    fun dismissGameOver() {
        _isGameOver.value = false
    }

    fun buyTimeBombExtraTime() {
        if (_coins.value >= 50) {
            _coins.value -= 50
            prefsManager.addCoins(-50)
            _isGameOver.value = false
            _gameState.update { current ->
                current?.copy(elapsedSeconds = (current?.elapsedSeconds ?: 0L) + 180L)
            }
            startTimer()
        }
    }

    fun autoFillNotes() {
        val current = _gameState.value ?: return
        if (_isGameOver.value || current.isCompleted || current.isPaused) return

        val newNotes = current.notes.toMutableList()

        for (i in 0 until 81) {
            if (current.entries[i] == 0) {
                val candidates = HashSet<Int>()
                val row = i / 9
                val col = i % 9
                val boxR = (row / 3) * 3
                val boxC = (col / 3) * 3

                val usedDigits = HashSet<Int>()
                // Row
                for (c in 0 until 9) {
                    val v = current.entries[row * 9 + c]
                    if (v != 0) usedDigits.add(v)
                }
                // Col
                for (r in 0 until 9) {
                    val v = current.entries[r * 9 + col]
                    if (v != 0) usedDigits.add(v)
                }
                // Box
                for (r in 0 until 3) {
                    for (c in 0 until 3) {
                        val v = current.entries[(boxR + r) * 9 + (boxC + c)]
                        if (v != 0) usedDigits.add(v)
                    }
                }

                for (d in 1..9) {
                    if (!usedDigits.contains(d)) {
                        candidates.add(d)
                    }
                }
                newNotes[i] = candidates
            } else {
                newNotes[i] = emptySet()
            }
        }

        val updated = current.copy(notes = newNotes)
        setUpdatedGameState(updated, checkCompletion = false)
    }

    fun clearAllNotes() {
        val current = _gameState.value ?: return
        if (_isGameOver.value || current.isCompleted || current.isPaused) return

        val newNotes = List(81) { emptySet<Int>() }
        val updated = current.copy(notes = newNotes)
        setUpdatedGameState(updated, checkCompletion = false)
    }

    fun pauseGame() {
        _gameState.update { it?.copy(isPaused = true) }
        stopTimer()
    }

    fun resumeGame() {
        _gameState.update { it?.copy(isPaused = false) }
        startTimer()
    }

    fun restartGame() {
        val current = _gameState.value ?: return
        val newEntries = current.puzzle.givens.clone()
        val newNotes = List(81) { emptySet<Int>() }

        val initialSeconds = if (current.puzzle.mode == GameMode.TIME_BOMB) {
            if (current.puzzle.challengeDateKey != null) 720L
            else when (current.puzzle.difficulty) {
                Difficulty.EASY -> 600L
                Difficulty.MEDIUM -> 720L
                Difficulty.HARD -> 900L
                Difficulty.EXPERT -> 1080L
            }
        } else 0L

        _isGameOver.value = false

        val resetState = current.copy(
            entries = newEntries,
            notes = newNotes,
            elapsedSeconds = initialSeconds,
            mistakes = 0,
            hintsUsed = 0,
            isCompleted = false,
            isPaused = false,
            selectedCellIndex = -1,
            undoStack = emptyList(),
            redoStack = emptyList()
        )
        _gameState.value = resetState
        prefsManager.saveActiveGame(resetState)
        startTimer()
    }

    fun abandonGame() {
        stopTimer()
        val mode = _gameState.value?.puzzle?.mode
        if (mode != null) {
            _activeGames.update { it - mode }
            prefsManager.clearActiveGame(mode)
        } else {
            prefsManager.clearActiveGame()
        }
        _gameState.value = null
    }

    fun updateSettings(newSettings: GameSettings) {
        val wasAutoFillOn = _settings.value.autoFillNotesEnabled
        _settings.value = newSettings
        prefsManager.saveSettings(newSettings)
        if (!wasAutoFillOn && newSettings.autoFillNotesEnabled) {
            autoFillNotes()
        }
    }

    fun resetAllData() {
        stopTimer()
        _gameState.value = null
        _activeGames.value = emptyMap()
        prefsManager.resetAllData()
        _settings.value = GameSettings()
        _statistics.value = OverallStatistics()
        _unlockedAchievements.value = emptyMap()
    }

    // --- Helper calculations ---

    fun getRemainingCounts(): IntArray {
        val current = _gameState.value ?: return IntArray(10) { 9 }
        val counts = IntArray(10) { 0 }
        for (i in 0 until 81) {
            val v = current.entries[i]
            if (v in 1..9 && v == current.puzzle.solution[i]) {
                counts[v]++
            }
        }
        val remaining = IntArray(10) { 0 }
        for (d in 1..9) {
            remaining[d] = maxOf(0, 9 - counts[d])
        }
        return remaining
    }

    fun getTodayUtcDateKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }

    fun isTodayDailyCompleted(): Boolean {
        val today = getTodayUtcDateKey()
        return prefsManager.getCompletedDailyDates().contains(today)
    }

    fun getCompletedDailyDates(): Set<String> {
        return prefsManager.getCompletedDailyDates()
    }

    // --- Private logic ---

    private fun setUpdatedGameState(updated: GameState, checkCompletion: Boolean = false) {
        _gameState.value = updated
        _conflictingIndices.value = validator.findAllConflicts(updated.entries, updated.puzzle.cages)
        _activeGames.update { it + (updated.puzzle.mode to updated) }
        if (checkCompletion) {
            checkBoardCompletion(updated)
        }
        prefsManager.saveActiveGame(_gameState.value)
    }

    private fun checkBoardCompletion(state: GameState) {
        val eval = validator.evaluateBoard(state.entries, state.puzzle.cages)
        if (eval !is ValidationResult.CompleteValid) return
        if (!state.entries.contentEquals(state.puzzle.solution)) return

        // Victory! All 81 cells match the solution and form a complete valid Sudoku
        stopTimer()
        val completedState = state.copy(isCompleted = true, selectedCellIndex = -1)
        _gameState.value = completedState
        _conflictingIndices.value = emptySet()
        _activeGames.update { it - state.puzzle.mode }
        prefsManager.clearActiveGame(state.puzzle.mode)

        // Record statistics with actual time taken
        val actualTimeTaken = if (state.puzzle.mode == GameMode.TIME_BOMB) {
            val initial = if (state.puzzle.challengeDateKey != null) 720L
                          else when (state.puzzle.difficulty) {
                              Difficulty.EASY -> 600L
                              Difficulty.MEDIUM -> 720L
                              Difficulty.HARD -> 900L
                              Difficulty.EXPERT -> 1080L
                          }
            (initial - state.elapsedSeconds).coerceAtLeast(1L)
        } else {
            state.elapsedSeconds
        }

        prefsManager.recordGameWon(
            difficulty = state.puzzle.difficulty,
            elapsedSeconds = actualTimeTaken,
            challengeDateKey = state.puzzle.challengeDateKey,
            mode = state.puzzle.mode
        )
        _statistics.value = prefsManager.loadStatistics()

        // Award coins
        val baseCoin = when (state.puzzle.difficulty) {
            Difficulty.EASY -> 50
            Difficulty.MEDIUM -> 100
            Difficulty.HARD -> 150
            Difficulty.EXPERT -> 250
        }
        val dailyBonus = if (state.puzzle.challengeDateKey != null) 100 else 0
        val earned = baseCoin + dailyBonus
        prefsManager.addCoins(earned)
        _coins.value = prefsManager.getCoins()

        // Check & unlock achievements
        prefsManager.unlockAchievement("first_step")
        when (state.puzzle.difficulty) {
            Difficulty.EASY -> prefsManager.unlockAchievement("easy_master")
            Difficulty.MEDIUM -> prefsManager.unlockAchievement("medium_master")
            Difficulty.HARD -> prefsManager.unlockAchievement("hard_master")
            Difficulty.EXPERT -> prefsManager.unlockAchievement("expert_master")
        }
        if (state.puzzle.challengeDateKey != null) {
            prefsManager.unlockAchievement("daily_devotee")
        }
        if (state.mistakes == 0) {
            prefsManager.unlockAchievement("flawless")
        }
        if (state.hintsUsed == 0) {
            prefsManager.unlockAchievement("no_hints")
        }
        val currentStreak = _statistics.value.currentStreak
        if (currentStreak >= 3) {
            prefsManager.unlockAchievement("streak_3")
        }
        if (currentStreak >= 7) {
            prefsManager.unlockAchievement("streak_7")
        }
        _unlockedAchievements.value = prefsManager.getUnlockedAchievements()
    }

    private fun clearNotesInRelatedCells(notesList: MutableList<Set<Int>>, index: Int, digit: Int): Map<Int, Set<Int>> {
        val clearedMap = HashMap<Int, Set<Int>>()
        val row = index / 9
        val col = index % 9

        fun removeNote(i: Int) {
            if (i != index && notesList[i].contains(digit)) {
                notesList[i] = notesList[i] - digit
                clearedMap[i] = (clearedMap[i] ?: emptySet()) + digit
            }
        }

        // Row
        val rStart = row * 9
        for (c in 0 until 9) removeNote(rStart + c)

        // Col
        for (r in 0 until 9) removeNote(r * 9 + col)

        // 3x3 Box
        val boxR = (row / 3) * 3
        val boxC = (col / 3) * 3
        for (r in 0 until 3) {
            for (c in 0 until 3) {
                removeNote((boxR + r) * 9 + (boxC + c))
            }
        }

        return clearedMap
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                val current = _gameState.value
                if (current != null && !current.isCompleted && !current.isPaused) {
                    val isTimeBomb = current.puzzle.mode == GameMode.TIME_BOMB
                    val nextTime = if (isTimeBomb) {
                        (current.elapsedSeconds - 1).coerceAtLeast(0L)
                    } else {
                        current.elapsedSeconds + 1
                    }

                    _gameState.update { it?.copy(elapsedSeconds = nextTime) }

                    if (isTimeBomb && nextTime == 0L) {
                        _isGameOver.value = true
                        stopTimer()
                    }

                    if (nextTime % 5 == 0L) {
                        prefsManager.saveActiveGame(_gameState.value)
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun getUserScore(): Int {
        val stats = _statistics.value
        val completedDailies = getCompletedDailyDates().size
        return (stats.totalWon * 150) +
                (stats.currentStreak * 100) +
                (_coins.value) +
                (stats.expertStats.gamesWon * 350) +
                (stats.hardStats.gamesWon * 200) +
                (stats.mediumStats.gamesWon * 100) +
                (completedDailies * 120)
    }

    fun getLeaderboardEntries(difficulty: Difficulty? = null, filter: String = "All Time"): List<LeaderboardEntry> {
        val userScore = getUserScore()
        val stats = _statistics.value

        // Seeded base competitors with distinct archetypes
        val competitors = listOf(
            Triple("Sophia Kowalski", 4250, 28),
            Triple("Alexandre Chen", 3890, 24),
            Triple("Elena Rostova", 3420, 21),
            Triple("Kenji Takahashi", 3050, 19),
            Triple("Marcus Vance", 2780, 17),
            Triple("Liam O'Connor", 2410, 15),
            Triple("Chloe Martin", 2090, 13),
            Triple("Arjun Patel", 1750, 11),
            Triple("Emma Watson", 1430, 9),
            Triple("Noah Miller", 1120, 7),
            Triple("Lucas Silva", 850, 5),
            Triple("Maya Lin", 620, 4),
            Triple("Oliver Brown", 410, 3),
            Triple("Isabella Rossi", 260, 2),
            Triple("Daniel Kim", 120, 1)
        )

        val avatarPool = listOf("🦁", "🦊", "🐼", "🦅", "🐺", "🐯", "🦉", "🦄", "🐉", "🐬", "🦋", "🚀")
        val colorPool = listOf(
            0xFF3B82F6, 0xFF10B981, 0xFF8B5CF6, 0xFFF59E0B, 0xFFEC4899,
            0xFF06B6D4, 0xFFEF4444, 0xFF6366F1, 0xFF14B8A6, 0xFFF97316
        )

        val list = mutableListOf<LeaderboardEntry>()

        // User entry
        val userAvatarEmoji = avatarPool.getOrElse(_playerAvatarIndex.value) { "🌟" }
        val userBestTime = when (difficulty) {
            Difficulty.EASY -> stats.easyStats.bestTimeSeconds
            Difficulty.MEDIUM -> stats.mediumStats.bestTimeSeconds
            Difficulty.HARD -> stats.hardStats.bestTimeSeconds
            Difficulty.EXPERT -> stats.expertStats.bestTimeSeconds
            null -> listOf(stats.easyStats.bestTimeSeconds, stats.mediumStats.bestTimeSeconds, stats.hardStats.bestTimeSeconds, stats.expertStats.bestTimeSeconds)
                .filter { it > 0 }
                .minOrNull() ?: 0L
        }

        list.add(
            LeaderboardEntry(
                rank = 0, // calculated later
                name = _playerName.value,
                score = userScore,
                bestTimeSeconds = userBestTime,
                winCount = stats.totalWon,
                avatarEmoji = userAvatarEmoji,
                avatarBgColorHex = 0xFF2563EB,
                isUser = true,
                badge = if (userScore > 2500) "Grandmaster" else if (userScore > 1000) "Master" else if (userScore > 300) "Solver" else "Novice"
            )
        )

        competitors.forEachIndexed { i, comp ->
            val compScore = when (filter) {
                "This Week" -> (comp.second * 0.45).toInt()
                "Daily Cup" -> (comp.second * 0.3).toInt()
                else -> comp.second
            }
            list.add(
                LeaderboardEntry(
                    rank = 0,
                    name = comp.first,
                    score = compScore,
                    bestTimeSeconds = (180 + i * 45).toLong(),
                    winCount = comp.third,
                    avatarEmoji = avatarPool[i % avatarPool.size],
                    avatarBgColorHex = colorPool[i % colorPool.size],
                    isUser = false,
                    badge = if (compScore > 3000) "Grandmaster" else if (compScore > 1500) "Master" else "Pro"
                )
            )
        }

        // Sort descending by score
        list.sortByDescending { it.score }

        // Reassign ranks 1..N
        return list.mapIndexed { index, item -> item.copy(rank = index + 1) }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        _gameState.value?.let { prefsManager.saveActiveGame(it) }
    }
}
