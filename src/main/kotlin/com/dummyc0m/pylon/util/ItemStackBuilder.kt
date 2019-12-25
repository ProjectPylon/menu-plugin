package com.dummyc0m.pylon.util

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * Created by Dummy on 7/18/16.
 */
class ItemStackBuilder {
    var material: Material = Material.AIR
    var amount: Int = 1
    var damage: Short = 0
    var unbreakable: Boolean = false

    var displayName: String? = null
    var lore: MutableList<String> = ArrayList()
    var enchants: MutableList<Pair<Enchantment, Int>> = ArrayList()
    var flags: MutableList<ItemFlag> = ArrayList()

    fun lore(line: String) {
        lore.add(line)
    }

    fun enchant(enchantment: Enchantment, level: Int) {
        enchants.add(Pair(enchantment, level))
    }

    fun flag(itemFlag: ItemFlag) {
        flags.add(itemFlag)
    }

    fun clearLore() {
        lore = ArrayList()
    }

    fun clearEnchants() {
        enchants = ArrayList()
    }

    fun clearFlags() {
        flags = ArrayList()
    }

    fun render(): ItemStack {
        val item = ItemStack(material, amount, damage)
        val itemMeta = item.itemMeta
        itemMeta.displayName = displayName
        itemMeta.lore = lore
        itemMeta.spigot().isUnbreakable = unbreakable
        enchants.forEach { pair -> itemMeta.addEnchant(pair.first, pair.second, true) }
        flags.forEach { flag -> itemMeta.addItemFlags(flag) }
        item.itemMeta = itemMeta
        return item
    }

    fun clone(): ItemStackBuilder {
        val cloned = ItemStackBuilder()
        cloned.material = material
        cloned.amount = amount
        cloned.damage = damage
        cloned.unbreakable = unbreakable
        cloned.displayName = displayName
        cloned.lore = lore.toMutableList()
        cloned.enchants = enchants.toMutableList()
        cloned.flags = flags.toMutableList()
        return cloned
    }
}

fun itemBuilder(init: ItemStackBuilder.() -> Unit): ItemStackBuilder {
    val itemBuilder = ItemStackBuilder()
    itemBuilder.init()
    return itemBuilder
}