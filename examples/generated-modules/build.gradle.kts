plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
}

android {

    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(16) // AndroidX Browser requires minSdk 16
        targetSdkVersion(30)

        applicationId = "it.czerwinski.android.hilt.examples.generated"

        versionCode = 1
        versionName = "${project.version}"

        multiDexEnabled = true

        testInstrumentationRunner = "it.czerwinski.android.hilt.examples.generated.HiltAndroidJUnitRunner"
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
    implementation("com.google.dagger:hilt-android:2.37")
    kapt("com.google.dagger:hilt-android-compiler:2.37")

    implementation(project(":hilt:extensions"))
    kapt(project(":hilt:processor"))

    implementation("com.google.android.material:material:1.4.0")

    implementation("androidx.activity:activity-ktx:1.2.3")

    implementation("androidx.multidex:multidex:2.0.1")

    implementation("androidx.browser:browser:1.3.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    implementation("io.ktor:ktor-client-android:1.6.1")
    implementation("io.ktor:ktor-client-serialization:1.6.1")
    implementation("io.ktor:ktor-client-logging:1.6.1")

    implementation("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")
    implementation("androidx.sqlite:sqlite-ktx:2.1.0")
    implementation("it.czerwinski.android.room:room-extensions:1.0.1")

    implementation("joda-time:joda-time:2.10.10")

    implementation("com.squareup.picasso:picasso:2.8")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.38")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.37")

    androidTestImplementation("io.ktor:ktor-client-mock:1.6.1")

    androidTestImplementation("it.czerwinski.android:xpresso-core:1.0")
    androidTestImplementation("it.czerwinski.android:xpresso-recyclerview:1.0")
}
