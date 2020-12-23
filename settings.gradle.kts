pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "android-hilt"

include(":hilt:extensions")
include(":hilt:processor")
