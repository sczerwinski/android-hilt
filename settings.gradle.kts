pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "android-hilt"

include(":hilt:annotations")
include(":hilt:extensions")
include(":hilt:fragment-testing")
include(":hilt:processor")
include(":hilt:processor-ksp")

include(":tests:fragment-testing-test")

include(":examples:generated-modules")
