# Initial Prompt for Google AI Studio — Native Android Sudoku

Build **Sudoku: Daily Logic**, a polished native Android Sudoku game. Use the **Android** platform option, Kotlin, Jetpack Compose, Material 3, a single activity and single module compatible with Google AI Studio's current native Android constraints. Create clean, maintainable code and keep puzzle rules separated from UI. This is a real offline-first game, not a static mockup.

## Product design
- Premium-modern, calm, refined, uncluttered visual design. Avoid generic AI-template gradients and overcrowded screens.
- Support Light, Dark and System themes, with a theme system designed to support additional curated color palettes.
- Make the 9×9 Sudoku grid the focus of the play screen. Strong 3×3 box boundaries, clear distinction between clues and user entries, accessible selected-cell / matching-digit / conflict states, large digit controls and comfortable touch targets.
- Responsive phone-first layout with deliberate tablet support, correct system-bar insets, and no overlapping controls. Support portrait and handle landscape gracefully.
- Use subtle non-blocking animations only. Do not sacrifice responsiveness or legibility for decoration.

## Initial deliverable
First create a buildable project with:
1. Home screen with Continue (only if a save exists), New Game, difficulty selection (Easy, Medium, Hard, Expert), Daily Challenge, Statistics, Achievements and Settings.
2. Game screen with 9×9 board, digit pad 1–9, selected cell, notes mode, erase, undo/redo, hint, timer, pause and completion flow.
3. Light / dark / system theme system.
4. Proper ViewModel/data-model separation. Do not put generator or solver algorithms inside composable UI functions.
5. An offline-first domain structure that can later be extended with a repository and persistence layer.

## Critical Sudoku correctness requirements
- Implement a real Sudoku validator and solver, not hard-coded boards that only look valid.
- Given clues are immutable.
- A puzzle may not be presented until the solver confirms that it has **exactly one valid solution**. Use a solution counter that stops after finding the second solution.
- Generate complete valid boards using a correct constraint-based algorithm, then remove clues only while uniqueness remains true.
- Keep generation bounded and run it away from the main UI thread. If the chosen difficulty cannot be generated after bounded attempts, fall back to a known-valid unique puzzle or retry safely; never freeze the app.
- Provide Easy, Medium, Hard and Expert. Prefer classifying difficulty by solving effort / logical techniques, not solely by clue count.
- The hint must come from the validated solution for the current puzzle.
- Victory only occurs when all cells are filled and the board is valid. Notes do not count as values.
- Include a strategy for unit tests or self-checks covering valid, invalid, unsolvable and multiple-solution boards.

## Offline, daily challenge and local progress
- Core gameplay must work in airplane mode. Do not call Gemini or any paid network API to generate puzzles.
- Save/resume an unfinished game reliably, including entries, notes, elapsed time, mistakes, hints and difficulty.
- Daily challenge must use a documented stable date key (UTC is preferred), seeded generation and a generator-version identifier. Cache today's puzzle locally.
- Add local statistics (games played/completed, win rate, total time, best time by difficulty), streaks and local achievements. Make updates idempotent so process recreation cannot duplicate a completion.
- Persist theme and settings.
- Add custom curated themes only after the base theme is correct and accessible.

## Important scope boundary
Google AI Studio native Android builds are currently client-side only and do not directly support a server runtime / Firebase integration. Therefore, do not fake cloud sync or a global leaderboard. In the first build, show local personal records and daily challenge completion. Create clean repository interfaces and document the intended future backend boundary so a real global leaderboard can be added after exporting the project to Android Studio or another supported backend workflow.

## Monetization boundary
Do not integrate AppLovin MAX or ads in this first pass. Keep a small AdsController interface or an explicit TODO boundary only if it does not add unnecessary dependencies. We'll add MAX after the game engine, interactions, persistence and offline QA pass. No ad may ever interrupt active puzzle solving.

## Quality rules
- Inspect the project and use appropriate current dependencies only.
- Do not silently remove existing working functionality while iterating.
- Prefer small, reviewable files with clear responsibilities.
- Avoid unnecessary permissions, login, network access or API keys.
- Handle empty states, invalid actions and app lifecycle events gracefully.
- Use descriptive accessibility labels for cells and controls.
- Before finishing, list files created/changed, describe implemented behavior, identify any incomplete requirements honestly, and fix build errors introduced by your changes.

Start with the minimal working vertical slice and a clean foundation. Do not try to implement every advanced feature in an unreviewable monolithic file. Ensure the current project compiles and the basic Home → New Game → playable board flow works before declaring this first stage complete.
