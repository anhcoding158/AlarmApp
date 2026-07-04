import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
}

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

    // --- CẤU HÌNH VERSION ---
    // Nếu muốn tự động (theo ngày): để là null
    // Nếu muốn ép kiểu thủ công: đổi thành ví dụ "20260704-99"
    val manualVersion: String? = "20260704-99"

    val jenkinsBuildNumber = System.getenv("BUILD_NUMBER")?.toIntOrNull() ?: 1
    val currentDate = SimpleDateFormat("yyyyMMdd").format(Date())

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34

        versionCode = jenkinsBuildNumber

        // Logic: Nếu manualVersion có giá trị, lấy nó. Không thì lấy tự động.
        val paddedBuildNumber = String.format("%02d", jenkinsBuildNumber)
        versionName = manualVersion ?: "$currentDate-$paddedBuildNumber"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation("com.google.android.material:material:1.12.0")
}