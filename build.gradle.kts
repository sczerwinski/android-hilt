plugins {
    id(libs.plugins.android.library.get().pluginId) apply false
    id(libs.plugins.kotlin.jvm.get().pluginId) apply false
    id(libs.plugins.kotlin.android.get().pluginId) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt) apply false
    id(libs.plugins.dokka.get().pluginId) apply false
    alias(libs.plugins.changelog)
}

allprojects {

    val libGroupId: String by project
    val libVersion: String by project
    val libVersionSuffix: String by project

    group = libGroupId
    version = if (libVersionSuffix.isBlank()) libVersion else "$libVersion-$libVersionSuffix"
}

changelog {
    version.set("${project.version}")
}
