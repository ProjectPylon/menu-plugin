package com.dummyc0m.pylon.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Created by Dummyc0m on 7/30/15.
 */
abstract class AbstractCommand(val label: String, val permission: String?, val isPlayerRequired: Boolean) {

    abstract fun execute(sender: CommandSender, args: Array<String>?)

    internal fun processCommand(sender: CommandSender, args: Array<String>?) {
        if (checkPermission(sender) && checkPlayer(sender)) {
            execute(sender, args)
        }
    }

    protected fun checkPermission(sender: CommandSender): Boolean {
        return permission == null || sender.hasPermission(permission)
    }

    protected fun checkPlayer(sender: CommandSender): Boolean {
        return !isPlayerRequired || sender is Player
    }
}
