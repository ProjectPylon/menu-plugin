package com.dummyc0m.pylon.datakit.bukkitcommons.app

import com.dummyc0m.pylon.datakit.bukkitcommons.app.view.ViewMeta
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView

/**
 * Created by Dummy on 7/13/16.
 */
class MenuView(player: HumanEntity,
               top: Inventory,
               bottom: Inventory,
               val enableBottom: Boolean,
               val menu: Menu,
               val viewMeta: ViewMeta) : InventoryView() {
    private val _player: HumanEntity
    private val _top: Inventory
    private val _bottom: Inventory
    internal var hasClosed = false

    init {
        _player = player
        _top = top
        _bottom = bottom
    }

    override fun getPlayer(): HumanEntity {
        return _player
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
}