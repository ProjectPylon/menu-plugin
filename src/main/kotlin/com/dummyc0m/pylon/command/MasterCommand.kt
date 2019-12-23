package com.dummyc0m.pylon.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

import java.util.*

/**
 * Created by Dummyc0m on 7/30/15.
 */
class MasterCommand(label: String, permission: String, playerRequired: Boolean) : AbstractCommand(label, permission, playerRequired), CommandExecutor {
    private val subCommands = HashMap<String, AbstractCommand>()

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

    fun getSubCommands(): List<AbstractCommand> {
        return Collections.unmodifiableList(ArrayList(subCommands.values))
    }

    fun addSubCommand(subCommand: AbstractCommand) {
        subCommands.put(subCommand.label, subCommand)
    }

    fun removeSubCommand(label: String): AbstractCommand? {
        return subCommands.remove(label)
    }

    private fun searchCommand(string: String): AbstractCommand? {
        var subCommand: AbstractCommand? = subCommands[string]
        if (subCommand == null) {
            subCommand = subCommands["help"]
        }
        return subCommand
    }

    private fun shiftArgs(args: Array<String>): Array<String>? {
        return if (args.size > 1) Arrays.copyOfRange(args, 1, args.size - 1) else null
    }
}
