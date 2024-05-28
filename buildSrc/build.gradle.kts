plugins {
    `kotlin-dsl`
}

buildscript {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath(libs.kotlin.gradle.plugin)
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.gradle)
    implementation(libs.gradle.api)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.dokka.core)
    implementation(libs.dokka.gradle.plugin)
    implementation(libs.javapoet) // Workaround for Hilt 2.40.2 issue
}
