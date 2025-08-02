plugins {
    id("serverscripts-conventions.common")
}

// Expose version catalog
val local = the<org.gradle.accessors.dm.LibrariesForLocal>()

//repositories {
//    mavenCentral()
//    maven("https://repo.papermc.io/repository/maven-public/") {
//        name = "papermc-repo"
//    }
//}

dependencies {
    compileOnly(local.paper)
}

tasks {
    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}
