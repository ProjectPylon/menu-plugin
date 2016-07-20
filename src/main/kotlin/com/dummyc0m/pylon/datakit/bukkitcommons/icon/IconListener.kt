package com.dummyc0m.pylon.datakit.bukkitcommons.icon

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*

/**
 * Created by Dummy on 7/7/16.
 */
class IconListener(private val iconModule: IconModule) : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.hasItem() && event.item.hasItemMeta()) {
            val itemName = event.item.itemMeta.lore?.last()
            if (itemName !== null) {
                iconModule.getIconWithPrefix(itemName)?.handler?.onInteract(event)
            }
        }
    }

    @EventHandler
    fun onPlayerInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        val item = event.player.itemOnCursor
        if (item != null && item.hasItemMeta()) {
            val itemName = item.itemMeta.lore?.last()
            if (itemName !== null) {
                iconModule.getIconWithPrefix(itemName)?.handler?.onInteractEntity(event)
            }
        }
    }

    @EventHandler
    fun onPlayerPickup(event: PlayerPickupItemEvent) {
        val item = event.item.itemStack
        if (item != null && item.hasItemMeta()) {
            val itemName = item.itemMeta.lore?.last()
            if (itemName !== null) {
                iconModule.getIconWithPrefix(itemName)?.handler?.onPickUp(event)
            }
        }
    }

    @EventHandler
    fun onPlayerDrop(event: PlayerDropItemEvent) {
        val item = event.itemDrop.itemStack
        if (item != null && item.hasItemMeta()) {
            val itemName = item.itemMeta.lore?.last()
            if (itemName !== null) {
                iconModule.getIconWithPrefix(itemName)?.handler?.onDrop(event)
            }
        }
    }

    @EventHandler
    fun OnHeld(event: PlayerItemHeldEvent) {
        val prevItem = event.player.inventory.getItem(event.previousSlot)
        val currItem = event.player.inventory.getItem(event.newSlot)
        if (prevItem != null && prevItem.hasItemMeta()) {
            val prevName = prevItem.itemMeta.lore?.last()
            if (prevName !== null) {
                iconModule.getIconWithPrefix(prevName)?.handler?.onSelect(event)
            }
        }
        if (currItem != null && currItem.hasItemMeta()) {
            val currName = currItem.itemMeta.lore?.last()
            if (currName !== null) {
                iconModule.getIconWithPrefix(currName)?.handler?.onSelect(event)
            }
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val item = event.currentItem
        if (item != null && item.hasItemMeta()) {
            val itemName = item.itemMeta.lore?.last()
            if (itemName !== null) {
                iconModule.getIconWithPrefix(itemName)?.handler?.onInventoryClick(event)
            }
        }
        if (event.click == ClickType.NUMBER_KEY) {
            val prevItem = event.whoClicked.inventory.getItem(event.hotbarButton)
            if (prevItem != null && prevItem.hasItemMeta()) {
                val hotBarItem = prevItem.itemMeta.lore?.last()
                if (hotBarItem !== null) {
                    iconModule.getIconWithPrefix(hotBarItem)?.handler?.onInventoryClick(event)
                }
            }
        }
    }
}