plugins {
    id("serverscripts-conventions.common")
    kotlin("kapt")
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
    compileOnly(local.velocity); kapt(local.velocity)
}
