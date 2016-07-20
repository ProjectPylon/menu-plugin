package com.dummyc0m.pylon.datakit.bukkitcommons.app

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.plugin.java.JavaPlugin

/**
 * internal listener!
 * Created by Dummy on 7/13/16.
 */
internal class AppListener(val plugin: JavaPlugin) : Listener {
    @EventHandler
    fun onInventoryOpen(inventoryOpenEvent: InventoryOpenEvent) {
        val view = inventoryOpenEvent.view
        if (view is MenuView) {
            val root = view.menu.root
            root.open.invoke(view)
        }
    }

    @EventHandler
    fun onInventoryClose(inventoryCloseEvent: InventoryCloseEvent) {
        val view = inventoryCloseEvent.view
        if (view is MenuView) {
            val root = view.menu.root
            root.close.invoke(view)
            view.hasClosed = true
            if (view.enableBottom) {
                plugin.server.scheduler.runTask(plugin) {
                    val player = inventoryCloseEvent.player
                    player.openInventory(player.inventory)
                    player.closeInventory()
                }
            }
        }
    }

    @EventHandler
    fun onInventoryClick(inventoryClickEvent: InventoryClickEvent) {
        val view = inventoryClickEvent.view
        if (view is MenuView) {
            val item = inventoryClickEvent.currentItem
            val root = view.menu.root
            val rawSlot = inventoryClickEvent.rawSlot
            val blankElement = view.viewMeta.blankHandler.get(rawSlot)
            if (blankElement !== null) {
                if (blankElement.click.invoke(view, inventoryClickEvent.click, inventoryClickEvent.cursor)) {
                    inventoryClickEvent.isCancelled = true
                }
            } else {
                if (view.enableBottom) {
                    inventoryClickEvent.isCancelled = true
                    val itemElement = view.viewMeta.clickHandler.get(rawSlot)
                    if (itemElement != null) {
                        itemElement.click.invoke(view, inventoryClickEvent.click, inventoryClickEvent.cursor)
                    }
                } else {
                    if (inventoryClickEvent.action === InventoryAction.MOVE_TO_OTHER_INVENTORY && rawSlot >= view.topInventory.size) {
                        inventoryClickEvent.isCancelled = true
                        if (root.transfer.invoke(view, item)) {
                            inventoryClickEvent.currentItem = null
                        }
                    } else if (rawSlot < view.topInventory.size) {
                        inventoryClickEvent.isCancelled = true
                        val itemElement = view.viewMeta.clickHandler.get(rawSlot)
                        if (itemElement !== null) {
                            itemElement.click.invoke(view, inventoryClickEvent.click, inventoryClickEvent.cursor)
                        }
                    } else if (inventoryClickEvent.action === InventoryAction.COLLECT_TO_CURSOR) {
                        inventoryClickEvent.isCancelled = true
                    }
                }
            }
        }
    }

    @EventHandler
    fun onInventoryDrag(inventoryDragEvent: InventoryDragEvent) {
        val view = inventoryDragEvent.view
        if (view is MenuView) {
            inventoryDragEvent.isCancelled = true
        }
    }
}