package com.dummyc0m.pylon.datakit.bukkitcommons.econ

import com.dummyc0m.pylon.datakit.bukkit.DataKitBukkit
import com.dummyc0m.pylon.datakit.bukkit.DataReceiveEvent
import com.dummyc0m.pylon.datakit.extlib.com.fasterxml.jackson.databind.node.IntNode
import com.dummyc0m.pylon.datakit.extlib.com.fasterxml.jackson.databind.node.ObjectNode
import com.dummyc0m.pylon.datakit.network.message.DeltaMessage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * minigames should not use this module!
 * Created by Dummy on 7/16/16.
 */
class EconModule(plugin: JavaPlugin) {
    private val econMap: MutableMap<UUID, EconData> = ConcurrentHashMap()

    init {
        plugin.server.pluginManager.registerEvents(EconListener(this, DataKitBukkit.instance), plugin)
    }

    internal fun addPlayerData(econData: EconData) {
        econMap.put(econData.uuid, econData)
    }

    fun getPlayerData(uuid: UUID): EconData? {
        return econMap.get(uuid)
    }

    /**
     * Unload without dereference! Please handle it on your own
     */
    internal fun unloadPlayerData(uuid: UUID) {
        econMap.remove(uuid)
    }

    class EconListener(private val econModule: EconModule, private val dataKit: DataKitBukkit) : Listener {
        @EventHandler
        fun onDataReceive(dataReceiveEvent: DataReceiveEvent) {
            val pluginRootNode = dataReceiveEvent.jsonData.get("commons") as ObjectNode?
            if (pluginRootNode === null) {
                econModule.addPlayerData(EconData(dataKit, dataReceiveEvent.player.uniqueId))
            } else {
                val econNode = pluginRootNode.get("econ")
                if (econNode === null) {
                    econModule.addPlayerData(EconData(dataKit, dataReceiveEvent.player.uniqueId))
                } else {
                    econModule.addPlayerData(EconData(dataKit, dataReceiveEvent.player.uniqueId, AtomicInteger(econNode.get("money").asInt())))
                }
            }
        }

        @EventHandler
        fun onPlayerDisconnect(playerQuitEvent: PlayerQuitEvent) {
            econModule.unloadPlayerData(playerQuitEvent.player.uniqueId)
        }
    }

    data class EconData(val dataKit: DataKitBukkit, val uuid: UUID, val money: AtomicInteger = AtomicInteger(0)) {
        fun change(copper: Int) {
            money.addAndGet(copper)
            Bukkit.getScheduler().runTaskAsynchronously(dataKit) {
                val message = DeltaMessage()
                message.offlineUUID = uuid
                message.setDelta("commons.econ.money", IntNode(copper))
                dataKit.send(message)
            }
        }
    }
}