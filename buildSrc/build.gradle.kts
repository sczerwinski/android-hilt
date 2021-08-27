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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.1")
    implementation("com.android.tools.build:gradle-api:7.0.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.30")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
    implementation("org.jetbrains.dokka:dokka-core:1.5.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.5.0")
}
