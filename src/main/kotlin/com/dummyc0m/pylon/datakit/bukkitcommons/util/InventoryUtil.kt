package com.dummyc0m.pylon.datakit.bukkitcommons.util

import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack

fun InventoryView.setItem(x: Int, y: Int, item: ItemStack?) {
    this.setItem(y * 9 + x, item)
}

fun InventoryView.getItem(x: Int, y: Int): ItemStack? {
    return this.getItem(y * 9 + x)
}