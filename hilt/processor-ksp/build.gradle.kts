plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
    alias(libs.plugins.ksp)
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

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":hilt:annotations"))

    implementation(libs.androidx.annotation)
    implementation(libs.hilt.core)
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.logback.classic)
    kspTest(project(":hilt:processor-ksp"))
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
