# Architecture and Product Decisions

## ADR-001 — Native Android with Kotlin and Jetpack Compose

**Decision:** Use Google AI Studio's native Android build option.  
**Reason:** The target deliverable is a native Android app and the current builder generates Kotlin + Jetpack Compose.  
**Trade-off:** Native AI Studio's Android workflow is currently client-side only and constrained to a single activity/module; backend work must happen after export or in a separate supported workflow.

## ADR-002 — Offline-first gameplay

**Decision:** The app's core puzzle experience must not depend on a network connection or a runtime Gemini API call.  
**Reason:** Predictable operating cost, dependable play, and resilience on low-connectivity devices.  
**Consequence:** Puzzle generation/validation is a deterministic local algorithm; backend features remain optional extensions.

## ADR-003 — Unique-solution puzzles only

**Decision:** Do not show a generated puzzle until a solution counter confirms exactly one solution.  
**Reason:** Puzzle correctness is a core trust requirement.  
**Consequence:** Use backtracking solver with early stop after two solutions, bounded generation attempts, and a validated fallback strategy.

## ADR-004 — Daily challenge is deterministic locally but not globally trusted

**Decision:** In MVP, use a stable date key and seeded generator for offline daily play.  
**Reason:** Daily challenges should work without account or network.  
**Trade-off:** A deterministic client generator does not prevent modified clients or score forgery; it cannot power a trustworthy world leaderboard by itself.

## ADR-005 — Global leaderboards require backend verification

**Decision:** Post-export milestone.  
**Reason:** Server-side validation, idempotency and rate limiting are needed for a credible global leaderboard. AI Studio's native Android builder does not provide a server runtime.  
**Consequence:** MVP shows daily challenge completion and personal bests, but must not label local records as global ranking.

## ADR-006 — Ads are integrated after core QA

**Decision:** Build and test a complete playable, saveable game before introducing MAX or mediated network SDKs.  
**Reason:** SDK integration increases Gradle, privacy and troubleshooting surface area and should not obscure game-engine bugs.  
**Consequence:** Ads remain behind a configuration flag until Phase 6.

## ADR-007 — Conservative ad experience

**Decision:** No ads at launch or during a live puzzle. Rewarded ads are opt-in; interstitials appear only after natural completion breaks and are capped.  
**Reason:** Player trust and Google Play disruptive-ad policy.

## ADR-008 — Working title and package name remain provisional

**Decision:** Use “Sudoku: Daily Logic” as a working title; do not finalize store branding or application ID until owner approves them.  
**Reason:** No brand name or package identifier has yet been approved for this game.
