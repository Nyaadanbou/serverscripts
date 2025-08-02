plugins {
    id("serverscripts-conventions.paper")
    id("nyaadanbou-conventions.copy-jar")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

repositories {
    maven { url = uri("https://repo.codemc.io/repository/maven-releases/") }
    maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") }
    maven { url = uri("https://maven.enginehub.org/repo/") }
}

dependencies {
    implementation(platform("com.intellectualsites.bom:bom-newest:1.47"))
    compileOnly("com.intellectualsites.plotsquared:plotsquared-core")
    compileOnly("com.intellectualsites.plotsquared:plotsquared-bukkit") { isTransitive = false }
    compileOnly(local.worldedit)
}

tasks {
    runServer {
        minecraftVersion("1.21.7")
    }
    shadowJar {
        relocate("org.spongepowered.configurate", "cc.mewcraft.transferer.config")
        relocate("io.leangen.geantyref", "cc.mewcraft.transferer.geantyref")
        relocate("org.incendo.cloud", "cc.mewcraft.transferer.cloud")
    }
    copyJar {
        environment = "paper"
        jarFileName = "serverscripts-xcaps-${project.version}.jar"
    }
}
