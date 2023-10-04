package me.jsinco.withdrawals.obj

import me.jsinco.withdrawals.Withdrawals
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class WithdrawalItem (
    item: ItemStack
) {
    companion object{
        val plugin: Withdrawals = Withdrawals.plugin
    }
    val isWithdrawalItem: Boolean = item.itemMeta?.persistentDataContainer?.has(NamespacedKey(plugin, "currency"), PersistentDataType.STRING) ?: false

    val currencyType: CurrencyType? = item.itemMeta?.persistentDataContainer?.get(NamespacedKey(plugin, "currency"), PersistentDataType.STRING)?.let { CurrencyType.valueOf(it) }
    val doubleAmount: Double? = if (item.itemMeta?.persistentDataContainer?.has(NamespacedKey(plugin, "amount"), PersistentDataType.DOUBLE) == true) {
        item.itemMeta?.persistentDataContainer?.get(NamespacedKey(plugin, "amount"), PersistentDataType.DOUBLE)
    } else {
        null
    }
    val intAmount: Int? = if (item.itemMeta?.persistentDataContainer?.has(NamespacedKey(plugin, "amount"), PersistentDataType.INTEGER) == true) {
        item.itemMeta?.persistentDataContainer?.get(NamespacedKey(plugin, "amount"), PersistentDataType.INTEGER)
    } else {
        null
    }
}