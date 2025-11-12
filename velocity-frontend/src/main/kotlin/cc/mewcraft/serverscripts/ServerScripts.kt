package cc.mewcraft.serverscripts

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger

@Plugin(
    id = "script-frontend",
    name = "Script-Frontend",
    version = "1.0.0-SNAPSHOT",
    description = "Scripts for frontend proxy",
    authors = ["Nailm"]
)
class ServerScripts @Inject constructor(
    private val server: ProxyServer,
    private val logger: Logger
) {

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        logger.info("ServerScripts-Frontend plugin enabled!")
    }
}
