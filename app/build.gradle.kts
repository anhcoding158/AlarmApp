import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    // Cấu hình ký ứng dụng để có thể cài đặt được file Release
    signingConfigs {
        create("release") {
            // Thay vì trỏ trực tiếp đến file, hãy dùng biến môi trường
            storeFile = file(System.getenv("KEYSTORE_FILE") ?: "my_key.jks")
            storePassword = "12345678"
            keyAlias = "key0"
            keyPassword = "12345678"
        }
    }

    // TỰ ĐỘNG: Lấy số bản build từ Jenkins (mặc định là 1 nếu chạy ở máy cá nhân)
    val jenkinsBuildNumber = System.getenv("BUILD_NUMBER")?.toIntOrNull() ?: 1

    // TỰ ĐỘNG: Lấy ngày tháng năm hiện tại (Ví dụ: 20260704)
    val currentDate = SimpleDateFormat("yyyyMMdd").format(Date())

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34

        // versionCode giữ nguyên là số để Google Play/Android hiểu được
        versionCode = jenkinsBuildNumber

        // versionName sẽ ra kết quả dạng: 20260704-01, 20260704-02...
        val paddedBuildNumber = String.format("%02d", jenkinsBuildNumber) // Thêm số 0 ở đầu nếu build < 10
        versionName = "$currentDate-$paddedBuildNumber"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") { // Phải dùng getByName trong Kotlin DSL
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

    // Thư viện Room Database
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")

    // Lưu ý nhỏ: Nếu code Room Database bằng Kotlin, bạn nên đổi annotationProcessor thành ksp (hoặc kapt)
    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation("com.google.android.material:material:1.12.0")
}