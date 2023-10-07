package me.jsinco.withdrawals.commands.subcommands

import me.jsinco.withdrawals.Withdrawals
import me.jsinco.withdrawals.commands.SubCommand
import me.jsinco.withdrawals.hooks.PlayerPointsHook
import me.jsinco.withdrawals.hooks.VaultHook
import me.jsinco.withdrawals.obj.CreateWithdrawalItem
import me.jsinco.withdrawals.obj.CurrencyType
import me.jsinco.withdrawals.util.Util
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WithdrawPoints : SubCommand {
    override fun execute(plugin: Withdrawals, sender: CommandSender, args: Array<out String>) {
        val player = sender as? Player ?: return

        if (args.size < 2) return
        val ppAPI = PlayerPointsHook.playerPointsAPI

        val amount: Int = if (args[1].equals("all", true)) {
            ppAPI.look(player.uniqueId)
        } else {
            args[1].toIntOrNull() ?: return
        }

        if (ppAPI.look(player.uniqueId) < amount || amount <= 0) {
            player.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}You do not have enough Solcoins to withdraw that amount!"))
            return
        }

        ppAPI.take(player.uniqueId, amount)
        val withdrawalItem = CreateWithdrawalItem(CurrencyType.POINTS, amount.toDouble())
        Util.giveItem(player, withdrawalItem.getItem())

        player.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}You withdrew &#f76a3b${String.format("%,d", amount)} &#E2E2E2Solcoins!"))
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
        return "withdrawals.points"
    }

    override fun playerOnly(): Boolean {
        return true
    }
}