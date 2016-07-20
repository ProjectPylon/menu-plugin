package com.dummyc0m.pylon.datakit.bukkitcommons.app

import com.dummyc0m.pylon.datakit.bukkitcommons.app.view.RootElement
import com.dummyc0m.pylon.datakit.bukkitcommons.app.view.ViewMeta
import com.dummyc0m.pylon.datakit.bukkitcommons.util.Frame
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

/**
 * Created by Dummy on 7/13/16.
 */
abstract class Menu {
    abstract val root: RootElement
    abstract val enableBottom: Boolean

    fun render(plugin: JavaPlugin, humanEntity: HumanEntity, argumentMap: Map<String, Any>) {
        val top = Bukkit.createInventory(humanEntity,
                root.topSize * 9,
                root.title)
        val bottom: Inventory
        if (enableBottom) {
            bottom = Bukkit.createInventory(humanEntity,
                    InventoryType.PLAYER,
                    root.title)
        } else {
            bottom = humanEntity.inventory
        }
        val view = MenuView(humanEntity, top, bottom, enableBottom, this, ViewMeta(argumentMap))
        root.render(view)
        Bukkit.getScheduler().runTask(plugin) {
            humanEntity.openInventory(view)
        }
        if (view.viewMeta.animationHandler.isNotEmpty()) {
            Animator(view).runTaskTimer(plugin, 2L, 4L)
        }
    }

    class Animator(val view: MenuView) : BukkitRunnable() {
        var frame = Frame(0)

        override fun run() {
            if (view.hasClosed) {
                cancel()
            }
            view.viewMeta.animationHandler.forEach {
                element ->
                element.tick.invoke(view, frame)
            }
        }
    }
}