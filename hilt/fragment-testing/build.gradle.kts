plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.41")
    kapt("com.google.dagger:hilt-android-compiler:2.41")

    api("androidx.test:core:1.4.0")

    androidTestImplementation("androidx.test:runner:1.4.0")
    debugImplementation("androidx.fragment:fragment-testing:1.4.1") {
        exclude(group = "androidx.text", module = "monitor")
        exclude(group = "androidx.activity", module = "activity-ktx")
    }
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("com.google.android.material:material:1.5.0")
    androidTestImplementation("androidx.activity:activity-ktx:1.4.0")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.41")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.41")
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
