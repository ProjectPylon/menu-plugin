package com.dummyc0m.pylon.datakit.bukkitcommons.util

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

/**
 * Created by Dummy on 7/18/16.
 */
class DynamicItemBuilder {
    private var _render: (itemMeta: ItemMeta, humanEntity: HumanEntity) -> Unit = renderConstant

    var material: Material = Material.AIR
    var amount: Int = 1
    var damage: Short = 0

    var displayName = "Item"
    var lore: MutableList<String> = ArrayList()
    var enchants: MutableList<Pair<Enchantment, Int>> = ArrayList()
    var flags: MutableList<ItemFlag> = ArrayList()

    fun onRender(render: (itemMeta: ItemMeta, humanEntity: HumanEntity) -> Unit) {
        _render = render
    }

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

    fun render(humanEntity: HumanEntity): ItemStack {
        val item = ItemStack(material, amount, damage)
        val itemMeta = item.itemMeta
        itemMeta.displayName = displayName
        itemMeta.lore = lore
        enchants.forEach { pair -> itemMeta.addEnchant(pair.first, pair.second, true) }
        flags.forEach { flag -> itemMeta.addItemFlags(flag) }
        if (_render !== renderConstant) {
            _render.invoke(itemMeta, humanEntity)
        }
        item.itemMeta = itemMeta
        return item
    }

    fun renderStatic(): ItemStack {
        val item = ItemStack(material, amount, damage)
        val itemMeta = item.itemMeta
        itemMeta.displayName = displayName
        itemMeta.lore = lore
        enchants.forEach { pair -> itemMeta.addEnchant(pair.first, pair.second, true) }
        flags.forEach { flag -> itemMeta.addItemFlags(flag) }
        item.itemMeta = itemMeta
        return item
    }

    companion object {
        private val renderConstant: (itemMeta: ItemMeta, humanEntity: HumanEntity) -> Unit = { itemMeta, humanEntity -> }
    }
}


fun item(init: DynamicItemBuilder.() -> Unit): DynamicItemBuilder {
    val itemBuilder = DynamicItemBuilder()
    itemBuilder.init()
    return itemBuilder
}