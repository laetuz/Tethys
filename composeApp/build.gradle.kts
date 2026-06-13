plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(project(":shared"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
        androidMain.dependencies {
            implementation(project(":shared"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.android)
            implementation(libs.androidx.activity.compose)
        }
        desktopMain.dependencies {
            implementation(project(":shared"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(compose.desktop.currentOs)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
    }
}

configure<com.android.build.api.dsl.ApplicationExtension> {
    namespace = "id.neotica.tethys"
    compileSdk = 36

    defaultConfig {
        applicationId = "id.neotica.tethys"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
