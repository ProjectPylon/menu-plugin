package com.dummyc0m.pylon.app.view

import com.dummyc0m.pylon.app.MenuView
import com.dummyc0m.pylon.util.ItemStackBuilder
import com.dummyc0m.pylon.util.setItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * Created by Dummy on 7/12/16.
 */

class ContainerElement(
        override val x: Int,
        override val y: Int
) : ViewElement {
    private val children: MutableList<ViewElement> = ArrayList()

    override fun render(menuView: MenuView, offsetX: Int, offsetY: Int) {
        for (c in children) {
            c.render(menuView, offsetX + x, offsetY + y)
        }
    }

    fun <T: ViewElement> h(viewElement: T, init: T.() -> Unit = {}) {
        viewElement.init()
        children.add(viewElement)
    }

    fun c(x: Int, y: Int, init: ContainerElement.() -> Unit) =
            h(ContainerElement(x, y), init)

    fun i(x: Int, y: Int, init: ItemElement.() -> Unit) =
            h(ItemElement(x, y), init)
}

class RootElement(
        private val viewElement: ViewElement
) {

    fun render(menuView: MenuView, offsetX: Int, offsetY: Int) {
        viewElement.render(menuView, offsetX, offsetY)
    }

    var title: String = "Container"
    var topSize: Int = 6
    var enableBottom: Boolean = false
    val open: () -> Unit
        get() = _open
    private var _open: () -> Unit = ocConstant

    val close: () -> Unit
        get() = _close
    private var _close: () -> Unit = ocConstant

    val clickDefault: (type: ClickType, cursor: ItemStack) -> Boolean
        get() = _clickDefault
    private var _clickDefault: (type: ClickType, cursor: ItemStack) -> Boolean = clickDefaultConstant

    /**
     * only when bottom inventory is humanEntity inventory and transferring up
     * true if consumed item
     *
     * default: false
     */
    val transfer: (item: ItemStack) -> Boolean
        get() = _transfer
    private var _transfer: (item: ItemStack) -> Boolean = transferConstant

    fun onOpen(open: () -> Unit) {
        _open = open
    }

    fun onClose(close: () -> Unit) {
        _close = close
    }

    /**
     * when there are no click handlers to invoke
     * true if consumed item
     *
     * default: false
     */
    fun onClickDefault(click: (type: ClickType, cursor: ItemStack) -> Boolean) {
        _clickDefault = click
    }

    fun onTransfer(transfer: (item: ItemStack) -> Boolean) {
        _transfer = transfer
    }

    companion object {
        private val transferConstant: (item: ItemStack) -> Boolean = { _ -> false }
        private val clickDefaultConstant: (type: ClickType, cursor: ItemStack) -> Boolean = { _, _ -> false }
        private val ocConstant: () -> Unit = {}
    }
}

/**
 * denies all clicks
 */
class ItemElement(
        override val x: Int,
        override val y: Int
) : ViewElement {

    val click: (type: ClickType, cursor: ItemStack) -> Unit
        get() = _click
    private var _click: (type: ClickType, cursor: ItemStack) -> Unit = clickConstant

    internal var itemBuilder = ItemStackBuilder()

    var item: ItemStackBuilder
        get() = itemBuilder
        set(value) {
            itemBuilder = value
        }

    var material: Material
        get() = itemBuilder.material
        set(value) {
            itemBuilder.material = value
        }
    var amount: Int
        get() = itemBuilder.amount
        set(value) {
            itemBuilder.amount = value
        }
    var damage: Short
        get() = itemBuilder.damage
        set(value) {
            itemBuilder.damage = value
        }

    var displayName: String?
        get() = itemBuilder.displayName
        set(value) {
            itemBuilder.displayName = value
        }

    val tick: (frame: Int) -> ItemStackBuilder
        get() = _tick
    private var _tick: (frame: Int) -> ItemStackBuilder = tickConstant

    fun onClick(click: (type: ClickType, cursor: ItemStack) -> Unit) {
        _click = click
    }

    fun onTick(tick: (frame: Int) -> ItemStackBuilder) {
        _tick = tick
    }

    fun lore(line: String) {
        itemBuilder.lore(line)
    }

    fun enchant(enchantment: Enchantment, level: Int) {
        itemBuilder.enchant(enchantment, level)
    }

    fun flag(itemFlag: ItemFlag) {
        itemBuilder.flag(itemFlag)
    }

    fun clearLore() {
        itemBuilder.clearLore()
    }

    fun clearEnchants() {
        itemBuilder.clearEnchants()
    }

    fun clearFlags() {
        itemBuilder.clearFlags()
    }

    override fun render(menuView: MenuView, offsetX: Int, offsetY: Int) {
        if (y < menuView.menu.root.topSize || menuView.menu.root.enableBottom) {
            if (_click !== clickConstant) {
                menuView.viewMeta.click(x + offsetX, y + offsetY, this)
            }
            if (_tick !== tickConstant) {
                menuView.viewMeta.animate(x + offsetX, y + offsetY, this)
            }
            menuView.setItem(x + offsetX, y + offsetY, itemBuilder.render())
        }
    }

    companion object {
        private val clickConstant: (type: ClickType, cursor: ItemStack) -> Unit = { _, _ -> }
        // actually returns an empty ItemStackBuilder, but we use reference comparison
        private val tickConstant: (frame: Int) -> ItemStackBuilder = { _ -> ItemStackBuilder() }
    }
}

fun root(viewElement: ViewElement, init: RootElement.() -> Unit = {}): RootElement {
    val root = RootElement(viewElement)
    root.init()
    return root
}

fun container(x: Int = 0, y: Int = 0, init: ContainerElement.() -> Unit = {}): ContainerElement {
    val container = ContainerElement(x, y)
    container.init()
    return container
}

fun item(x: Int = 0, y: Int = 0, init: ItemElement.() -> Unit = {}): ItemElement {
    val item = ItemElement(x, y)
    item.init()
    return item
}