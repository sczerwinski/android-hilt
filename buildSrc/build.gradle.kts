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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.21")
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.3")
    implementation("com.android.tools.build:gradle-api:7.1.3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.20")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
    implementation("org.jetbrains.dokka:dokka-core:1.6.10")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
    implementation("com.squareup:javapoet:1.13.0") // Workaround for Hilt 2.40.2 issue
}
