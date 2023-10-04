package me.jsinco.withdrawals.obj

import me.jsinco.withdrawals.Withdrawals
import me.jsinco.withdrawals.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class CreateWithdrawalItem(
    private val currencyType: CurrencyType,
    private val amount: Double
) {

    private val plugin: Withdrawals = Withdrawals.plugin

    fun getItem(): ItemStack { // Help me
        val material: Material = Material.valueOf(plugin.config.getString("withdrawal-items.$currencyType.material")!!)
        val name = Util.colorcode(plugin.config.getString("withdrawal-items.$currencyType.name")!!.replace("%amount%", amount.toString()))
        val customModelData = plugin.config.getInt("withdrawal-items.$currencyType.custom-model-data")
        val enchantGlint = plugin.config.getBoolean("withdrawal-items.$currencyType.enchant-glint")

        val lore = Util.colorList(plugin.config.getStringList("withdrawal-items.$currencyType.lore")).toMutableList()
        for (i in lore.indices) {
            lore[i] = lore[i].replace("%amount%", amount.toString())
        }

        val item = ItemStack(material)
        val meta = item.itemMeta!!

        meta.setDisplayName(name)
        if (lore.isNotEmpty()) meta.lore = lore
        if (customModelData != 0) meta.setCustomModelData(customModelData)
        if (enchantGlint) {
            meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        meta.persistentDataContainer.set(NamespacedKey(plugin, "currency"), PersistentDataType.STRING, currencyType.toString())
        when (currencyType) {
            CurrencyType.EXPERIENCE, CurrencyType.MONEY -> {
                meta.persistentDataContainer.set(NamespacedKey(plugin, "amount"), PersistentDataType.DOUBLE, amount)
            }
            CurrencyType.POINTS -> {
                meta.persistentDataContainer.set(NamespacedKey(plugin, "amount"), PersistentDataType.INTEGER, amount.toInt())
            }
        }

        item.itemMeta = meta
        return item
    }
}