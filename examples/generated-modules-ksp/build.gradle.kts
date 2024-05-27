plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.detekt)
}

android {

    compileSdk = 34

    namespace = "it.czerwinski.android.hilt.examples.generated"

    defaultConfig {
        minSdk = 26
        targetSdk = 34

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

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(project(":hilt:extensions"))
    ksp(project(":hilt:processor-ksp"))

    implementation(libs.material)

    implementation(libs.androidx.activity.ktx)

    implementation(libs.androidx.multidex)

    implementation(libs.androidx.browser)

    implementation(libs.bundles.androidx.lifecycle)

    implementation(libs.bundles.ktor)

    implementation(libs.logback.classic)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.sqlite.ktx)
    implementation(libs.czerwinski.android.room)

    implementation(libs.picasso)

    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

    androidTestImplementation(libs.androidx.test.core)

    androidTestImplementation(libs.ktor.client.mock)

    androidTestImplementation(libs.bundles.czerwinski.xpresso)
}

detekt {
    config.from(file("../../config/detekt/detekt.yml"))
    buildUponDefaultConfig  = true
}
