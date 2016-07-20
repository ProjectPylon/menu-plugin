package com.dummyc0m.pylon.datakit.bukkitcommons.icon

import com.dummyc0m.pylon.datakit.bukkitcommons.icon.handler.IIconHandler
import com.dummyc0m.pylon.datakit.bukkitcommons.util.DARK_GRAY
import com.dummyc0m.pylon.datakit.bukkitcommons.util.RESET
import com.dummyc0m.pylon.datakit.bukkitcommons.util.YELLOW
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dummy on 7/7/16.
 */
class IconModule(plugin: JavaPlugin) {
    private val identifierMap: MutableMap<String, Icon> = ConcurrentHashMap()
    val idPrefix = RESET + DARK_GRAY
    val prefixLength = idPrefix.length
    val modulePrefix: String = RESET + YELLOW
    val moduleName: String

    init {
        plugin.server.pluginManager.registerEvents(IconListener(this), plugin)
        moduleName = plugin.name
    }

    fun newIcon(unboundedId: String,
                handler: IIconHandler = Icon.defaultHandler): Icon {
        return Icon("$moduleName:$unboundedId", this, handler)
    }

    internal fun getIconWithPrefix(identifier: String): Icon? {
        return if (identifier.startsWith(idPrefix))
            identifierMap[identifier.substring(prefixLength)]
        else
            null
    }

    fun getIcon(identifier: String): Icon? {
        return identifierMap[identifier]
    }

    internal fun registerIcon(icon: Icon) {
        if (identifierMap.containsKey(icon.id)) {
            throw IconException("Duplicate Icon Id ${icon.id}")
        } else {
            identifierMap.put(icon.id, icon)
        }
    }

}