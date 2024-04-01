buildscript {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.21")
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.9.3.0")
    }
}

allprojects {

    val libGroupId: String by project
    val libVersion: String by project
    val libVersionSuffix: String by project

    group = libGroupId
    version = if (libVersionSuffix.isBlank()) libVersion else "$libVersion-$libVersionSuffix"

    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.changelog") version "2.0.0"
}

changelog {
    version.set("${project.version}")
}
