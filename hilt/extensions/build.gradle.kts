plugins {
    id("com.android.library")
    kotlin("android")
    id("de.mannodermaus.android-junit5")
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

android {

    compileSdk = 33

    namespace = "it.czerwinski.android.hilt"

    defaultConfig {
        minSdk = 16
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

kotlin.jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
}

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
}

dependencies {
    api(project(":hilt:annotations"))

    implementation("com.google.dagger:hilt-android:2.45")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("ch.qos.logback:logback-classic:1.4.7")
}

detekt {
    config = files("../../config/detekt/detekt.yml")
    buildUponDefaultConfig  = true
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
    tasks {
        getByName("generateMetadataFileForLibAarPublication")
            .dependsOn(getByName("sourcesJar"))
    }
}
