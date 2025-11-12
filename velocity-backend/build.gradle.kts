plugins {
    id("serverscripts-conventions.velocity")
    id("nyaadanbou-conventions.copy-jar")

}

tasks {
    shadowJar {
        relocate("org.spongepowered.configurate", "cc.mewcraft.transferer.config")
        relocate("io.leangen.geantyref", "cc.mewcraft.transferer.geantyref")
        relocate("org.incendo.cloud", "cc.mewcraft.transferer.cloud")
    }
    copyJar {
        environment = "paper"
        jarFileName = "script-backend-${project.version}.jar"
    }
}
