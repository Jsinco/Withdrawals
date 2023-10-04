package me.jsinco.withdrawals.commands

import me.jsinco.withdrawals.Withdrawals
import me.jsinco.withdrawals.commands.subcommands.WithdrawExperience
import me.jsinco.withdrawals.commands.subcommands.WithdrawMoney
import me.jsinco.withdrawals.commands.subcommands.WithdrawPoints
import me.jsinco.withdrawals.util.Util
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class CommandManager(val plugin: Withdrawals) : CommandExecutor, TabCompleter {

    private val subCommands: MutableMap<String, SubCommand> = mutableMapOf()

    init {
        // SubCommands
        subCommands["experience"] = WithdrawExperience()
        subCommands["money"] = WithdrawMoney()
        subCommands["solcoins"] = WithdrawPoints()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}Specify a currency to withdraw"))
            return true
        }

        val subCommand: SubCommand = subCommands[args[0].lowercase()] ?: return true

        if (subCommand.permission() != null && !sender.hasPermission(subCommand.permission()!!)) {
            sender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}You do not have permission to use this!"))
            return true
        } else if (subCommand.playerOnly() && sender !is Player) {
            sender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}Only players can use this command!"))
            return true
        }

        subCommand.execute(plugin, sender, args)
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        if (args.size == 1) {
            return subCommands.keys.toMutableList()
        }

        val subCommand: SubCommand = subCommands[args[0].lowercase()] ?: return null
        return subCommand.tabComplete(plugin, sender, args)?.toMutableList()
    }
}