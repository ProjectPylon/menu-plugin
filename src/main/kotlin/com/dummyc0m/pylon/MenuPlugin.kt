package com.dummyc0m.pylon

import com.dummyc0m.pylon.app.AppListener
import com.dummyc0m.pylon.demo.DemoHandler
import com.dummyc0m.pylon.demo.getMasterCommand
import com.dummyc0m.pylon.icon.Icon
import com.dummyc0m.pylon.icon.IconModule
import com.dummyc0m.pylon.util.itemBuilder
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

/**
 * Created by Dummy on 7/5/16.
 */
class MenuPlugin : JavaPlugin() {
    fun setUpDemo() {
        // also check the demo package
        val iconModule = IconModule(this)
        val demoStick = itemBuilder {
            material = Material.STICK
            enchant(Enchantment.DAMAGE_ALL, 10)
        }
        demoStickIcon = iconModule.newIcon("demo_stick", demoStick)
        iconModule.registerIcon(demoStickIcon.id, DemoHandler())
        getCommand("menuplugin").executor = getMasterCommand(demoStickIcon)
    }

    override fun onEnable() {
        privateLogger = logger
        privatePlugin = this
        logger.info("Starting")
        server.pluginManager.registerEvents(AppListener(this), this)
        setUpDemo()
    }

    override fun onDisable() {
        logger.info("Bye")
    }

    override fun onCommand(sender: CommandSender, command: Command?, label: String, args: Array<out String>?): Boolean {
        if ("commons".equals(label, true) && sender is Player) {
            sender.sendMessage("Running MenuPlugin for CraftBukkit 1.8.8")
            return true
        }
        return false
    }

    companion object {
        val logger: Logger
            get() = privateLogger
        private lateinit var privateLogger: Logger

        val instance: MenuPlugin
            get() = privatePlugin
        private lateinit var privatePlugin: MenuPlugin

        internal lateinit var demoStickIcon: Icon
    }
}