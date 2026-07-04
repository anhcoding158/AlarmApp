import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
}

val buildNumber = System.getenv("BUILD_NUMBER")?.toIntOrNull() ?: 1

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("KEYSTORE_FILE") ?: "my_key.jks")
            storePassword = "12345678"
            keyAlias = "key0"
            keyPassword = "12345678"
        }
    }

    defaultConfig {
        applicationId = "com.example.myapplication"

        minSdk = 24
        targetSdk = 34

        // Tự động tăng theo số lần Jenkins build
        versionCode = buildNumber
        versionName = "1.0.$buildNumber"

        println("======================================")
        println("VERSION CODE : $versionCode")
        println("VERSION NAME : $versionName")
        println("BUILD NUMBER : $buildNumber")
        println("======================================")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")

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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    val roomVersion = "2.6.1"

    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    implementation("com.google.android.material:material:1.12.0")
}