plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        maven("https://repo.mewcraft.cc/releases") {
            name = "nyaadanbouReleases"
        }
        maven {
            url = uri("https://repo.mewcraft.cc/private")
            name = "nyaadanbouPrivate"
            credentials(PasswordCredentials::class)
        }
    }
    versionCatalogs {
        create("local") {
            from(files("gradle/local.versions.toml"))
        }
    }
    versionCatalogs {
        create("libs") {
            from("cc.mewcraft.gradle:catalog:0.11-SNAPSHOT")
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
