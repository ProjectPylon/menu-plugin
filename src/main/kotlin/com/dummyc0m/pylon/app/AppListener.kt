package com.dummyc0m.pylon.app

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*
import org.bukkit.inventory.ItemStack
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
            if (view.rerendering) {
                view.rerendering = false
            } else {
                view.menu.root.open.invoke()
            }
        }
    }

    @EventHandler
    fun onInventoryClose(inventoryCloseEvent: InventoryCloseEvent) {
        val view = inventoryCloseEvent.view
        // if it's rerendering, we do not invoke close
        if (view is MenuView && !view.rerendering) {
            val root = view.menu.root
            root.close.invoke()
            view.hasClosed = true
            // extended inventory, we do this hack to refresh the client-side inventory view
            if (view.menu.root.enableBottom) {
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
            if (view.menu.root.enableBottom) {
                inventoryClickEvent.isCancelled = true
                val itemElement = view.viewMeta.clickHandler[rawSlot]
                if (itemElement != null) {
                    itemElement.click.invoke(inventoryClickEvent.click, inventoryClickEvent.cursor)
                } else {
                    if (view.menu.root.clickDefault.invoke(inventoryClickEvent.click, inventoryClickEvent.cursor)) {
                        @Suppress("DEPRECATION")
                        inventoryClickEvent.cursor = ItemStack(Material.AIR)
                    }
                }
            } else {
                if (inventoryClickEvent.action === InventoryAction.MOVE_TO_OTHER_INVENTORY && rawSlot >= view.topInventory.size) {
                    inventoryClickEvent.isCancelled = true
                    if (root.transfer.invoke(item)) {
                        inventoryClickEvent.currentItem = null
                    }
                } else if (rawSlot < view.topInventory.size) {
                    inventoryClickEvent.isCancelled = true
                    val itemElement = view.viewMeta.clickHandler[rawSlot]
                    if (itemElement !== null) {
                        itemElement.click.invoke(inventoryClickEvent.click, inventoryClickEvent.cursor)
                    } else {
                        if (view.menu.root.clickDefault.invoke(inventoryClickEvent.click, inventoryClickEvent.cursor)) {
                            // TODO verify this works
                            @Suppress("DEPRECATION")
                            inventoryClickEvent.cursor = ItemStack(Material.AIR)
                        }
                    }
                } else if (inventoryClickEvent.action === InventoryAction.COLLECT_TO_CURSOR) {
                    inventoryClickEvent.isCancelled = true
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