import org.gradle.kotlin.dsl.the

plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
    id("nyaadanbou-conventions.repositories")
}

group = "cc.mewcraft"
version = "1.0.0-SNAPSHOT"

// Expose version catalog
val local = the<org.gradle.accessors.dm.LibrariesForLocal>()

tasks {
    compileKotlin {
        compilerOptions {
            // we rely on IDE analysis
            suppressWarnings.set(true)
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveClassifier.set("shaded")
        dependencies {
            exclude("META-INF/maven/**")
            exclude("META-INF/LICENSE*")
            exclude("META-INF/NOTICE*")
        }
    }
    test {
        useJUnitPlatform()
    }
}

java {
    withSourcesJar()
}

kotlin {
    jvmToolchain(21)

    sourceSets {
        val main by getting {
            dependencies {
                compileOnly(kotlin("stdlib"))
                compileOnly(kotlin("reflect"))
                compileOnly(local.kotlinx.atomicfu)
                compileOnly(local.kotlinx.coroutines.core)
            }
        }
        val test by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))
                implementation(local.kotlinx.atomicfu)
                implementation(local.kotlinx.coroutines.core)
            }
        }
    }
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src/main/kotlin/"))
    }
}