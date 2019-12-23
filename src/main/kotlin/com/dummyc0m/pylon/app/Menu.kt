package com.dummyc0m.pylon.app

import com.dummyc0m.pylon.app.view.RootElement
import com.dummyc0m.pylon.app.view.ViewMeta
import com.dummyc0m.pylon.util.setItem
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Dummy on 7/13/16.
 */
class Menu(
        val plugin: JavaPlugin, val humanEntity: HumanEntity,
        val root: RootElement,
        val frame: AtomicInteger, val suppressOpen: Boolean) {
    internal fun render(): MenuView {
        val top = Bukkit.createInventory(humanEntity,
                root.topSize * 9,
                root.title)
        val bottom: Inventory
        if (root.enableBottom) {
            bottom = Bukkit.createInventory(humanEntity,
                    InventoryType.PLAYER,
                    root.title)
        } else {
            bottom = humanEntity.inventory
        }
        val view = MenuView(top, bottom, this, ViewMeta(), suppressOpen)
        root.render(view, 0, 0)
        return view
    }

    class Animator(private val view: MenuView) : BukkitRunnable() {
        override fun run() {
            if (view.hasClosed) {
                cancel()
                return
            }
            val currFrame = view.menu.frame.getAndIncrement()
            view.viewMeta.animationHandler.forEach {
                (idx, element) ->
                val newItemStackBuilder = element.tick.invoke(currFrame)
                if (newItemStackBuilder != element.itemBuilder) {
                    view.setItem(idx, newItemStackBuilder.render())
                }
            }
        }
    }
}