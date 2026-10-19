# Iteration Prompts — Apply One at a Time

Use only after the initial project builds. At the start of every prompt, tell the AI Studio agent to inspect the current code and preserve all working behavior. Do not paste all stages as one request.

## Prompt 1 — Puzzle engine and solver

Inspect the current Sudoku project before editing. Implement a well-tested Sudoku domain engine separated from Compose UI: board validation, constraint-based backtracking solver, solution counter that stops at two, seeded random generator, generator that removes clues only while exactly one solution remains, and a bounded difficulty rating/generation loop for Easy / Medium / Hard / Expert. Never show an invalid or multi-solution puzzle. Run generation off the main thread. Add unit tests for valid solved boards, duplicate rows/columns/boxes, unsolvable boards, multi-solution boards, fixed clues, and generated uniqueness. Preserve the current UI and fix any build/test issues introduced.

## Prompt 2 — Complete gameplay interactions

Inspect the existing implementation. Finish game interactions: selected cell, digit pad, immutable givens, entry/erase, notes mode, conflict highlighting, matching-digit / row / column / box highlighting, undo/redo, validated hint, mistakes, timer, pause/resume, restart/abandon confirmation and win detection. Notes never count as placed digits, and partial/invalid boards must never trigger victory. Keep domain logic outside composables, keep taps responsive, and preserve the current design system. Run checks and report remaining defects honestly.

## Prompt 3 — Durable offline save/resume

Add reliable local persistence for active game state, history, settings and theme. Prefer Room for game/history records and DataStore for preferences if the current project supports them cleanly; otherwise isolate the storage behind repository interfaces and use the simplest reliable local persistence supported by this project. Save meaningful changes safely without blocking the UI. Restore the puzzle, entries, notes, elapsed time, hints, mistakes and difficulty after force-stop/relaunch. Add corrupt-save recovery and verify that no network is needed for core play. Do not add accounts or cloud sync.

## Prompt 4 — Daily challenge

Implement a deterministic offline daily challenge with a documented UTC date key, seeded random sequence and explicit generator version. Cache the daily puzzle, store completion once per date, preserve a personal best, and prevent duplicate stats updates when the screen reopens or the app restarts. Make UI copy clear that personal daily records are local and are not a global rank. Test a date transition using an injectable clock or test date rather than waiting for midnight.

## Prompt 5 — Stats, streaks and achievements

Implement local statistics, current/longest daily streak and achievements. Define streak rules in code and UI. Make result recording and achievement unlocks idempotent. Add achievement categories for first completion, difficulty milestones, daily challenges and streaks. Validate statistics against known test sequences. Do not create leaderboard screens that falsely claim a global ranking; use a placeholder explaining online competition will be enabled only when a verified backend is connected.

## Prompt 6 — Custom themes and accessibility

Audit all screens for UI consistency and improve the premium-modern feel without rewriting working game logic. Add a small curated custom-theme palette alongside Light, Dark and System. Test clue/user-entry differentiation, notes, selection, conflicts, disabled controls and text contrast across every palette. Fix small-phone overflow, tablet stretch, landscape, system insets, font scaling and screen-reader descriptions. Prefer restrained transitions and no blocking animation.

## Prompt 7 — Final game QA audit

Audit against `docs/QA_CHECKLIST.md` from the project pack. Do not add ads yet. Identify all failed criteria, prioritize correctness/data loss/crashes first, fix them in small steps, rerun the relevant tests and provide a pass/fail report. Verify airplane-mode play and persistence after force-stop/relaunch. Do not claim a test passed unless it was actually run or manually verified.

## Prompt 8 — Prepare MAX integration (planning only)

Review the current project structure and prepare a small implementation plan for AppLovin MAX integration using the official current Android documentation. Do not invent SDK versions or adapter versions. List required dashboard items, Gradle dependency and adapter requirements, consent/privacy integration choices, test mode, ad callback state handling and files that will change. Keep all IDs as placeholders and do not implement anything until the plan is reviewed. No ad may appear at launch or during a live puzzle.

## Prompt 9 — Implement MAX in a separate reviewed change

Integrate the current supported AppLovin MAX Android SDK and only approved mediation adapters using compatible pinned versions. Keep IDs as placeholders and document where the owner supplies the MAX SDK key and ad-unit IDs. Add a resilient AdsController; app remains usable if ads fail or no fill occurs. Rewarded ads must be opt-in and award one hint only on earned-reward callback, exactly once. Interstitials may appear only at natural result-screen breaks and must obey configured frequency caps. No startup or active-game ads. Use test mode/test units and test failure, early close, duplicate callback, background/resume and consent-denied paths. Do not click live ads. Run Gradle/build checks and report anything not verified.

## Prompt 10 — Export handoff

Prepare the project for ZIP export and continued development in Android Studio. Check minSdk compatibility with the selected MAX SDK, targetSdk against the current Google Play requirement, app ID placeholders, Gradle configuration, manifest permissions, and any dependencies. Do not assert production-readiness without a successful release build and physical-device testing. Give a concise handoff note including build steps, tests run, outstanding issues and the location of the global-leaderboard backend boundary.
