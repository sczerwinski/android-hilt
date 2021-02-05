plugins {
    id("com.android.library")
    kotlin("android")
    id("de.mannodermaus.android-junit5")
    id("io.gitlab.arturbosch.detekt") version "1.15.0"
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

android {

    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(14)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "${project.version}"
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

    implementation("com.google.dagger:hilt-android:2.31.2-alpha")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    testImplementation("io.mockk:mockk:1.10.5")
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
