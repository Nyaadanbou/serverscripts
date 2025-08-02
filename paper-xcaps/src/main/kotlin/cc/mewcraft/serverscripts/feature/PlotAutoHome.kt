package cc.mewcraft.serverscripts.feature

import cc.mewcraft.serverscripts.PLOT_WORLD_A_NAME
import com.plotsquared.bukkit.util.BukkitUtil
import com.plotsquared.core.PlotAPI
import com.plotsquared.core.PlotSquared
import com.plotsquared.core.player.PlotPlayer
import com.plotsquared.core.plot.PlotArea
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import java.util.concurrent.ConcurrentHashMap

/**
 * 当玩家进入地皮世界服务器后:
 * - 如果玩家在地皮世界已经有地皮, 则自动将他传送到他的地皮.
 * - 如果玩家在地皮世界还没有地皮, 则先为他领取一个地皮, 然后再将他传送到地皮上.
 */
class PlotAutoHome(
    private val plugin: JavaPlugin,
    private val logger: Logger,
) : Listener {

    // 用于追踪正在领取地皮的玩家, 避免重复操作
    private val claimingPlayers = ConcurrentHashMap.newKeySet<String>()
    private val plotApi = PlotAPI()

    @EventHandler
    fun on(event: PlayerJoinEvent) {
        val player = event.player
        handlePlayerJoin(player)
    }

    private fun handlePlayerJoin(player: Player) {
        // 获取既定的 PlotArea
        val plotArea = PlotSquared.get().plotAreaManager.getPlotAreasSet(PLOT_WORLD_A_NAME).firstOrNull() ?: return
        // 获取玩家的 PlotPlayer 实例
        val plotPlayer = BukkitUtil.adapt(player)
        // 检查玩家在该区域是否已有地皮
        val ownedPlots = plotArea.getPlots(plotPlayer.uuid)
        if (ownedPlots.isNotEmpty()) {
            // 玩家已有地皮, 传送到第一个地皮的 home
            ownedPlots.first().teleportPlayer(plotPlayer) { result -> }
        } else {
            // 玩家没有地皮, 自动领取一个
            autoClaimPlot(player, plotArea, plotPlayer)
        }
    }

    private fun autoClaimPlot(player: Player, plotArea: PlotArea, plotPlayer: PlotPlayer<*>) {
        val playerName = player.name

        // 防止重复领取
        if (!claimingPlayers.add(playerName)) {
            return
        }

        // 异步执行地皮领取操作
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try {
                // 获取下一个可用的地皮
                val nextPlot = plotArea.getNextFreePlot(plotPlayer, null)
                if (nextPlot != null) {
                    // 在主线程中执行领取操作
                    Bukkit.getScheduler().runTask(plugin, Runnable {
                        try {
                            val success = nextPlot.claim(plotPlayer, true, null, true, false)
                            if (!success) {
                                player.sendMessage(text("Failed to claim a room for you.").color(NamedTextColor.RED))
                                teleportToSpawn(player)
                            }
                        } catch (e: Exception) {
                            logger.warn("An error occurred while claiming a plot for player ${player.name}: ${e.message}")
                            player.sendMessage(text("Failed to claim a room for you.").color(NamedTextColor.RED))
                            teleportToSpawn(player)
                        } finally {
                            claimingPlayers.remove(playerName)
                        }
                    })
                } else {
                    // 没有可用地皮
                    Bukkit.getScheduler().runTask(plugin, Runnable {
                        player.sendMessage(text("No available room found.").color(NamedTextColor.RED))
                        teleportToSpawn(player)
                        claimingPlayers.remove(playerName)
                    })
                }
            } catch (e: Exception) {
                logger.warn("An error occurred while searching for the next free plot: ${e.message}")
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    player.sendMessage(text("Failed to claim a room for you.").color(NamedTextColor.RED))
                    teleportToSpawn(player)
                    claimingPlayers.remove(playerName)
                })
            }
        })
    }

    private fun teleportToSpawn(player: Player) {
        val spawnLoc = Bukkit.getWorlds().first().spawnLocation
        player.teleportAsync(spawnLoc).thenAccept { success ->
            if (success) {
                player.sendMessage("Back to spawn.")
            } else {
                logger.warn("Failed to teleport player ${player.name} to spawn.")
            }
        }
    }

}