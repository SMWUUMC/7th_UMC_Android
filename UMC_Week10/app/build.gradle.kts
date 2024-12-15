import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

// 그래들 8.9
plugins {
    alias(libs.plugins.jetbrainsKotlinAndroid) // 또는 id("kotlin-android")
    id("kotlin-kapt")
    // id("org.jetbrains.kotlin.jvm") version "1.9.20"
    id("com.android.application") version "8.3.0"
}

android {
    namespace = "com.haeun.umc_week3"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.haeun.umc_week3"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8" //////
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("me.relex:circleindicator:2.1.6")
    implementation("com.google.code.gson:gson:2.11.0")

    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")  // Room 런타임
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("com.google.android.gms:play-services-auth:20.6.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")

    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    implementation("androidx.databinding:databinding-runtime:7.0.0")


}

kapt {
    arguments {
        arg("-Xadd-opens", "jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED")
    }
    correctErrorTypes = true
}
