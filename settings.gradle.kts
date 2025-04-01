// Configure how Gradle finds and uses plugins
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://nexus.inductiveautomation.com/repository/public/")
        }
    }
}

// Configure how Gradle resolves dependencies
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        mavenLocal()
        maven {
            url = uri("https://nexus.inductiveautomation.com/repository/public/")
        }
        mavenCentral()
    }
}

// Enable type-safe project accessors for cleaner build scripts
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// Set the root project name
rootProject.name = "perspective-browser-resource"

// Include all subprojects in the build
include(":gateway")