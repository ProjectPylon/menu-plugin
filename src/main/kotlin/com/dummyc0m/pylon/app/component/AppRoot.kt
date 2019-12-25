package com.dummyc0m.pylon.app.component

import com.dummyc0m.pylon.app.Menu
import com.dummyc0m.pylon.app.MenuView
import com.dummyc0m.pylon.app.view.RootElement
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Dummy on 12/23/19.
 */

class AppRoot(
        private val plugin: JavaPlugin,
        private val humanEntity: HumanEntity
) {
    internal lateinit var rootComponent: RootComponent
    private lateinit var currentView: MenuView

    private val frame = AtomicInteger()
    private val refreshing = AtomicBoolean(true)

    internal fun show() {
        Bukkit.getScheduler().runTask(plugin) {
            refreshing.set(false)
            val rendered = Menu(plugin, humanEntity, rootComponent.render(), frame, false).render()
            currentView = rendered
            humanEntity.openInventory(
                    rendered
            )
            currentView.animate()
        }
    }

    private fun requireRerender(newRootElement: RootElement): Boolean {
        val currentRootElement = currentView.menu.root
        return newRootElement.title != currentRootElement.title
                || newRootElement.enableBottom != currentRootElement.enableBottom
                || newRootElement.topSize != currentRootElement.topSize
    }

    // assumes that new and old have the same size
    private fun patchCurrentView(newRootElement: RootElement) {
        val menu = Menu(plugin, humanEntity, newRootElement, frame, true)
        val rendered = menu.render()
        if (newRootElement.enableBottom) {
            // replace each item
//            currentView.bottomInventory.replaceWith(rendered.bottomInventory)
            // replaces entire contents
            currentView.bottomInventory.contents = rendered.bottomInventory.contents
        }
        // replace each item
//        currentView.topInventory.replaceWith(rendered.topInventory)
        // replaces entire contents
        currentView.topInventory.contents = rendered.topInventory.contents

        currentView.menu = menu
        if (currentView.viewMeta.animationHandler.isEmpty() &&
                rendered.viewMeta.animationHandler.isNotEmpty()) {
            currentView.animate()
        }
        currentView.viewMeta = rendered.viewMeta
    }

    internal fun refresh(suppressOpen: Boolean = true) {
        if (refreshing.compareAndSet(false, true)) {
            Bukkit.getScheduler().runTask(plugin) {
                synchronized(refreshing) {
                    refreshing.set(false)
                    val newRootElement = rootComponent.render()
                    if (requireRerender(newRootElement)) {
                        currentView.rerendering = true
                        humanEntity.closeInventory()
                        val rendered = Menu(plugin, humanEntity, newRootElement, frame, suppressOpen).render()
                        currentView = rendered
                        humanEntity.openInventory(
                                rendered
                        )
                        currentView.animate()
                    } else {
                        patchCurrentView(newRootElement)
                    }
                }
            }
        } else {
            // already refreshing
        }
    }
}

fun <R: RootComponent> bootstrapApp(
        plugin: JavaPlugin,
        humanEntity: HumanEntity,
        init: (AppRoot) -> R
) {
    val app = AppRoot(plugin, humanEntity)
    app.rootComponent = init(app)
    app.show()
}