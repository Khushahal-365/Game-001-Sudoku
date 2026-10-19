# Game #001 — Sudoku Premium Edition

**Status:** Product specification finalized; implementation planning ready.  
**Build path:** Google AI Studio → native Android (Kotlin + Jetpack Compose) → export ZIP → Android Studio for deeper verification and online-backend integration.

This pack is the source of truth for the first build. Use the prompt files in order. Do not ask the AI Studio agent to implement every feature in one giant pass; first establish a compilable vertical slice, then add the puzzle engine, persistence, extras, and ads.

## Documents

- `docs/PRD.md` — finalized product requirements and acceptance criteria.
- `docs/ARCHITECTURE.md` — app structure, domain rules, offline storage, leaderboard boundary.
- `docs/IMPLEMENTATION_PLAN.md` — staged delivery plan and exit gates.
- `docs/DECISIONS.md` — architecture decisions and deferred scope.
- `docs/MONETIZATION_PRIVACY_RELEASE.md` — AppLovin MAX plan, consent, ad placement rules, and Play release checks.
- `docs/QA_CHECKLIST.md` — engine, UI, persistence, ads, and release test list.
- `prompts/01_AI_STUDIO_MASTER_PROMPT.md` — start a new native Android build.
- `prompts/02_ITERATION_PROMPTS.md` — smaller prompts to use one at a time after the first build.
- `prompts/03_POST_EXPORT_ANDROID_STUDIO_PROMPT.md` — task prompt for exported project work, including online leaderboard readiness.

## Recommended workflow

1. Open Google AI Studio and choose **Build mode → Android**.
2. Paste `prompts/01_AI_STUDIO_MASTER_PROMPT.md` as the initial prompt.
3. Build and inspect the minimum viable game. Then use `prompts/02_ITERATION_PROMPTS.md` sequentially, not all at once.
4. After each milestone, test a full puzzle on a physical Android phone and keep a ZIP backup.
5. Export the Android project ZIP. Open it in Android Studio for unit tests, Gradle/build configuration checks, SDK integration, and any backend work.
6. Add real AppLovin credentials only in the controlled release workflow. Do not paste private account credentials into prompts or commit them to a public repository.
7. Release to Play internal testing before any production rollout.

## Scope truth

Google AI Studio's native Android builder currently generates a client-side Kotlin/Jetpack Compose project and does not directly provide server-side services such as Firebase integration or multiplayer. Therefore:

- **First native milestone:** real offline Sudoku, local daily challenge, local saves, local statistics/streaks, local achievements, themes.
- **Backend milestone:** real cross-device sync and global/verified leaderboards, after export to a workflow where a backend can be added.
- **Monetization milestone:** integrate AppLovin MAX and approved network adapters after core gameplay passes QA.

A local score list must never be presented as a real global leaderboard.

## Current platform checks (verify again before release)

- Google's AI Studio Android guide: https://ai.google.dev/gemini-api/docs/aistudio-android
- Google AI Studio Build mode: https://ai.google.dev/gemini-api/docs/aistudio-build-mode
- AppLovin MAX Android integration: https://developers.applovin.com/en/max/android/overview/integration/
- Google Play target API requirements: https://developer.android.com/google/play/requirements/target-sdk
- Google Play Ads policy: https://support.google.com/googleplay/android-developer/answer/9857753

As checked on 19 October 2026, new Android apps and updates submitted to Google Play must target Android 16 / API 36 or higher under the requirements effective 31 August 2026. Recheck the live policy at submission time.
