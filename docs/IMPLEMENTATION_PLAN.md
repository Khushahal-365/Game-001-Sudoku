# Implementation Plan — Google AI Studio Workflow

## Delivery strategy

Build one stable vertical slice at a time. Do not add MAX, cloud sync, or global leaderboards before the core puzzle engine and persistence work correctly. Keep an exported ZIP backup after every accepted milestone.

## Phase 0 — Project setup and first prompt

**Tool:** Google AI Studio → Build mode → Android.  
**Prompt:** `prompts/01_AI_STUDIO_MASTER_PROMPT.md`

Tasks:
- Create a native Kotlin + Jetpack Compose Android project.
- Add an elegant home screen and playable Sudoku board shell.
- Establish navigation, theme system, ViewModel boundaries, and package structure.
- Confirm code compiles and runs in the cloud emulator.
- Don't integrate ads or online features yet.

Exit gate:
- App starts with no build errors.
- Home → new game → game screen navigation works.
- UI scales to phone and tablet previews.
- Code is readable and game logic is not embedded in composable functions.

## Phase 1 — Engine correctness

Tasks:
- Implement validator, solver, solution counter capped at two, generator, and difficulty rater.
- Add seeded random generator for reproducibility.
- Add known board fixtures and unit tests where the AI Studio environment permits; if not, export to Android Studio to run the tests.
- Ensure generation happens off the UI thread and has bounded retries.

Exit gate:
- Generated board passes the validator.
- Exactly one solution is confirmed before puzzle is shown.
- Givens match the stored solution.
- Generation never holds the UI indefinitely.

## Phase 2 — Play interaction and game lifecycle

Tasks:
- Cell select, digit pad, erase, notes, undo/redo, hints, conflict highlighting, pause and timer.
- Win validation and completion result screen.
- Restart/abandon confirmation and Resume game flow.
- Add accessibility descriptions and minimum comfortable tap targets.

Exit gate:
- Can complete an entire valid puzzle.
- Fixed clues cannot be changed.
- Notes don't count toward completion.
- Invalid/partial board never triggers victory.

## Phase 3 — Local persistence and offline readiness

Tasks:
- Persistent active game save and resume.
- Settings and theme persistence.
- Local game history and aggregate statistics.
- Airplane-mode verification; no required network calls for core gameplay.
- Corrupt-save recovery handling.

Exit gate:
- Force-stop/relaunch restores the in-progress board.
- Theme and settings survive restart.
- Full core flow works in airplane mode.

## Phase 4 — Daily challenge, streaks, achievements, custom themes

Tasks:
- Stable daily key and seeded puzzle generation.
- Local once-per-day completion and streak logic.
- Idempotent statistics/achievement updates.
- Light/dark/system modes and curated accent palettes.

Exit gate:
- Same date + generator version yields the same challenge identity.
- A challenge cannot add multiple streak days if opened repeatedly.
- Theme combinations remain readable, including selection/conflict/notes states.

## Phase 5 — Quality and Android configuration

Tasks:
- Export ZIP and open in Android Studio.
- Run unit tests, lint, Gradle build, and available UI tests.
- Verify application ID, version code/name, app icon, minSdk, targetSdk, permissions and manifest.
- As of 19 Oct 2026, verify target API 36+ for Play submission based on the currently published requirement.
- Test performance on a low/mid-range physical device.

Exit gate:
- Clean release build.
- No unnecessary permissions or runtime API keys.
- No data loss or crashes in test cases.

## Phase 6 — AppLovin MAX monetization

Tasks:
- Create app and ad units in MAX dashboard.
- Integrate current stable AppLovin MAX Android SDK and only the networks that have been approved and configured.
- Add the SDK key through the documented Android metadata/config flow; use a placeholder until a real app key is assigned.
- Implement region-appropriate consent and privacy flow before requesting ads as required by applicable law, Play policy and SDK/network guidance.
- Test rewarded load/fail/close/complete callbacks and interstitial placement caps with test ads.
- Do not test by clicking live ad creatives.

Exit gate:
- Game runs if ads fail or are unavailable.
- Rewarded hint only grants after successful earned-reward callback and never twice.
- No ads at launch or during active solving.
- Ad placements are dismissible and clearly distinct from game controls.

## Phase 7 — Play internal test and launch preparation

Tasks:
- Prepare store icon, screenshots, short/full description, privacy policy, content rating, target audience, data-safety declarations and ads declaration based on actual behavior.
- Upload internal testing build; test on multiple real devices and Android versions.
- Validate crash-free cold start, game save recovery, offline behavior and ad compliance.
- Resolve all blockers before production rollout.

Exit gate:
- Release candidate passes `docs/QA_CHECKLIST.md`.

## Phase 8 — Global leaderboard / backend extension

Tasks after exporting the project:
- Select the backend approach and identity model.
- Design canonical daily puzzle/version and secure score submission API.
- Implement server validation, rate limiting, idempotency, privacy and abuse reporting.
- Add sync queue and clear offline/pending/submitted states.
- Do not display a global ranking until the backend is live and validates records.

## Prompting discipline

- One major feature group per request.
- Ask the agent to inspect the existing project before editing.
- Ask for the list of files changed and the reason for each meaningful change.
- In every iteration, require it to preserve working functionality and fix compile errors introduced by its changes.
- Test after each stage; avoid adding UI polish and complex feature logic in the same iteration.
- If repeated fixes make the project unstable, revert to the last ZIP backup and reapply a smaller change.
