# Developer Agent Guide: Tethys (ASCII Shark)

This repository contains a Kotlin Multiplatform (KMP) project targeting Android (phone), Compose Desktop, and Wear OS. Follow these critical guidelines to build, compile, and maintain the codebase safely.

---

## 1. Project & Module Architecture
The project is split into three modules:
*   **`:shared`** — KMP library module containing all core entities, MVI logic (`TethysViewModel`, `TethysUiState`), theme elements (`Color`, `Theme`, `Type`), ASCII art, and utilities (`BrailleUtils`).
    *   *Targets:* `androidTarget` + `jvm("desktop")`.
*   **`:composeApp`** — KMP application module providing phone and desktop entry points.
    *   *Targets:* `androidTarget` (produces APK) + `jvm("desktop")` (Desktop JVM app).
    *   *Transitive Dependency Quirk:* Even though `:composeApp` depends on `:shared`, KMP's variant resolution model does **not** transitively expose libraries across multiplatform module boundaries. You **must** duplicate explicit `implementation` declarations (such as Koin and Compose dependencies) in `:composeApp/build.gradle.kts`'s sourceSets so they are visible during compilation.
*   **`:wearApp`** — Android-only Wear OS application (not KMP).
    *   *Targets:* Android Wear. Depends on `:shared`'s Android AAR output.

---

## 2. Toolchain & Gradle Quirks

*   **AGP 9.2.1 Compatibilities:**
    *   KMP's `kotlin.multiplatform` plugin is incompatible with AGP 9.x's new DSL. You **must** keep `android.newDsl=false` in `gradle.properties`.
    *   `android.builtInKotlin=false` is set globally in `gradle.properties` to prevent conflicts between KMP and AGP's built-in Kotlin support.
*   **Wear OS Kotlin Compilation:**
    *   Because `android.builtInKotlin=false` is set globally, the `:wearApp` module (despite being non-KMP) **must** explicitly apply the `kotlin-android` (`org.jetbrains.kotlin.android`) plugin to compile Kotlin files into the APK's dex file. Otherwise, you will get a silent `ClassNotFoundException` at runtime.
*   **JVM Target Alignment:**
    *   The project is configured for **Java 11** (`JavaVersion.VERSION_11`).
    *   Kotlin 2.4.x default compilation target is JVM 21. In `:wearApp/build.gradle.kts`, you **must** explicitly constrain the Kotlin compiler target to match Java:
        ```kotlin
        kotlin {
            compilerOptions {
                jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
            }
        }
        ```

---

## 3. Developer Commands & Verification Steps

*   **Clean Cache:** Clean Gradle configurations and build directories before verifying major structural changes:
    ```bash
    rm -rf build .gradle composeApp/build composeApp/.gradle shared/build shared/.gradle wearApp/build wearApp/.gradle
    ```
*   **Build Desktop Target:**
    ```bash
    ./gradlew composeApp:compileKotlinDesktop
    ```
*   **Build Android Debug Target (Phone):**
    ```bash
    ./gradlew composeApp:compileDebugKotlinAndroid
    ```
*   **Build Phone `debugrelease` APK (minified + shrunk):**
    ```bash
    ./gradlew composeApp:assembleDebugRelease
    ```
*   **Build Wear OS Target:**
    ```bash
    ./gradlew :wearApp:assembleDebug
    ```
*   **Build Wear OS `debugrelease` APK (minified + shrunk):**
    ```bash
    ./gradlew :wearApp:assembleDebugRelease
    ```
*   **Create a Release (GitHub Actions):**
    Push a version tag to trigger the CI/CD pipeline that builds both APKs and publishes a GitHub Release:
    ```bash
    git tag v1.0.0 && git push origin v1.0.0
    ```
    The workflow is defined in `.github/workflows/release.yml`. It builds both phone and Wear OS APKs, uploads them as build artifacts on every push, and creates a GitHub Release with the APKs attached when a tag matching `v*` is pushed.

---

## 4. Compose Compiler & Lambda Gotchas (Kotlin 2.x)

*   **Named Parameter for Cross-Module Themes:**
    In Kotlin 2.x, the Compose compiler plugin does not always infer a trailing lambda across module boundaries as `@Composable () -> Unit` (inferring it as `() -> Unit` instead, which causes a `ComposableFunction0` type mismatch).
    *   *Rule:* Always call `TethysTheme` from other modules (like `:wearApp`) using the explicit `content` named parameter:
        ```kotlin
        // WRONG (will fail to compile in Wear App)
        TethysTheme { WatchFace() }

        // CORRECT
        TethysTheme(content = { WatchFace() })
        ```

---

## 5. Wear OS Watch Face Specifics

*   **Watch Face Service:**
    The Watch Face is implemented programmatically as a `WatchFaceService` in `:wearApp` using `androidx.wear.watchface:watchface` (version `1.2.1`).
*   **Renderer:**
    It uses `Renderer.CanvasRenderer2<Renderer.SharedAssets>` and renders using low-level `Canvas.drawText` with `Paint` and `Typeface.MONOSPACE` since Compose-based Watch Face renderers are not stable or bundled in standard libraries.
*   **Required Overrides:**
    To ensure the watch face appears in the Wear OS watch face picker (and does not crash during preview generation), you **must** provide concrete implementations for the following protected overrides in `TethysWatchFaceService`:
    *   `createUserStyleSchema()` returning `UserStyleSchema(emptyList())`
    *   `createComplicationSlotsManager()` returning `ComplicationSlotsManager(emptyList(), currentUserStyleRepository)`
*   **Required Manifest Metadata:**
    The Wear OS Watch Face Manager strictly requires the `com.google.android.wearable.watchface` `<meta-data>` tag in `AndroidManifest.xml` pointing to a valid `@xml/watch_face` resource (e.g. `<WatchFace />`) for the service to be discoverable in the watch face picker list. Without this XML metadata file, the watch face will be completely ignored by the system's watch face list.
*   **Wear OS 5 API Level Workaround:**
    Wear OS 5 (API 34+) enforces Watch Face Format (WFF) validation for apps targeting SDK 34+. Programmatic `WatchFaceService` implementations are silently ignored by the picker unless the app targets a lower API level. Set `compileSdk = 33` and `targetSdk = 33` in `wearApp/build.gradle.kts` to bypass this restriction (the watch face will appear in the "Downloaded" section on Samsung Galaxy Watch 6 and similar devices).
*   **Watch Face Animation:**
    The watch face renderer alternates between `SHARK_NORMAL` and `SHARK_NORMAL2` every 400ms (using `zonedDateTime.toInstant().toEpochMilli()`) for swimming animation, plus a sinusoidal bob (`sin(epochMillis * 0.005) * 4f`). Font size is calculated dynamically as `bounds.width() / maxLineLength` to fit the widest ASCII line within the screen.
*   **ASCII Frame Export:**
    Both animation frames must be public for the watch face to access them. `SHARK_NORMAL` (public) and `SHARK_NORMAL2` (renamed from private `SHARK_NORMAL_2`) are exported from `shared`'s `Shark.kt`.

