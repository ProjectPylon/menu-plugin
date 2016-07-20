package com.dummyc0m.pylon.datakit.bukkitcommons.icon

import com.dummyc0m.pylon.datakit.bukkitcommons.icon.handler.IIconHandler
import com.dummyc0m.pylon.datakit.bukkitcommons.icon.handler.IconHandler
import com.dummyc0m.pylon.datakit.bukkitcommons.util.DynamicItemBuilder
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Created by Dummy on 7/5/16.
 */
data class Icon internal constructor(internal val id: String,
                                     internal val module: IconModule,
                                     internal var handler: IIconHandler) {

    private val itemBuilder = DynamicItemBuilder()

    fun item(init: DynamicItemBuilder.() -> Unit): Icon {
        itemBuilder.init()
        itemBuilder.lore(module.idPrefix + id)
        return this
    }

    fun getItem(player: Player): ItemStack {
        return itemBuilder.render(player)
    }

    fun handler(handler: IIconHandler): Icon {
        this.handler = handler
        return this
    }

    fun register(): Icon {
        module.registerIcon(this)
        return this
    }

    companion object {
        val defaultHandler = IconHandler()
    }
}