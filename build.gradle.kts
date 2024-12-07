// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0") // 최신 버전으로 업데이트
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0") // 최신 Kotlin 버전으로 업데이트
    }
}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
