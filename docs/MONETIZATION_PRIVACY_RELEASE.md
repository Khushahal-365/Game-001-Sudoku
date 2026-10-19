# Monetization, Privacy, and Release Checklist

## 1. Monetization model

### Rewarded ads
- Show a clear, user-initiated offer such as “Watch an ad for 1 extra hint”.
- Explain the reward before the ad opens.
- Deliver the reward only from the SDK's earned-reward callback; make the callback idempotent.
- If no ad is available, loading fails, the user closes early, or the app loses connectivity, the game remains usable and the user is not penalized.
- Never force an ad to start or finish a puzzle.

### Interstitial ads
- No startup interstitial.
- No full-screen ads while the user is solving, selecting a cell, typing a number, or navigating a game control.
- Only eligible after a completion/result screen or another clear natural break.
- Initial cap: no more than one after every three completed standard games and no more than three per day. Exclude daily challenge completion if it would feel disruptive; revisit only after user-experience testing and policy review.
- Ads must have a clear dismissal path and must not use misleading close controls.

### Banner ads
- Optional only on secondary screens such as Home or Statistics if they do not crowd interactive content.
- Never place beside/under the number pad where taps could be accidental.
- Do not ship a banner just because an ad unit exists; measure usability first.

## 2. MAX integration controls

Before implementation:
- Create the app and ad units in AppLovin MAX.
- Confirm app approval and serving readiness for every intended network.
- Choose only networks that are supported by the current MAX SDK and whose accounts/ad units are approved.
- Follow the current official MAX Android integration documentation for Gradle dependency, SDK key, adapter version and initialization.
- Pin known-compatible stable SDK/adapter versions; avoid floating `+` dependency versions in the release build.
- Test initialization failure, no-fill, network timeout, activity lifecycle, rotation/background/resume, and duplicate callbacks.
- Use test mode/test ad units until a QA-approved release candidate is ready. Never click live ads as a test method.

The MAX SDK key is an app integration identifier embedded/configured according to MAX's Android setup. Still, do not share AppLovin account passwords, dashboard credentials, billing information, or other account secrets in prompts or source control.

## 3. Consent and user privacy

- Determine target countries and the actual audience before configuring personalized ads.
- Implement the current consent process required by applicable law, Google Play policy, AppLovin, and each mediated network. Re-check official SDK guidance at integration time because requirements and APIs change.
- Do not request or initialize personalized ad serving before the required consent state is resolved where consent is required.
- Provide a privacy policy accessible from the app and store listing.
- Complete Play Console Data safety, Ads, content rating, target audience and any SDK declarations based on what the shipped binary actually does.
- Review each included ad SDK/network's data collection and sharing disclosures. Do not guess Data safety answers from a generic template.
- Avoid unnecessary permissions. Sudoku does not need location, contacts, camera, microphone, or phone state for core play.
- No account is required for offline play. If online accounts are added later, add clear privacy, data retention/deletion and security behavior.
- Decide and declare the audience honestly; do not market specifically to children unless the app is redesigned and configured for the applicable Families requirements.

## 4. Google Play release

- Check the current Android target API requirement immediately before submission. On 19 October 2026, Google's published requirement effective 31 August 2026 states new apps and updates must target Android 16 / API 36 or higher.
- Check AppLovin MAX's current minimum SDK requirement; the currently consulted Android integration page states minSdkVersion 24 or above.
- Verify application ID, version name/code, package structure, release signing, icons, screenshots, store copy and support contact.
- Verify app behavior with no internet, no ad fill, consent denied, and SDK initialization failure.
- Check current Google Play policies: ads must not surprise users during gameplay or obstruct normal use.
- Run internal testing before production rollout. Keep a known-good source ZIP and release artifact.
- Confirm privacy policy URL is publicly reachable and accurately describes actual app/SDK behavior.

## 5. Official references to re-check

- Google AI Studio Android build: https://ai.google.dev/gemini-api/docs/aistudio-android
- AppLovin MAX Android integration: https://developers.applovin.com/en/max/android/overview/integration/
- AppLovin MAX getting started: https://developers.applovin.com/en/max/getting-started/
- Google Play target API levels: https://developer.android.com/google/play/requirements/target-sdk
- Google Play Ads policy: https://support.google.com/googleplay/android-developer/answer/9857753
- Google AdMob privacy strategy reference (also useful as background for mediated Google demand): https://developers.google.com/admob/android/privacy/strategies
