plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        maven("https://repo.mewcraft.cc/releases")
        maven("https://repo.mewcraft.cc/private") {
            credentials {
                username = providers.gradleProperty("nyaadanbou.mavenUsername").getOrElse("")
                password = providers.gradleProperty("nyaadanbou.mavenPassword").getOrElse("")
            }
        }
    }
    versionCatalogs {
        create("local") {
            from(files("gradle/local.versions.toml"))
        }
    }
    versionCatalogs {
        create("libs") {
            from("cc.mewcraft.gradle:catalog:0.8-SNAPSHOT")
        }
    }
}

rootProject.name = "serverscripts"

include("paper-xcaps")
include("paper-xcity")
include("paper-xhome")
include("paper-xmine")
include("velocity-backend")
include("velocity-frontend")
