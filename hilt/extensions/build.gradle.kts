plugins {
    id("com.android.library")
    kotlin("android")
    id("de.mannodermaus.android-junit5")
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

android {

    compileSdk = 31

    defaultConfig {
        minSdk = 16
        targetSdk = 31
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(project(":hilt:annotations"))

    implementation("com.google.dagger:hilt-android:2.41")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("io.mockk:mockk:1.12.3")
}

tasks {
    dokkaJavadoc { setUpJavadocTask(project) }
    dokkaJekyll { setUpJekyllTask(project) }

    artifacts {
        archives(createJavadocJar(dokkaJavadoc))
        archives(createSourcesJar(android.sourceSets.named("main").get().java.srcDirs))
    }
}

afterEvaluate {
    publishing {
        publications { registerAarPublication(project) }
        repositories { sonatype(project) }
    }
    signing { signAllMavenPublications(project, publishing) }
}
