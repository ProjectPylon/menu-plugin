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

    var displayName = "Item"
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
        enchants.forEach { pair -> itemMeta.addEnchant(pair.first, pair.second, true) }
        flags.forEach { flag -> itemMeta.addItemFlags(flag) }
        item.itemMeta = itemMeta
        return item
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemStackBuilder

        if (material != other.material) return false
        if (amount != other.amount) return false
        if (damage != other.damage) return false
        if (displayName != other.displayName) return false
        if (lore != other.lore) return false
        if (enchants != other.enchants) return false
        if (flags != other.flags) return false

        return true
    }

    override fun hashCode(): Int {
        var result = material.hashCode()
        result = 31 * result + amount
        result = 31 * result + damage
        result = 31 * result + displayName.hashCode()
        result = 31 * result + lore.hashCode()
        result = 31 * result + enchants.hashCode()
        result = 31 * result + flags.hashCode()
        return result
    }
}

fun itemBuilder(init: ItemStackBuilder.() -> Unit): ItemStackBuilder {
    val itemBuilder = ItemStackBuilder()
    itemBuilder.init()
    return itemBuilder
}