# Product Requirements Document — Sudoku Premium Edition

**Game ID:** GAME-001  
**Working title:** Sudoku: Daily Logic (store title can change after branding review)  
**Platform:** Android phones and tablets  
**Primary build workflow:** Google AI Studio native Android builder  
**Implementation:** Kotlin + Jetpack Compose, single activity / single module initially  
**Product posture:** Premium-modern visual quality; free-to-play, offline-first, ad-supported  
**Document status:** Finalized for implementation planning  
**Date:** 19 October 2026

## 1. Product vision

Create a polished, fast, trustworthy Sudoku game that feels like a carefully designed premium puzzle product. It must work without an account or internet connection for its core use cases. Gameplay correctness comes before ads, online features, and decorative polish.

## 2. Product goals

1. Deliver correct, repeatable Sudoku gameplay with puzzles that have exactly one valid solution.
2. Make the app comfortable to use one-handed on small Android phones and still scale well to tablets.
3. Offer meaningful repeat play through four difficulty levels, daily challenges, progress tracking, streaks, achievements, and custom themes.
4. Preserve active games and preferences across process death and device restart.
5. Monetize carefully without putting ads in the user's way or making ads necessary to finish a puzzle.
6. Establish a clean base that can be extended with a real backend and global leaderboards after export from Google AI Studio.

## 3. Intended audience and positioning

- General Sudoku players, from beginners to experienced solvers.
- Product marketing should not specifically target children unless the product and Play Console audience settings are deliberately redesigned for that purpose.
- Store target-audience declarations, privacy disclosures, and ad configuration must truthfully reflect the final audience and markets.

## 4. MVP scope

### 4.1 Core play

- 9×9 Sudoku grid with clear 3×3 box boundaries.
- Given clues are visually distinct and immutable.
- Select a cell and enter digits 1–9 using a large, reachable number pad.
- Erase, undo, redo, pencil notes, hint, pause/resume, and restart/abandon confirmation.
- Highlight selected cell, its row/column/box, matching digits, and conflicts using distinct accessible styles.
- Optional mistake feedback with a setting to enable/disable it; don't silently change the puzzle or auto-correct user entries.
- Timer with pause behavior; pausing must not accidentally count background time if the chosen design says the timer pauses.
- Completion state checks that all 81 cells are filled and the board is valid against the puzzle solution.
- Clear success screen, completion time, difficulty, and action to play again.
- Never permit edits to fixed clues.

### 4.2 Difficulty

- Easy, Medium, Hard, Expert.
- A generated puzzle must have exactly one solution, regardless of difficulty.
- Difficulty classification must not be based only on clue count. The engine should use a solving-effort score or logical-technique profile; clue-count ranges can be a generation heuristic, not a correctness guarantee.
- If an attempt misses the desired difficulty, retry within a bounded number of attempts and then choose a known-valid fallback. Never freeze indefinitely while trying to generate a puzzle.

### 4.3 Daily Challenge

- One daily puzzle, available offline.
- Same calendar-day challenge key should be deterministic across devices and app restarts, using a documented fixed time boundary (UTC is recommended for challenge identity).
- Keep the day's puzzle and result locally available after first generation.
- Clearly distinguish the local/offline challenge from any server-verified global competition.
- Daily completion, best time, and streak update idempotently; reopening the app must not grant a duplicate completion or streak day.
- Offline daily puzzle identity is suitable for personal play only. Online ranking must use a server-issued or server-verified challenge ID and scoring rules.

### 4.4 Statistics and streaks

- Games started/completed, wins, win rate, total play time, best time per difficulty, current streak, and longest streak.
- Define streak rule in product code and UI. Recommended: count one qualifying daily challenge completion per UTC challenge date; missed dates break current streak; never grant multiple streak increments for the same date.
- Stats remain local in MVP and are not advertised as synced or tamper-proof.

### 4.5 Achievements

- Local achievements for first completion, completing each difficulty, daily challenge milestones, streak milestones, and clean/low-mistake games.
- Achievement unlock checks are idempotent and can be re-run without duplicate rewards.
- Use accessible progress and unlocked states.
- No paid purchase or ad watch should be required for a basic achievement.

### 4.6 Themes and settings

- Light and dark themes at launch.
- Custom theme selection with a small curated set of palettes; keep text, clues, notes, selected cells, conflict indicators, and focus states readable.
- Follow system theme as an optional setting, alongside forced Light and Dark.
- Settings: sound on/off (if sounds ship), vibration/haptics on/off, mistake highlighting, auto-highlight same digit, timer behavior, theme, and accessibility options.
- Store preference state persistently.

### 4.7 Offline and persistence

- All core play, puzzle generation, local daily challenge, themes, stats, achievements, and saving/resuming work in airplane mode.
- Save after meaningful state changes, not only on exit.
- On launch, offer Resume when an unfinished game exists.
- Persist the full game state: puzzle ID/seed, givens, solution or deterministic answer source, user entries, notes, elapsed time, selected difficulty, mistakes, hint count, and completion state.
- Defensively handle corrupt or outdated save payloads: recover gracefully with a message and never crash at startup.

## 5. Online features — planned extension, not a fake MVP implementation

Real global leaderboards require a backend that can validate submissions and prevent trivial client-side score forgery. Google AI Studio's native Android builder is client-side only, so the global leaderboard is a separate post-export milestone.

When added, it should include:
- Server-issued daily challenge definition and stable challenge ID.
- Score submission with server validation of puzzle ID, completion status, elapsed-time bounds, attempt metadata, and duplicate submissions.
- Rate limiting and replay/idempotency protection.
- Privacy-conscious player display name and a non-identifying public player ID; no public email or full legal name.
- Reporting/abuse pathway and data deletion policy where accounts/profile data exist.
- Clear offline status if a score could not be submitted. Never imply an unsubmitted local score is globally ranked.

## 6. Monetization requirements

Integrate AppLovin MAX only after the core game works and passes gameplay QA.

- Rewarded ads are explicitly opt-in and must describe the reward before launch. Example: one additional hint. If the ad fails, is unavailable, or is closed before completion, the user does not lose anything and the puzzle remains playable.
- Interstitials only at natural transitions such as after a completion/result screen; never at launch, during active solving, immediately after a normal cell tap, or before the intended action.
- Start with a conservative cap: no interstitial on first launch, no interstitial during an active puzzle, at most one after every three completed standard puzzles, and no more than three interstitials per day. These are product defaults to tune only after policy review and real UX testing.
- Optional banner placements only on non-gameplay screens if tests show they don't crowd controls; do not place banners where the user may confuse them with the number pad.
- Use official MAX SDK and currently supported adapter versions. Verify current network approval, SDK requirements, consent guidance, and Android compatibility before adding versions.
- Use test ad units / test mode until the release candidate has passed QA. Do not click live ads during tests.
- Ads must never be required to begin, continue, or complete a puzzle.

## 7. Design and interaction requirements

- Calm, premium, uncluttered visual language; no generic gradient-heavy AI-template aesthetic.
- Strong typography hierarchy, careful spacing, restrained accent color, refined selected-cell state, subtle transitions, and purposeful empty states.
- Grid should dominate the play screen and preserve a near-square shape. The number pad and primary tools must remain visible without obscuring the grid on common phone sizes.
- Tap targets should generally be at least 48dp where feasible.
- Support font scaling and avoid clipping at larger system font sizes.
- Color must not be the only way to convey fixed cells, selected cells, wrong entries, notes, or achievements.
- Respect Android system bars, insets, and light/dark system contrast.
- Animation must be short and non-blocking; support reduced animation if practical.
- No unnecessary login, permission prompts, or network request on launch.

## 8. Technical constraints

- Native Android code generated with Kotlin and Jetpack Compose by Google AI Studio.
- Start with single activity and single module, respecting current native AI Studio limitations.
- Keep Sudoku rules and solver logic independent from Compose UI.
- Do not use Gemini or any paid inference API at runtime for puzzle generation; puzzles are deterministic local computation.
- Minimum SDK must be compatible with the selected MAX SDK. Current AppLovin Android MAX documentation states minSdkVersion 24 or above; verify the exact version in use.
- At the time this specification was prepared (19 October 2026), Google Play's published requirement effective 31 August 2026 is target Android 16 / API 36 or higher for new apps and updates. Validate the generated Gradle configuration against the live policy before submission.

## 9. Success criteria / acceptance gates

### Gate A — playable prototype
- App builds and opens on emulator and a physical Android phone.
- A user can start Easy Sudoku, fill cells, erase, use notes, undo, and complete a puzzle.
- Fixed clues cannot be changed.

### Gate B — correctness
- The generator validates every puzzle and confirms exactly one solution before presenting it.
- Unit tests cover known solved boards, invalid rows/columns/boxes, unsolvable boards, multi-solution boards, and generated puzzle uniqueness.
- All four difficulty labels work and generation is bounded.

### Gate C — reliable app
- State restores after force-stop/relaunch and after process death.
- App remains fully playable offline.
- Themes and settings persist.
- No crash, freeze, or layout overflow on supported phone/tablet sizes and font scaling.

### Gate D — product extras
- Daily challenge is deterministic for a documented date key.
- Stats, streaks, and achievements update exactly once per qualifying event.
- Custom themes retain adequate contrast.

### Gate E — monetization and release
- No intrusive ad placements; rewarded ads are opt-in; test ads and all close/reward/failure callbacks are tested.
- Consent, privacy policy, Data safety, target audience, ad declaration, SDK inventory, and content rating are reviewed against actual implementation.
- Internal testing release installs and runs on representative real devices.

### Gate F — online leaderboard (post-export)
- Scores are server-verified and never trusted solely from client submissions.
- Offline score submission/retry behavior and duplicate prevention are tested.

## 10. Out of scope for first native milestone

- Multiplayer / head-to-head play.
- Cloud sync and cross-device restore.
- Real global leaderboard.
- Account login and social graph.
- Paid subscriptions, IAP, or virtual currency.
- Runtime generative AI or a network requirement for puzzles.
- User-generated theme uploads.

These can be considered later without compromising the first release.
