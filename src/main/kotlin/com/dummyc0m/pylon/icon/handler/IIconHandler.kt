package com.dummyc0m.pylon.icon.handler

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*

/**
 * Created by Dummy on 7/8/16.
 */
interface IIconHandler {
    fun onInteract(event: PlayerInteractEvent)


    fun onInteractEntity(event: PlayerInteractAtEntityEvent)


    fun onPickUp(event: PlayerPickupItemEvent)


    fun onDrop(event: PlayerDropItemEvent)


    fun onSelect(event: PlayerItemHeldEvent)


    fun onDeselect(event: PlayerItemHeldEvent)


    fun onConsume(event: PlayerItemConsumeEvent)


    fun onBreak(event: PlayerItemBreakEvent)


    fun onInventoryClick(event: InventoryClickEvent)
}