# LVNG - ZORCH

Android Studio / Gradle source project for an independently rebuilt LVNG - ZORCH app shell.

## What's included

- New Android application ID: `com.lvng.zorch`
- App label: `LVNG - ZORCH`
- User-provided pink/navy logo installed as the launcher icon and kept in `branding/`
- Dark navy and neon-pink interface
- Home / game library screen with Free Fire launch intent (only if installed)
- Account screen with unlimited local credit display and no premium purchase/paywall UI
- Settings screen
- Local Key Mapping preset list (add/remove labels and persist locally)
- Local sensitivity preference sliders (persist locally)
- Macro Record demo status panel
- Additional local preferences
- GitHub Actions workflow that builds a Debug APK and uploads it as an artifact

## Important feature boundary

The supplied Nebula Touch package was an APK bundle, not its original source project. This project is a clean-room Android implementation based on the screenshots and app details supplied in the conversation. It does **not** contain Nebula's original native mapping engine, touch injection/accessibility service, cloud export/import backend, Firebase integration, or game-specific optimization code.

The Key Mapping screen currently manages local preset labels only. Sensitivity sliders save local preferences only. Macro Record toggles a local demo recording state; it does not capture/replay input or control a game. No feature bypasses a game's anti-cheat or modifies third-party game files. These native capabilities require a separately designed, authorized implementation and real-device testing.

## Build on GitHub

1. Create or open your GitHub repository.
2. Upload the **contents** of this ZIP to the repository root (not the enclosing ZIP folder).
3. Commit to the `main` branch.
4. Open **Actions** → **Build LVNG - ZORCH APK**.
5. Wait for the workflow to finish successfully.
6. Open the workflow run and download the `LVNG-ZORCH-debug-apk` artifact.
7. Extract the artifact ZIP to get `app-debug.apk`.

The workflow uses JDK 17, Android SDK, and Gradle 8.11.1. It runs on pushes to `main` and can also be started manually with **Run workflow**.

## Build locally (optional)

With JDK 17, Android SDK platform 35, and Gradle 8.11.1 installed:

```bash
gradle --no-daemon assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## Package identity

- Namespace: `com.lvng.zorch`
- Application ID: `com.lvng.zorch`
- Version: `1.0.0` (versionCode 1)
