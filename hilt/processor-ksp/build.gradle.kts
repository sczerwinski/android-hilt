import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.8.20-1.0.10"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("org.jetbrains.dokka")
    `maven-publish`
    signing
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":hilt:annotations"))

    implementation("androidx.annotation:annotation:1.6.0")
    implementation("com.google.dagger:hilt-core:2.45")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.20-1.0.11")
    implementation("com.squareup:kotlinpoet:1.13.0")
    implementation("com.squareup:kotlinpoet-ksp:1.13.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("io.mockk:mockk:1.13.4")
    kspTest(project(":hilt:processor-ksp"))
    testImplementation("ch.qos.logback:logback-classic:1.4.6")
}

detekt {
    config = files("../../config/detekt/detekt.yml")
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
