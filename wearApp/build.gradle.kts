plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
fun keystoreProperty(key: String): String? {
    if (!keystorePropertiesFile.exists()) return null
    return keystorePropertiesFile.readLines()
        .firstOrNull { it.startsWith("$key=") }
        ?.substringAfter("=")
        ?.trim()
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

android {
    namespace = "id.neotica.tethys"
    compileSdk = 36

    defaultConfig {
        applicationId = "id.neotica.tethys"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    val hasReleaseKeys = keystoreProperty("storeFile") != null
    if (hasReleaseKeys) {
        signingConfigs {
            create("release") {
                storeFile = rootProject.file(keystoreProperty("storeFile")!!)
                storePassword = keystoreProperty("storePassword") ?: ""
                keyAlias = keystoreProperty("keyAlias") ?: ""
                keyPassword = keystoreProperty("keyPassword") ?: ""
            }
        }
    }

    buildTypes {
        create("debugRelease") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName(if (hasReleaseKeys) "release" else "debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.wear.compose.material)
    implementation(libs.wear.compose.foundation)
    implementation(libs.watchface)
    implementation(libs.androidx.activity.compose)
}
