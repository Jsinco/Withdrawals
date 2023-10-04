package me.jsinco.withdrawals.commands.subcommands

import me.jsinco.withdrawals.Withdrawals
import me.jsinco.withdrawals.commands.SubCommand
import me.jsinco.withdrawals.obj.CreateWithdrawalItem
import me.jsinco.withdrawals.obj.CurrencyType
import me.jsinco.withdrawals.util.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WithdrawExperience : SubCommand {

    @Suppress("unreachable_code") // FIXME
    override fun execute(plugin: Withdrawals, sender: CommandSender, args: Array<out String>) {
        sender.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}This command is currently disabled"))
        return
        val player = sender as? Player ?: return

        if (args.size < 2) return

        val amount: Double = if (args[1].equals("all", true)) {
            player.exp.toDouble()
        } else {
            args[1].toDoubleOrNull() ?: return
        }

        if (player.exp < amount) {
            player.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}You do not have enough experience to withdraw that amount!"))
            return
        }

        player.exp -= amount.toFloat()

        val withdrawalItem = CreateWithdrawalItem(CurrencyType.EXPERIENCE, amount)
        player.inventory.addItem(withdrawalItem.getItem())
        player.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}You withdrew $amount experience!"))
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
        return "withdrawals.experience"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}