package com.dummyc0m.pylon.demo

import com.dummyc0m.pylon.command.AbstractCommand
import com.dummyc0m.pylon.command.MasterCommand
import com.dummyc0m.pylon.icon.Icon
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GetStickCommand(val stickIcon: Icon): AbstractCommand("stick", "pylon.menu.master", true) {
    override fun execute(sender: CommandSender, args: Array<String>?) {
        if (sender is Player) {
            sender.inventory.addItem(stickIcon.getItem())
        } else {
            sender.sendMessage("gotta be a player")
        }
    }
}

class HelpCommand: AbstractCommand("help", "pylon.menu.master", false) {
    override fun execute(sender: CommandSender, args: Array<String>?) {
        sender.sendMessage("try /menuplugin stick")
    }

}

fun getMasterCommand(stickIcon: Icon): CommandExecutor {
    val master = MasterCommand("master", "pylon.menu.master", false)
    master.addSubCommand(GetStickCommand(stickIcon))
    master.addSubCommand(HelpCommand())
    return master
}