package com.dummyc0m.pylon.demo

import com.dummyc0m.pylon.MenuPlugin
import com.dummyc0m.pylon.app.component.AppRoot
import com.dummyc0m.pylon.app.component.Component
import com.dummyc0m.pylon.app.component.RootComponent
import com.dummyc0m.pylon.app.component.bootstrapApp
import com.dummyc0m.pylon.app.view.*
import com.dummyc0m.pylon.icon.handler.IconHandler
import com.dummyc0m.pylon.util.*
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin

// this is for the demo stick
class DemoHandler: IconHandler() {
    override fun onInteract(event: PlayerInteractEvent) {
        showDemoMenu(MenuPlugin.instance, event.player)
    }
}

// bootstraps and shows the demo menu
fun showDemoMenu(
        javaPlugin: JavaPlugin,
        humanEntity: HumanEntity
) {
    bootstrapApp(javaPlugin, humanEntity) {
        DemoHome(it)
    }
}

// root component
class DemoHome(app: AppRoot) : RootComponent(app) {
    // delegation provides a nice way to detect changes in data
    private var switch by prop(true)
    private var childCounter by prop(1)
    private val child = DemoChild(app) { childCounter++ }

    // renders stuff in the chest view
    override fun render(): RootElement {
        return root(container {
            MenuPlugin.logger.info("switch is $switch")
            // shorthand for adding an item
            i(if (switch) 0 else 5, 0) {
                material = Material.ANVIL
                displayName = "${RESET}child count: $childCounter"
                lore("$RESET${DARK_GREEN}click to move me around")

                // add callbacks for clicking on an item
                // only changing member properties like switch/childCounter here refreshes the view
                onClick { type, cursor ->
                    switch = !switch
                }
            }

            // shorthand for adding a container
            c(0, 1) {
                h(child.render())
            }

            // shorthand for item again
            i(5, 2) {
                material = Material.APPLE
                displayName = "${RESET}I'm an apple"
                enchant(Enchantment.DAMAGE_ALL, 32767)
            }
        }) {
            title = "Beautiful Demo"
            // size of the top inventory
            topSize = 2
            // enables using player inventory as an extended display panel
            enableBottom = true
        }
    }
}

// child component
class DemoChild(app: AppRoot, private val onClick: () -> Unit) : Component(app) {
    private val const = 1

    override fun render(): ViewElement {
        return item {
            material = Material.ANVIL
            displayName = "$RESET${GOLD}increment parent counter"
            damage = 0
            // we can pass in a callback from the parent
            onClick { type, cursor -> this@DemoChild.onClick() }
            // a jank way to animate items
            onTick { frame ->
                itemBuilder {
                    material = Material.ANVIL
                    displayName = "$RESET${GOLD}increment parent counter"
                    damage = (frame % 3 + const).toShort()
                    onClick { type, cursor -> this@DemoChild.onClick() }
                }
            }
        }
    }
}