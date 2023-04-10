plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp") version "1.8.20-1.0.10"
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

android {

    compileSdk = 33

    namespace = "it.czerwinski.android.hilt.examples.generated"

    defaultConfig {
        minSdk = 16
        targetSdk = 33

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

    implementation(project(":hilt:extensions"))
    ksp(project(":hilt:processor-ksp"))

    implementation("com.google.android.material:material:1.8.0")

    implementation("androidx.activity:activity-ktx:1.7.0")

    implementation("androidx.multidex:multidex:2.0.1")

    implementation("androidx.browser:browser:1.5.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    implementation("io.ktor:ktor-client-android:2.2.4")
    implementation("io.ktor:ktor-client-resources:2.2.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
    implementation("io.ktor:ktor-client-logging:2.2.4")

    // Logback 1.4.x requires Java 11:
    @Suppress("GradleDependency")
    implementation("ch.qos.logback:logback-classic:1.3.6")

    implementation("androidx.room:room-runtime:2.5.1")
    ksp("androidx.room:room-compiler:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")
    implementation("androidx.sqlite:sqlite-ktx:2.3.1")
    implementation("it.czerwinski.android.room:room-extensions:1.2.0")

    implementation("joda-time:joda-time:2.10.14")

    implementation("com.squareup.picasso:picasso:2.8")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.45")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.45")

    androidTestImplementation("androidx.test:core:1.5.0")

    androidTestImplementation("io.ktor:ktor-client-mock:2.0.0")

    androidTestImplementation("it.czerwinski.android:xpresso-core:1.0")
    androidTestImplementation("it.czerwinski.android:xpresso-recyclerview:1.0")
}

detekt {
    config = files("../../config/detekt/detekt.yml")
    buildUponDefaultConfig  = true
}
