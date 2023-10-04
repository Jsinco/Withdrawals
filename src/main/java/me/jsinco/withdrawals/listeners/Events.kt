package me.jsinco.withdrawals.listeners

import me.jsinco.withdrawals.Withdrawals
import me.jsinco.withdrawals.hooks.PlayerPointsHook
import me.jsinco.withdrawals.hooks.VaultHook
import me.jsinco.withdrawals.obj.CurrencyType
import me.jsinco.withdrawals.obj.WithdrawalItem
import me.jsinco.withdrawals.util.Util
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class Events(private val plugin: Withdrawals) : Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.item == null) return
        val withdrawalItem = WithdrawalItem(event.item!!)


        if (!withdrawalItem.isWithdrawalItem || (event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.RIGHT_CLICK_AIR)) {
            return
        }

        val currencyType = withdrawalItem.currencyType
        var amountRedeemed = ""

        when (currencyType) {
            CurrencyType.EXPERIENCE -> {
                event.player.exp += withdrawalItem.doubleAmount!!.toFloat()
                amountRedeemed = withdrawalItem.doubleAmount.toString()
            }
            CurrencyType.MONEY -> {
                val econ = VaultHook.getEconomy()
                econ.depositPlayer(event.player, withdrawalItem.doubleAmount!!)
                amountRedeemed = "$${withdrawalItem.doubleAmount}"
            }

            CurrencyType.POINTS -> {
                val ppAPI = PlayerPointsHook.playerPointsAPI
                ppAPI.give(event.player.uniqueId, withdrawalItem.intAmount!!)
                amountRedeemed = withdrawalItem.intAmount.toString()
            }
            null -> {
                return
            }
        }

        event.item!!.amount -= 1
        event.player.sendMessage(Util.colorcode("${plugin.config.getString("prefix")}You redeemed $amountRedeemed ${currencyType.currencyName}!"))
    }
}