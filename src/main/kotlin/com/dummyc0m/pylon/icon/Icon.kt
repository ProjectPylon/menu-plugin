package com.dummyc0m.pylon.icon

import com.dummyc0m.pylon.icon.IconModule.Companion.idPrefix
import com.dummyc0m.pylon.icon.handler.IconHandler
import com.dummyc0m.pylon.util.ItemStackBuilder
import org.bukkit.inventory.ItemStack

/**
 * Created by Dummy on 7/5/16.
 */
data class Icon internal constructor(val id: String,
                                     private val itemStackBuilder: ItemStackBuilder) {

    init {
        itemStackBuilder.lore(idPrefix + id)
    }

    fun getItem(): ItemStack {
        return itemStackBuilder.render()
    }

    companion object {
        val defaultHandler = IconHandler()
    }
}