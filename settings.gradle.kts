pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
include(":examples:generated-modules-ksp")
