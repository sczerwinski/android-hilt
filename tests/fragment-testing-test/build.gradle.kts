plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

android {

    compileSdk = 33

    namespace = "it.czerwinski.android.hilt.fragment.testing.tests"

    defaultConfig {
        minSdk = 16
        targetSdk = 33

        applicationId = "it.czerwinski.android.hilt.fragment.testing.tests"

        versionCode = 1
        versionName = "${project.version}"

        testInstrumentationRunner = "it.czerwinski.android.hilt.fragment.testing.tests.test.HiltAndroidJUnitRunner"
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

    implementation("androidx.fragment:fragment-ktx:1.5.6")
    implementation("com.google.android.material:material:1.8.0")

    androidTestImplementation("androidx.test:runner:1.5.2")
    debugImplementation("androidx.fragment:fragment-testing:1.5.6")
    debugImplementation("androidx.test:monitor:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.45")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.45")
    debugImplementation(project(":hilt:fragment-testing"))
}