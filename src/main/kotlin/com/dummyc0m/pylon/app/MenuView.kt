package com.dummyc0m.pylon.app

import com.dummyc0m.pylon.app.view.ViewMeta
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView

/**
 * Created by Dummy on 7/13/16.
 */
class MenuView internal constructor(
        top: Inventory,
        bottom: Inventory,
        internal var menu: Menu,
        internal var viewMeta: ViewMeta,
        internal var rerendering: Boolean) : InventoryView() {
    private val _top: Inventory
    private val _bottom: Inventory
    internal var hasClosed = false

    init {
        _top = top
        _bottom = bottom
    }

    override fun getPlayer(): HumanEntity {
        return menu.humanEntity
    }

    override fun getType(): InventoryType {
        return InventoryType.CHEST
    }

    override fun getBottomInventory(): Inventory {
        return _bottom
    }

    override fun getTopInventory(): Inventory {
        return _top
    }

    internal fun animate() {
        if (viewMeta.animationHandler.isNotEmpty()) {
            com.dummyc0m.pylon.app.Menu.Animator(this).runTaskTimer(menu.plugin, 2L, 4L)
        }
    }
}