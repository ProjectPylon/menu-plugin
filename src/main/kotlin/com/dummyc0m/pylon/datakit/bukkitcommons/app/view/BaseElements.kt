package com.dummyc0m.pylon.datakit.bukkitcommons.app.view

import com.dummyc0m.pylon.datakit.bukkitcommons.app.MenuView
import com.dummyc0m.pylon.datakit.bukkitcommons.util.DynamicItemBuilder
import com.dummyc0m.pylon.datakit.bukkitcommons.util.Frame
import com.dummyc0m.pylon.datakit.bukkitcommons.util.setItem
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

/**
 * Nothing is thread safe
 * Created by Dummy on 7/12/16.
 */
abstract class Tag() : ViewElement {
    override var x = 0
    override var y = 0
}

/**
 * does not deny clicks
 */
class BlankElement internal constructor() : Tag() {
    val click: (view: MenuView, type: ClickType, cursor: ItemStack) -> Boolean
        get() = _click
    private var _click: (view: MenuView, type: ClickType, cursor: ItemStack) -> Boolean = clickConstant

    /**
     * return true if the action is denied
     * default: false
     */
    fun onClick(click: (view: MenuView, type: ClickType, cursor: ItemStack) -> Boolean) {
        _click = click
    }

    override fun render(menuView: MenuView) {
        if (y < menuView.menu.root.topSize || menuView.enableBottom) {
            menuView.viewMeta.blank(this)
        }
    }

    companion object {
        private val clickConstant: (view: MenuView, type: ClickType, cursor: ItemStack) -> Boolean = { view, type, cursor -> false }
    }
}

open class ContainerElement : Tag() {
    val children: MutableList<ViewElement> = ArrayList()

    override fun render(menuView: MenuView) {
        for (c in children) {
            c.render(menuView)
        }
    }

    protected fun <T : ViewElement> initElement(element: T, init: T.() -> Unit): T {
        element.init()
        element.x += x
        element.y += y
        children.add(element)
        return element
    }

    fun static(init: StaticItemElement.() -> Unit) =
            initElement(StaticItemElement(), init)

    fun animated(init: AnimatedItemElement.() -> Unit) =
            initElement(AnimatedItemElement(), init)

    fun empty(init: BlankElement.() -> Unit) =
            initElement(BlankElement(), init)

    fun <T : ContainerElement> container(containerElement: T, init: T.() -> Unit) =
            initElement(containerElement, init)
}

class RootElement internal constructor() : ContainerElement() {
    var title: String = "Container"
    var topSize: Int = 6
    val open: (view: MenuView) -> Unit
        get() = _open
    private var _open: (view: MenuView) -> Unit = ocConstant

    val close: (view: MenuView) -> Unit
        get() = _close
    private var _close: (view: MenuView) -> Unit = ocConstant

    /**
     * only when bottom inventory is humanEntity inventory and transfering up
     * true if consumed item
     *
     * default: false
     */
    val transfer: (view: MenuView, item: ItemStack) -> Boolean
        get() = _transfer
    private var _transfer: (view: MenuView, item: ItemStack) -> Boolean = transferConstant

    fun onOpen(open: (view: MenuView) -> Unit) {
        _open = open
    }

    fun onClose(close: (view: MenuView) -> Unit) {
        _close = close
    }

    fun onTransfer(transfer: (view: MenuView, item: ItemStack) -> Boolean) {
        _transfer = transfer
    }

    companion object {
        private val transferConstant: (view: MenuView, item: ItemStack) -> Boolean = { view, item -> false }
        private val ocConstant: (view: MenuView) -> Unit = {}
    }
}

/**
 * denies all clicks
 */
abstract class ItemElement : Tag() {
    val click: (view: MenuView, type: ClickType, cursor: ItemStack) -> Unit
        get() = _click
    private var _click: (view: MenuView, type: ClickType, cursor: ItemStack) -> Unit = clickConstant

    private val itemBuilder = DynamicItemBuilder()

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

    var displayName: String
        get() = itemBuilder.displayName
        set(value) {
            itemBuilder.displayName = value
        }

    fun onClick(click: (view: MenuView, type: ClickType, cursor: ItemStack) -> Unit) {
        _click = click
    }

    fun onRender(render: (itemMeta: ItemMeta, humanEntity: HumanEntity) -> Unit) {
        itemBuilder.onRender(render)
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

    override fun render(menuView: MenuView) {
        if (y < menuView.menu.root.topSize || menuView.enableBottom) {
            if (_click !== clickConstant) {
                menuView.viewMeta.click(this)
            }
            menuView.setItem(x, y, itemBuilder.render(menuView.player))
        }
    }

    companion object {
        private val clickConstant: (view: MenuView, type: ClickType, cursor: ItemStack) -> Unit = { view, type, cursor -> }
    }
}

class StaticItemElement internal constructor() : ItemElement()

class AnimatedItemElement internal constructor() : ItemElement() {
    val tick: (view: MenuView, frame: Frame) -> Unit
        get() = _tick
    private var _tick: (view: MenuView, frame: Frame) -> Unit = tickConstant

    fun onTick(tick: (view: MenuView, frame: Frame) -> Unit) {
        _tick = tick
    }

    override fun render(menuView: MenuView) {
        if (y < menuView.menu.root.topSize || menuView.enableBottom) {
            if (_tick !== tickConstant) {
                menuView.viewMeta.animate(this)
            }
            super.render(menuView)
        }
    }

    companion object {
        private val tickConstant: (view: MenuView, frame: Frame) -> Unit = { view, frame -> }
    }
}

fun menu(init: RootElement.() -> Unit): RootElement {
    val root = RootElement()
    root.init()
    return root
}