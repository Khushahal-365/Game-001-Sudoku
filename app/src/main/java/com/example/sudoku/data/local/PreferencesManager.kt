package com.example.sudoku.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.sudoku.data.model.*
import org.json.JSONArray
import org.json.JSONObject

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("sudoku_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val TAG = "PreferencesManager"
        private const val KEY_ACTIVE_GAME = "active_game_state"
        private const val KEY_SETTINGS = "user_settings"
        private const val KEY_STATISTICS = "user_statistics"
        private const val KEY_ACHIEVEMENTS = "unlocked_achievements"
        private const val KEY_DAILY_COMPLETED_DATES = "daily_completed_dates"
        private const val KEY_DAILY_COMPLETED_DATES_KILLER = "daily_completed_dates_killer"
        private const val KEY_PLAYER_NAME = "player_name"
        private const val KEY_PLAYER_AVATAR = "player_avatar"
        private const val KEY_PLAYER_COINS = "player_coins"
    }

    fun getPlayerName(): String {
        return prefs.getString(KEY_PLAYER_NAME, "You") ?: "You"
    }

    fun setPlayerName(name: String) {
        prefs.edit().putString(KEY_PLAYER_NAME, name.trim().ifEmpty { "You" }).apply()
    }

    fun getPlayerAvatarIndex(): Int {
        return prefs.getInt(KEY_PLAYER_AVATAR, 0)
    }

    fun setPlayerAvatarIndex(index: Int) {
        prefs.edit().putInt(KEY_PLAYER_AVATAR, index).apply()
    }

    fun getCoins(): Int {
        return prefs.getInt(KEY_PLAYER_COINS, 0)
    }

    fun addCoins(amount: Int) {
        val current = getCoins()
        prefs.edit().putInt(KEY_PLAYER_COINS, current + amount).apply()
    }

    // --- Active Game Persistence (Per Mode) ---

    private fun getActiveGameKey(mode: GameMode): String {
        return "${KEY_ACTIVE_GAME}_${mode.name}"
    }

    fun saveActiveGame(state: GameState?) {
        if (state == null || state.isCompleted) {
            return
        }
        val modeKey = getActiveGameKey(state.puzzle.mode)
        try {
            val json = JSONObject().apply {
                put("puzzleId", state.puzzle.id)
                put("seed", state.puzzle.seed)
                put("difficulty", state.puzzle.difficulty.name)
                state.puzzle.challengeDateKey?.let { put("challengeDateKey", it) }
                put("generatorVersion", state.puzzle.generatorVersion)

                put("mode", state.puzzle.mode.name)
                val cagesArr = JSONArray()
                state.puzzle.cages.forEach { cage ->
                    cagesArr.put(JSONObject().apply {
                        put("id", cage.id)
                        put("sum", cage.targetSum)
                        put("cells", JSONArray(cage.cellIndices))
                    })
                }
                put("cages", cagesArr)

                put("givens", JSONArray(state.puzzle.givens.toList()))
                put("solution", JSONArray(state.puzzle.solution.toList()))
                put("entries", JSONArray(state.entries.toList()))

                val notesArray = JSONArray()
                state.notes.forEach { noteSet ->
                    notesArray.put(JSONArray(noteSet.toList()))
                }
                put("notes", notesArray)

                put("elapsedSeconds", state.elapsedSeconds)
                put("mistakes", state.mistakes)
                put("hintsUsed", state.hintsUsed)
                put("selectedCellIndex", state.selectedCellIndex)
                put("isNotesMode", state.isNotesMode)
            }
            prefs.edit()
                .putString(modeKey, json.toString())
                .putString(KEY_ACTIVE_GAME, json.toString())
                .apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving active game", e)
        }
    }

    fun loadActiveGame(mode: GameMode = GameMode.CLASSIC): GameState? {
        val modeKey = getActiveGameKey(mode)
        val raw = prefs.getString(modeKey, null) ?: prefs.getString(KEY_ACTIVE_GAME, null) ?: return null
        val loaded = parseGameState(raw) ?: return null
        return if (loaded.puzzle.mode == mode) loaded else null
    }

    fun loadAllActiveGames(): Map<GameMode, GameState> {
        val result = HashMap<GameMode, GameState>()
        GameMode.values().forEach { mode ->
            val game = loadActiveGame(mode)
            if (game != null && !game.isCompleted) {
                result[mode] = game
            }
        }
        return result
    }

    private fun parseGameState(raw: String): GameState? {
        return try {
            val json = JSONObject(raw)
            val puzzleId = json.getString("puzzleId")
            val seed = json.optLong("seed", 0L)
            val diff = Difficulty.fromString(json.getString("difficulty"))
            val mode = GameMode.fromString(json.optString("mode", GameMode.CLASSIC.name))
            val dateKey = if (json.has("challengeDateKey")) json.getString("challengeDateKey") else null
            val genVer = json.optInt("generatorVersion", 1)

            val cagesList = ArrayList<KillerCage>()
            val cagesArr = json.optJSONArray("cages")
            if (cagesArr != null) {
                for (ci in 0 until cagesArr.length()) {
                    val cObj = cagesArr.getJSONObject(ci)
                    val id = cObj.getInt("id")
                    val sum = cObj.getInt("sum")
                    val cellsArr = cObj.getJSONArray("cells")
                    val cells = ArrayList<Int>()
                    for (cj in 0 until cellsArr.length()) {
                        cells.add(cellsArr.getInt(cj))
                    }
                    cagesList.add(KillerCage(id, sum, cells))
                }
            }

            val givensArr = json.getJSONArray("givens")
            val givens = IntArray(81) { givensArr.getInt(it) }

            val solutionArr = json.getJSONArray("solution")
            val solution = IntArray(81) { solutionArr.getInt(it) }

            val puzzle = SudokuPuzzle(
                id = puzzleId,
                seed = seed,
                difficulty = diff,
                givens = givens,
                solution = solution,
                challengeDateKey = dateKey,
                mode = mode,
                cages = cagesList,
                generatorVersion = genVer
            )

            val entriesArr = json.getJSONArray("entries")
            val entries = IntArray(81) { entriesArr.getInt(it) }

            val notesArr = json.optJSONArray("notes")
            val notes = ArrayList<Set<Int>>(81)
            for (i in 0 until 81) {
                if (notesArr != null && i < notesArr.length()) {
                    val subArr = notesArr.getJSONArray(i)
                    val set = HashSet<Int>()
                    for (j in 0 until subArr.length()) {
                        set.add(subArr.getInt(j))
                    }
                    notes.add(set)
                } else {
                    notes.add(emptySet())
                }
            }

            val elapsed = json.optLong("elapsedSeconds", 0L)
            val mistakes = json.optInt("mistakes", 0)
            val hints = json.optInt("hintsUsed", 0)
            val selected = json.optInt("selectedCellIndex", -1)
            val isNotes = json.optBoolean("isNotesMode", false)

            GameState(
                puzzle = puzzle,
                entries = entries,
                notes = notes,
                elapsedSeconds = elapsed,
                mistakes = mistakes,
                hintsUsed = hints,
                selectedCellIndex = selected,
                isNotesMode = isNotes,
                isCompleted = false,
                isPaused = false
            )
        } catch (e: Exception) {
            Log.e(TAG, "Corrupt save payload detected", e)
            null
        }
    }

    fun clearActiveGame(mode: GameMode? = null) {
        val editor = prefs.edit()
        if (mode != null) {
            editor.remove(getActiveGameKey(mode))
            val mainRaw = prefs.getString(KEY_ACTIVE_GAME, null)
            if (mainRaw != null) {
                val parsed = parseGameState(mainRaw)
                if (parsed != null && parsed.puzzle.mode == mode) {
                    editor.remove(KEY_ACTIVE_GAME)
                }
            }
        } else {
            GameMode.values().forEach { m ->
                editor.remove(getActiveGameKey(m))
            }
            editor.remove(KEY_ACTIVE_GAME)
        }
        editor.apply()
    }

    // --- Settings Persistence ---

    fun saveSettings(settings: GameSettings) {
        val json = JSONObject().apply {
            put("themeColor", settings.themeColor.name)
            put("isDarkMode", settings.isDarkMode)
            put("highlightSameDigits", settings.highlightSameDigits)
            put("highlightConflicts", settings.highlightConflicts)
            put("vibrateOnInput", settings.vibrateOnInput)
            put("showTimer", settings.showTimer)
            put("autoClearNotes", settings.autoClearNotes)
            put("autoFillNotesEnabled", settings.autoFillNotesEnabled)
        }
        prefs.edit().putString(KEY_SETTINGS, json.toString()).apply()
    }

    fun loadSettings(): GameSettings {
        val raw = prefs.getString(KEY_SETTINGS, null) ?: return GameSettings()
        return try {
            val json = JSONObject(raw)
            GameSettings(
                themeColor = AppThemeColor.fromString(json.optString("themeColor", AppThemeColor.BLUE_OCEAN.name)),
                isDarkMode = json.optBoolean("isDarkMode", false),
                highlightSameDigits = json.optBoolean("highlightSameDigits", true),
                highlightConflicts = json.optBoolean("highlightConflicts", true),
                vibrateOnInput = json.optBoolean("vibrateOnInput", true),
                showTimer = json.optBoolean("showTimer", true),
                autoClearNotes = json.optBoolean("autoClearNotes", true),
                autoFillNotesEnabled = json.optBoolean("autoFillNotesEnabled", false)
            )
        } catch (e: Exception) {
            GameSettings()
        }
    }

    // --- Statistics Persistence ---

    fun loadStatistics(): OverallStatistics {
        val raw = prefs.getString(KEY_STATISTICS, null) ?: return OverallStatistics()
        return try {
            val json = JSONObject(raw)
            val totalStarted = json.optInt("totalStarted", 0)
            val totalWon = json.optInt("totalWon", 0)
            val currentStreak = json.optInt("currentStreak", 0)
            val longestStreak = json.optInt("longestStreak", 0)
            val lastCompletedDateKey = if (json.has("lastCompletedDateKey")) json.getString("lastCompletedDateKey") else null

            fun parseDiff(diffKey: String): DifficultyStats {
                val obj = json.optJSONObject(diffKey) ?: return DifficultyStats()
                return DifficultyStats(
                    gamesStarted = obj.optInt("gamesStarted", 0),
                    gamesWon = obj.optInt("gamesWon", 0),
                    bestTimeSeconds = obj.optLong("bestTimeSeconds", 0L),
                    totalTimeSeconds = obj.optLong("totalTimeSeconds", 0L)
                )
            }

            OverallStatistics(
                totalStarted = totalStarted,
                totalWon = totalWon,
                currentStreak = currentStreak,
                longestStreak = longestStreak,
                lastCompletedDateKey = lastCompletedDateKey,
                easyStats = parseDiff("easy"),
                mediumStats = parseDiff("medium"),
                hardStats = parseDiff("hard"),
                expertStats = parseDiff("expert")
            )
        } catch (e: Exception) {
            OverallStatistics()
        }
    }

    fun recordGameStarted(difficulty: Difficulty) {
        val current = loadStatistics()
        val updated = when (difficulty) {
            Difficulty.EASY -> current.copy(
                totalStarted = current.totalStarted + 1,
                easyStats = current.easyStats.copy(gamesStarted = current.easyStats.gamesStarted + 1)
            )
            Difficulty.MEDIUM -> current.copy(
                totalStarted = current.totalStarted + 1,
                mediumStats = current.mediumStats.copy(gamesStarted = current.mediumStats.gamesStarted + 1)
            )
            Difficulty.HARD -> current.copy(
                totalStarted = current.totalStarted + 1,
                hardStats = current.hardStats.copy(gamesStarted = current.hardStats.gamesStarted + 1)
            )
            Difficulty.EXPERT -> current.copy(
                totalStarted = current.totalStarted + 1,
                expertStats = current.expertStats.copy(gamesStarted = current.expertStats.gamesStarted + 1)
            )
        }
        saveStatistics(updated)
    }

    fun recordGameWon(difficulty: Difficulty, elapsedSeconds: Long, challengeDateKey: String?, mode: GameMode = GameMode.CLASSIC) {
        val current = loadStatistics()
        val oldDiff = current.statsFor(difficulty)
        val newBest = if (oldDiff.bestTimeSeconds == 0L || elapsedSeconds < oldDiff.bestTimeSeconds) {
            elapsedSeconds
        } else {
            oldDiff.bestTimeSeconds
        }
        val newDiff = oldDiff.copy(
            gamesWon = oldDiff.gamesWon + 1,
            bestTimeSeconds = newBest,
            totalTimeSeconds = oldDiff.totalTimeSeconds + elapsedSeconds
        )

        var newStreak = current.currentStreak
        var newLongest = current.longestStreak
        var newLastDate = current.lastCompletedDateKey

        if (challengeDateKey != null) {
            // Check daily completion idempotency for the mode
            val completedDates = getCompletedDailyDates(mode).toMutableSet()
            if (!completedDates.contains(challengeDateKey)) {
                completedDates.add(challengeDateKey)
                recordDailyWon(challengeDateKey, mode)

                newStreak += 1
                if (newStreak > newLongest) {
                    newLongest = newStreak
                }
                newLastDate = challengeDateKey
            }
        }

        val updated = when (difficulty) {
            Difficulty.EASY -> current.copy(
                totalWon = current.totalWon + 1,
                currentStreak = newStreak,
                longestStreak = newLongest,
                lastCompletedDateKey = newLastDate,
                easyStats = newDiff
            )
            Difficulty.MEDIUM -> current.copy(
                totalWon = current.totalWon + 1,
                currentStreak = newStreak,
                longestStreak = newLongest,
                lastCompletedDateKey = newLastDate,
                mediumStats = newDiff
            )
            Difficulty.HARD -> current.copy(
                totalWon = current.totalWon + 1,
                currentStreak = newStreak,
                longestStreak = newLongest,
                lastCompletedDateKey = newLastDate,
                hardStats = newDiff
            )
            Difficulty.EXPERT -> current.copy(
                totalWon = current.totalWon + 1,
                currentStreak = newStreak,
                longestStreak = newLongest,
                lastCompletedDateKey = newLastDate,
                expertStats = newDiff
            )
        }
        saveStatistics(updated)
    }

    private fun saveStatistics(stats: OverallStatistics) {
        val json = JSONObject().apply {
            put("totalStarted", stats.totalStarted)
            put("totalWon", stats.totalWon)
            put("currentStreak", stats.currentStreak)
            put("longestStreak", stats.longestStreak)
            stats.lastCompletedDateKey?.let { put("lastCompletedDateKey", it) }

            fun putDiff(key: String, d: DifficultyStats) {
                put(key, JSONObject().apply {
                    put("gamesStarted", d.gamesStarted)
                    put("gamesWon", d.gamesWon)
                    put("bestTimeSeconds", d.bestTimeSeconds)
                    put("totalTimeSeconds", d.totalTimeSeconds)
                })
            }

            putDiff("easy", stats.easyStats)
            putDiff("medium", stats.mediumStats)
            putDiff("hard", stats.hardStats)
            putDiff("expert", stats.expertStats)
        }
        prefs.edit().putString(KEY_STATISTICS, json.toString()).apply()
    }

    fun getCompletedDailyDates(mode: GameMode = GameMode.CLASSIC): Set<String> {
        val key = if (mode == GameMode.KILLER) KEY_DAILY_COMPLETED_DATES_KILLER else KEY_DAILY_COMPLETED_DATES
        return prefs.getStringSet(key, emptySet()) ?: emptySet()
    }

    fun recordDailyWon(dateKey: String, mode: GameMode = GameMode.CLASSIC) {
        val key = if (mode == GameMode.KILLER) KEY_DAILY_COMPLETED_DATES_KILLER else KEY_DAILY_COMPLETED_DATES
        val current = prefs.getStringSet(key, emptySet()) ?: emptySet()
        val updated = current + dateKey
        prefs.edit().putStringSet(key, updated).apply()
    }

    // --- Achievements Persistence ---

    fun getUnlockedAchievements(): Map<String, Long> {
        val raw = prefs.getString(KEY_ACHIEVEMENTS, null) ?: return emptyMap()
        return try {
            val json = JSONObject(raw)
            val result = HashMap<String, Long>()
            val keys = json.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                result[key] = json.getLong(key)
            }
            result
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun unlockAchievement(id: String): Boolean {
        val current = getUnlockedAchievements().toMutableMap()
        if (current.containsKey(id)) return false // already unlocked
        current[id] = System.currentTimeMillis()
        val json = JSONObject()
        current.forEach { (k, v) -> json.put(k, v) }
        prefs.edit().putString(KEY_ACHIEVEMENTS, json.toString()).apply()
        return true
    }

    fun resetAllData() {
        prefs.edit().clear().apply()
    }
}
