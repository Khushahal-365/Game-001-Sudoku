# Post-Export Prompt — Android Studio Hardening and Online Leaderboard

You are working on the exported native Android project for **Sudoku: Daily Logic**. First inspect the complete project and the companion PRD / architecture / QA documents. Preserve all working offline Sudoku behavior. Do not rewrite the game from scratch.

## Part A — harden the existing Android app
- Open the exported Gradle project and make it build reproducibly.
- Run existing tests, add missing engine tests and fix failures rather than suppressing them.
- Confirm Kotlin/Compose architecture, minimum SDK, target SDK, manifest permissions, app ID/version, release configuration and dependency compatibility.
- Pin compatible stable dependency versions; avoid floating `+` dependencies in release artifacts.
- Test rotation, process death, offline play, persistence, font scaling, navigation and low/mid-range device performance.
- Recheck Google's current Play target API requirement before configuring the release build.
- Do not claim app-store readiness until a release build and physical-device QA pass.

## Part B — implement real online leaderboard only after choosing a backend

Design a small trusted backend for daily challenge rankings. Before coding, present the proposed data model, authentication strategy, API endpoints, privacy decisions, costs/free-tier assumptions and abuse controls. Prefer the simplest maintainable option that fits a small budget; do not add unnecessary infrastructure.

Required capabilities:
- Canonical server-issued daily challenge ID and generator/rules version.
- Score submission including challenge ID, elapsed duration, completion signal and idempotency key.
- Server-side validation of challenge membership, legal duration bounds, one submission policy / improved-score policy, duplicate prevention and rate limiting.
- A rank query that returns only safe public fields and paginates.
- Clear Pending / Submitted / Rejected UI state for offline submissions.
- Private profile identifiers; never publish email or full legal name.
- Error handling, basic monitoring and documented data retention/deletion.
- Backend tests for invalid values, replayed submissions, duplicate keys, offline retry and concurrent submissions.

Important: never trust client-reported completion or elapsed time alone. If the score-verification scheme cannot establish fairness against a modified client, document that limitation and call the leaderboard casual rather than cheat-proof. Do not label local device scores as global.

## Part C — ads and privacy audit
- Check the current official AppLovin MAX Android SDK/adapters and consent guide; do not invent versions.
- Integrate only if ad configuration and consent/privacy behavior have been reviewed.
- No ads at launch or during active solving; opt-in rewarded ad only; interstitial only at natural completion breaks with frequency caps.
- Validate Play Console declarations against actual included SDKs and network behavior.

Finish with a report containing architecture changes, files changed, tests run and results, unresolved risks, deployment steps, and any claims that still require manual owner verification.
