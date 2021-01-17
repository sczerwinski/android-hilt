buildscript {

    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21-2")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.21")
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.31-alpha")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.7.0.0")
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
        jcenter()
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.changelog") version "1.0.1"
}

changelog {
    version = "${project.version}"
}
