package com.dummyc0m.pylon.conversation

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dummy on 7/14/16.
 */
class ChatTriggerModule(plugin: JavaPlugin) {
    private val triggerMap: MutableMap<UUID, ChatResult> = ConcurrentHashMap()

    init {
        plugin.server.pluginManager.registerEvents(ChatListener(this), plugin)
    }

    fun onChat(uuid: UUID): ChatResult {
        val new = ChatResult(null)
        triggerMap.put(uuid, new)
        return new
    }

    internal fun trigger(event: AsyncPlayerChatEvent) {
        val new = triggerMap.remove(event.player.uniqueId)?.invoke(event)
        if (new != null) {
            triggerMap.put(event.player.uniqueId, new)
        }
    }

    internal fun delete(uuid: UUID) {
        triggerMap.remove(uuid)
    }

    class ChatResult internal constructor(private var prev: ChatResult?) {
        private var next: ChatResult? = null
        private var consumer: ((event: AsyncPlayerChatEvent) -> Boolean)? = null

        internal fun invoke(event: AsyncPlayerChatEvent): ChatResult? {
            val consume = consumer
            if (consume != null && consume.invoke(event)) {
                return next
            }
            return null
        }

        /**
         * return true to continue, false to drop
         */
        fun then(handler: (event: AsyncPlayerChatEvent) -> Boolean): ChatResult {
            prev?.next = this
            prev = null
            consumer = handler
            return this
        }

        fun next(): ChatResult {
            return ChatResult(this)
        }
    }

    class ChatListener(val chatTriggerModule: ChatTriggerModule) : Listener {
        @EventHandler
        fun onPlayerChat(event: AsyncPlayerChatEvent) {
            chatTriggerModule.trigger(event)
        }

        @EventHandler
        fun onPlayerLeave(event: PlayerQuitEvent) {
            chatTriggerModule.delete(event.player.uniqueId)
        }
    }
}