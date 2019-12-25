package com.dummyc0m.pylon.util

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack

fun InventoryView.setItem(x: Int, y: Int, item: ItemStack?) {
    this.setItem(y * 9 + x, item)
}

fun InventoryView.getItem(x: Int, y: Int): ItemStack? {
    return this.getItem(y * 9 + x)
}

fun Inventory.replaceWith(other: Inventory) {
    for (i in 0 until size) {
        val existingItem = getItem(i)
        val newItem = other.getItem(i)
        if (existingItem != newItem) {
            setItem(i, newItem)
        }
    }
}