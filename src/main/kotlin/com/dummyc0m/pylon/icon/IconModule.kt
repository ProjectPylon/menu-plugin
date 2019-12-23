package com.dummyc0m.pylon.icon

import com.dummyc0m.pylon.icon.handler.IIconHandler
import com.dummyc0m.pylon.icon.handler.IconHandler
import com.dummyc0m.pylon.util.DARK_GRAY
import com.dummyc0m.pylon.util.ItemStackBuilder
import com.dummyc0m.pylon.util.RESET
import com.dummyc0m.pylon.util.YELLOW
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dummy on 7/7/16.
 */
class IconModule(plugin: JavaPlugin) {
    private val identifierMap: MutableMap<String, IIconHandler> = ConcurrentHashMap()
    val prefixLength = idPrefix.length
    val moduleName: String

    init {
        plugin.server.pluginManager.registerEvents(IconListener(this), plugin)
        moduleName = plugin.name
    }

    fun newIcon(unboundedId: String,
                itemStackBuilder: ItemStackBuilder
    ): Icon {
        return Icon("$moduleName:$unboundedId", itemStackBuilder)
    }

    internal fun getIconWithPrefix(identifier: String): IIconHandler? {
        return if (identifier.startsWith(idPrefix))
            identifierMap[identifier.substring(prefixLength)]
        else
            null
    }

    fun registerIcon(boundedId: String, iconHandler: IIconHandler) {
        if (identifierMap.containsKey(boundedId)) {
            throw IconException("Duplicate Icon Id $boundedId")
        } else {
            identifierMap.put(boundedId, iconHandler)
        }
    }

    companion object {
        val idPrefix = RESET + DARK_GRAY
        val modulePrefix: String = RESET + YELLOW
    }
}