plugins {
    id("serverscripts-conventions.paper")
    id("nyaadanbou-conventions.copy-jar")
    id("xyz.jpenilla.run-paper") version "2.3.1"
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
        jarFileName = "script-home-${project.version}.jar"
    }
}
