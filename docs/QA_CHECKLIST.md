# QA Checklist — Sudoku Premium Edition

Mark each item pass/fail and record device, app version, and notes for any failure. A feature is not done merely because its UI appears.

## A. Build and startup
- [ ] Project builds without errors.
- [ ] App launches repeatedly without crash or startup hang.
- [ ] Fresh install lands on a useful Home screen.
- [ ] Back button behavior is consistent; no accidental app exit from an unfinished puzzle without save/confirmation.
- [ ] No unexpected permissions are requested.

## B. Sudoku engine correctness
- [ ] Known valid solved board passes validation.
- [ ] Duplicate digit in a row is rejected.
- [ ] Duplicate digit in a column is rejected.
- [ ] Duplicate digit in a 3×3 box is rejected.
- [ ] Invalid symbols / cell counts are rejected safely.
- [ ] Unsolvable board is detected.
- [ ] Puzzle with multiple solutions is detected.
- [ ] Solution counter stops at two rather than exploring all solutions unnecessarily.
- [ ] Each generated puzzle has exactly one solution.
- [ ] Each given matches its solution cell.
- [ ] Generator uses bounded retries and has a safe fallback.
- [ ] Generator does not block the UI thread.
- [ ] Seeded generation is reproducible for the same generator version and seed.
- [ ] Difficulty labels correspond to validated difficulty ratings, not labels only.

## C. Game interactions
- [ ] Given cells cannot be erased or changed.
- [ ] Digit entry works for 1–9.
- [ ] Erase clears user entries but never givens.
- [ ] Notes can be toggled and do not count as placed values.
- [ ] Undo/redo restores correct board and note states.
- [ ] Hint points to a correct value in the current solution.
- [ ] Conflicts/highlights update when expected.
- [ ] Mistake highlighting can be switched off without disabling completion validation.
- [ ] Empty/partial/invalid boards do not trigger victory.
- [ ] Completed valid board triggers the result screen exactly once.
- [ ] Restart/abandon confirmation behaves correctly.
- [ ] Timer pause/resume behavior matches the product rule.

## D. Persistence and offline
- [ ] An unfinished game restores after app background/resume.
- [ ] An unfinished game restores after force-stop/relaunch.
- [ ] Entries, notes, difficulty, elapsed time, hints and mistakes restore accurately.
- [ ] Completed results are not recorded twice after repeated resume/reopen.
- [ ] Settings and theme persist after restart.
- [ ] App remains playable in airplane mode.
- [ ] No network dependency blocks new puzzle, save, resume, stats, or themes.
- [ ] Corrupted/migrated save state is handled gracefully rather than crashing.

## E. Daily challenge, statistics, streaks, achievements
- [ ] Same UTC date key + generator version results in stable challenge identity.
- [ ] Date transition selects the next daily puzzle according to documented rule.
- [ ] Reopen/replay on the same date does not add a duplicate completion.
- [ ] Streak advances once per qualifying date only.
- [ ] Missed date behavior is correct and documented.
- [ ] Statistics calculations match known test sequences.
- [ ] Achievements unlock once and remain unlocked after restart.
- [ ] Achievement and streak changes are not duplicated by lifecycle callbacks.
- [ ] UI never claims an offline local result has been submitted globally.

## F. Themes, responsiveness, accessibility
- [ ] Light theme is readable and complete.
- [ ] Dark theme is readable and complete.
- [ ] Every custom palette preserves contrast for givens, entries, notes, selection and conflicts.
- [ ] System theme mode follows device setting.
- [ ] Small phone layout has no clipped controls or grid overflow.
- [ ] Typical phone layout allows grid and primary controls to remain usable without scrolling during normal play.
- [ ] Tablet layout is intentional, not merely stretched.
- [ ] Portrait and landscape behavior is handled consistently.
- [ ] Larger font size does not clip essential actions.
- [ ] TalkBack labels describe cells and controls usefully.
- [ ] Tap targets are comfortably sized.
- [ ] Motion is non-blocking and essential information is not conveyed by color alone.

## G. Monetization and consent (only after ads are integrated)
- [ ] App core remains usable if MAX SDK initialization fails.
- [ ] No ad at app launch.
- [ ] No interstitial during active puzzle play.
- [ ] Interstitial frequency caps are enforced across navigation and app lifecycle.
- [ ] Rewarded ad is always user-initiated and reward description is clear.
- [ ] Reward is delivered once after earned callback only.
- [ ] No reward is lost due to duplicate callback; no duplicate reward if callback repeats.
- [ ] Ad unavailable / no-fill / early close has no negative effect on the puzzle.
- [ ] Test mode/test ad units were used for QA; no live ads were clicked.
- [ ] Required consent flow is respected before relevant ad requests.
- [ ] Consent denial / restricted personalization paths work.
- [ ] Ads cannot be confused with number pad buttons.
- [ ] Privacy, Data safety and ad network disclosures reflect actual integrated SDKs.

## H. Online leaderboard (only after backend milestone)
- [ ] Server determines canonical challenge ID/version.
- [ ] Server rejects invalid puzzle, impossible elapsed time, malformed score and duplicate submissions.
- [ ] Rate limiting and replay/idempotency protection are tested.
- [ ] Offline result is visibly pending until accepted.
- [ ] Rejected submissions are shown accurately.
- [ ] Public player identifiers and display names are privacy-conscious.
- [ ] Server—not local client state—determines ranked records.

## I. Release candidate
- [ ] Current target API requirement verified in official Google Play documentation.
- [ ] Current MAX minimum SDK and adapter compatibility verified.
- [ ] Release build uses pinned compatible dependencies.
- [ ] Application ID, version code/name and app icon are final.
- [ ] Install/update path is tested.
- [ ] Privacy policy URL works.
- [ ] Play Console ads, target audience, Data safety, content rating and SDK disclosures completed accurately.
- [ ] Internal testing track build passes physical-device smoke test.
- [ ] Source ZIP and release build are backed up.
