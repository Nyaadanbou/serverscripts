package cc.mewcraft.serverscripts

import com.google.inject.Inject
import com.velocitypowered.api.event.Continuation
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.slf4j.Logger

@Plugin(
    id = "script-backend",
    name = "Script-Backend",
    version = "1.0.0-SNAPSHOT",
    description = "Scripts for backend proxy",
    authors = ["Nailm"],
    dependencies = [
        Dependency("luckperms", true),
    ]
)
class ServerScripts @Inject constructor(
    private val server: ProxyServer,
    private val logger: Logger,
) {

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        logger.info("ServerScripts-Backend plugin enabled!")
    }

    @Subscribe(order = PostOrder.LATE)
    fun onServerPreConnect(
        event: ServerPreConnectEvent,
        continuation: Continuation, // 考虑到查询权限应该需要一点时间, 采用异步处理
    ) {
        val player = event.player

        //
        // 玩家连接到服务器之前会检查其是否有相应的权限; 如果没有, 则禁止连接操作
        //

        val hasBypass = player.hasPermission("script.connect.bypass")

        var fromDenied = false
        val fromServer = event.previousServer
        if (fromServer != null) {
            val fromServerInfo = fromServer.serverInfo
            val fromServerName = fromServerInfo.name
            if (!player.hasPermission("script.connect.from.${fromServerName.lowercase()}") && !hasBypass) {
                fromDenied = true
                logger.info("Denied for player ${player.username} (${player.uniqueId}) from server ${fromServerInfo.name} ")
            }
        }

        var toDenied = false
        val toServer = event.result.server
        if (toServer != null) {
            val toServerInfo = toServer.get().serverInfo
            val toServerName = toServerInfo.name
            if (!player.hasPermission("script.connect.to.${toServerName.lowercase()}") && !hasBypass) {
                toDenied = true
                logger.info("Denied for player ${player.username} (${player.uniqueId}) to server ${toServerInfo.name} ")
            }
        }

        if (fromDenied || toDenied) {
            event.result = ServerPreConnectEvent.ServerResult.denied()
            logger.info("Denied connection of player ${player.username} (${player.uniqueId}) to server ${toServer?.get()?.serverInfo?.name} from server ${fromServer?.serverInfo?.name}")

            val prompt = Component.text("一股神秘的力量阻止了你前往目标位面.").color(NamedTextColor.RED)

            if (fromServer == null) {
                // 玩家刚连到 Velocity 时, fromServer 是 null
                // 这种情况下如果只设置 event.result 但不主动断开连接的话, 玩家会一直卡在 "加入世界中..." 直到超时掉线
                // 所以我们主动断开一下连接, 给出更加直接的提示信息
                player.disconnect(prompt)
            } else {
                // 其余情况: 玩家已经在 Velocity 内的某个服务器上, 这时只需要发送聊天框消息即可
                // Velocity 并不会自己发送消息告诉玩家无法连接到目标位面, 需要我们自己发送消息给玩家
                player.sendMessage(prompt)
            }
        }

        continuation.resume()
        logger.info("ServerPreConnectEvent was continued")
    }
}
