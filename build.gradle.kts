buildscript {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.20")
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.45")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.8.2.0")
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
