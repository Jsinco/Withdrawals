package me.jsinco.withdrawals.commands

import me.jsinco.withdrawals.Withdrawals
import org.bukkit.command.CommandSender

interface SubCommand {

    /**
     * The execution code of the command
     * @param plugin the plugin instance
     * @param sender the sender of the command
     * @param args the arguments of the command
     */
    fun execute(plugin: Withdrawals, sender: CommandSender, args: Array<out String>)

    /**
     * Tab completion for this commandq
     * @param plugin the plugin instance
     * @param sender the sender of the command
     * @param args the arguments of the command
     * @return a list of possible completions
     */
    fun tabComplete(plugin: Withdrawals, sender: CommandSender, args: Array<out String>): List<String>?

    /**
     * @return the permission node required for the command, if any
     */
    fun permission(): String?

    /**
     * Whether this command can only be executed by a player
     * @return true if this command can only be executed by a player
     */
    fun playerOnly(): Boolean
}
