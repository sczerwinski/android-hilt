plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
    alias(libs.plugins.detekt)
    id(libs.plugins.dokka.get().pluginId)
    `maven-publish`
    signing
}

kotlin.jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
}

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
}

dependencies {
    implementation(libs.hilt.core)
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
        archives(createSourcesJar(sourceSets.named("main").get().java.srcDirs))
    }
}

afterEvaluate {
    publishing {
        publications { registerJarPublication(project) }
        repositories { sonatype(project) }
    }
    signing { signAllMavenPublications(project, publishing) }
}
