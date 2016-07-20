package com.dummyc0m.pylon.datakit.bukkitcommons.npc

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

/**
 * Created by Dummy on 7/5/16.
 */
class NpcModule(private val plugin: JavaPlugin) {
    private val handlerMap: MutableMap<UUID, NpcHandler> = HashMap()

    init {
        plugin.server.pluginManager.registerEvents(NpcListener(this), plugin)
    }

    fun addHandler(npcUUID: UUID, handler: NpcHandler) {
        handlerMap.put(npcUUID, handler)
    }

    internal fun handle(event: PlayerInteractEntityEvent) {
        val handler = handlerMap.get(event.rightClicked.uniqueId)
        if (handler !== null) {
            handler.onRightClick(event.player)
            event.isCancelled = true
        }
    }

    class NpcListener(val npcModule: NpcModule) : Listener {
        @EventHandler
        fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
            npcModule.handle(event)
        }
    }
}