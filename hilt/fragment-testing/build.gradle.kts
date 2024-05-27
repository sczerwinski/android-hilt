plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    alias(libs.plugins.hilt)
    alias(libs.plugins.detekt)
    id(libs.plugins.dokka.get().pluginId)
    `maven-publish`
    signing
}

android {

    compileSdk = 34

    namespace = "it.czerwinski.android.hilt.fragment.testing"

    defaultConfig {
        minSdk = 19
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

    api(libs.androidx.test.core)
    api(libs.androidx.fragment.ktx)
    api(libs.androidx.activity.ktx)
}

configurations.all {
    resolutionStrategy {
        val activityDependency = libs.androidx.activity.ktx.get()
        force("${activityDependency.group}:${activityDependency.name}:${activityDependency.version}")
    }
}

detekt {
    config.from(file("../../config/detekt/detekt.yml"))
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
