package com.dummyc0m.pylon.datakit.bukkitcommons

import com.dummyc0m.pylon.datakit.bukkitcommons.app.AppListener
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

/**
 * Created by Dummy on 7/5/16.
 */
class BKPlugin : JavaPlugin() {
    override fun onEnable() {
        _log = logger
        logger.info("Starting")
        server.pluginManager.registerEvents(AppListener(this), this)
    }

    override fun onDisable() {
        logger.info("Bye")
    }

    override fun onCommand(sender: CommandSender, command: Command?, label: String, args: Array<out String>?): Boolean {
        if ("commons".equals(label, true) && sender is Player) {
            sender.sendMessage("Running BukkitCommons for CraftBukkit 1.10.2")
            return true
        }
        return false
    }

    companion object {
        val logger: Logger
            get() = _log
        private lateinit var _log: Logger
    }
}