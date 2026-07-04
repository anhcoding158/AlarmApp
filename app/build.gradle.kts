import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

//=============================
// AUTO VERSION
//=============================

val versionFile = rootProject.file("version.properties")

val props = Properties()

if (versionFile.exists()) {
    versionFile.inputStream().use {
        props.load(it)
    }
}

val today = SimpleDateFormat("yyyyMMdd").format(Date())

val lastDate = props.getProperty("lastDate", "")
var buildCount = props.getProperty("buildCount", "0").toInt()
var versionCodeValue = props.getProperty("versionCode", "0").toInt()

if (today == lastDate) {
    buildCount++
} else {
    buildCount = 1
}

versionCodeValue++

props["lastDate"] = today
props["buildCount"] = buildCount.toString()
props["versionCode"] = versionCodeValue.toString()

versionFile.outputStream().use {
    props.store(it, "Auto generated. DO NOT EDIT")
}

val versionNameValue = "${today}_${String.format("%02d", buildCount)}"

//=============================

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

        versionCode = versionCodeValue
        versionName = versionNameValue

        println("===================================")
        println("VersionCode : $versionCodeValue")
        println("VersionName : $versionNameValue")
        println("===================================")

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
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

    annotationProcessor(
        "androidx.room:room-compiler:$roomVersion"
    )

    implementation("com.google.android.material:material:1.12.0")
}