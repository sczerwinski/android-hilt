plugins {
    `kotlin-dsl`
}

buildscript {

    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
    }
}

repositories {
    google()
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.1.2")
    implementation("com.android.tools.build:gradle-api:4.1.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.31")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
    implementation("org.jetbrains.dokka:dokka-core:1.4.20")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.4.20")
}
