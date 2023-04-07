plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

android {

    compileSdk = 33

    defaultConfig {
        minSdk = 16
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true
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

    packagingOptions {
        resources.excludes.add("META-INF/INDEX.LIST")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-android-compiler:2.45")

    api("androidx.test:core:1.5.0")

    androidTestImplementation("androidx.test:runner:1.5.2")
    debugImplementation("androidx.fragment:fragment-testing:1.5.6") {
        exclude(group = "androidx.text", module = "monitor")
        exclude(group = "androidx.activity", module = "activity-ktx")
    }
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("com.google.android.material:material:1.8.0")
    androidTestImplementation("androidx.activity:activity-ktx:1.7.0")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.45")
    androidTestImplementation("androidx.multidex:multidex:2.0.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.45")
}

configurations.all {
    resolutionStrategy {
        force("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
        force("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    }
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
