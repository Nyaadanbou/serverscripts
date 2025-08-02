package cc.mewcraft.serverscripts

import cc.mewcraft.serverscripts.feature.PlotAutoHome
import org.bukkit.plugin.java.JavaPlugin

class ServerScripts : JavaPlugin() {

    override fun onEnable() {
        if (!server.pluginManager.isPluginEnabled("PlotSquared")) {
            logger.severe("PlotSquared not found, ServerScripts will be disabled!")
            server.pluginManager.disablePlugin(this)
            return
        }

        registerListeners()
    }

    override fun onDisable() {
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(PlotAutoHome(this, slF4JLogger), this)
    }
}
