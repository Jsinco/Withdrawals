package me.jsinco.withdrawals.commands.subcommands

import me.jsinco.withdrawals.Withdrawals
import me.jsinco.withdrawals.commands.SubCommand
import me.jsinco.withdrawals.hooks.VaultHook
import me.jsinco.withdrawals.obj.CreateWithdrawalItem
import me.jsinco.withdrawals.obj.CurrencyType
import me.jsinco.withdrawals.util.Util
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WithdrawMoney : SubCommand {
    override fun execute(plugin: Withdrawals, sender: CommandSender, args: Array<out String>) {
        val player = sender as? Player ?: return

        if (args.size < 2) return
        val econ = VaultHook.getEconomy()

        val amount: Double = if (args[1].equals("all", true)) {
            econ.getBalance(player)
        } else {
            args[1].toDoubleOrNull() ?: return
        }

        if (econ.getBalance(player) < amount) {
            player.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}You do not have enough money to withdraw that amount!"))
            return
        } else if (amount < 1) {
            player.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}You must withdraw at least $1!"))
            return
        }
        val withdrawalItem = CreateWithdrawalItem(CurrencyType.MONEY, amount)

        if (econ.withdrawPlayer(player, amount).transactionSuccess()) {
            player.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}You withdrew &a$${String.format("%,.2f", amount)} &#E2E2E2dollars!"))
            Util.giveItem(player, withdrawalItem.getItem())
        } else {
            player.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}An error occurred while withdrawing your money!"))
        }
    }

    override fun tabComplete(plugin: Withdrawals, sender: CommandSender, args: Array<out String>): List<String>? {
        when (args.size) {
            2 -> {
                return listOf("<amount>", "all")
            }
        }
        return null
    }

    override fun permission(): String {
        return "withdrawals.money"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}