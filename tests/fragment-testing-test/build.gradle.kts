plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    alias(libs.plugins.hilt)
    alias(libs.plugins.detekt)
}

android {

    compileSdk = 34

    namespace = "it.czerwinski.android.hilt.fragment.testing.tests"

    defaultConfig {
        minSdk = 19
        targetSdk = 34

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

    packaging {
        resources.excludes.add("META-INF/INDEX.LIST")
    }
}

kotlin.jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
}

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.material)

    androidTestImplementation(libs.androidx.test.runner)
    debugImplementation(libs.androidx.fragment.testing)
    debugImplementation(libs.androidx.test.monitor)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    debugImplementation(project(":hilt:fragment-testing"))
}

detekt {
    config.from(file("../../config/detekt/detekt.yml"))
    buildUponDefaultConfig  = true
}
