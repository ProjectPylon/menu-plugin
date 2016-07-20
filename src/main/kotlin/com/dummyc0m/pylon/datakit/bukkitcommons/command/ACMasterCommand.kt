package com.dummyc0m.pylon.datakit.bukkitcommons.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

import java.util.*

/**
 * Created by Dummyc0m on 7/30/15.
 */
class ACMasterCommand(label: String, permission: String, playerRequired: Boolean) : ACAbstractCommand(label, permission, playerRequired), CommandExecutor {
    private val subCommands = HashMap<String, ACAbstractCommand>()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        processCommand(sender, args)
        return true
    }

    override fun execute(sender: CommandSender, args: Array<String>?) {
        if (args != null && args.size > 0) {
            val subCommand = searchCommand(args[0])
            subCommand?.processCommand(sender, shiftArgs(args))
        }
    }

    fun getSubCommands(): List<ACAbstractCommand> {
        return Collections.unmodifiableList(ArrayList(subCommands.values))
    }

    fun addSubCommand(subCommand: ACAbstractCommand) {
        subCommands.put(subCommand.label, subCommand)
    }

    fun removeSubCommand(label: String): ACAbstractCommand? {
        return subCommands.remove(label)
    }

    private fun searchCommand(string: String): ACAbstractCommand? {
        var subCommand: ACAbstractCommand? = subCommands[string]
        if (subCommand == null) {
            subCommand = subCommands["help"]
        }
        return subCommand
    }

    private fun shiftArgs(args: Array<String>): Array<String>? {
        return if (args.size > 1) Arrays.copyOfRange(args, 1, args.size - 1) else null
    }
}
