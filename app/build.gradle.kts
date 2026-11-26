plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.DroidtesterApp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.DroidtesterApp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Menggunakan Android Framework bawaan (android.app.Activity),
    // sehingga tidak memerlukan dependensi eksternal tambahan.
}
