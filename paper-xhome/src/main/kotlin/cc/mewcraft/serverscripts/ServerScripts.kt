package cc.mewcraft.serverscripts

import org.bukkit.plugin.java.JavaPlugin

class ServerScripts : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic
        logger.info("XHome plugin enabled!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("XHome plugin disabled!")
    }
}
