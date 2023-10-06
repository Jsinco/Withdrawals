package me.jsinco.withdrawals.obj

import com.iridium.iridiumcolorapi.IridiumColorAPI
import me.jsinco.withdrawals.Withdrawals
import me.jsinco.withdrawals.util.Util
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

    val material: Material = Material.valueOf(plugin.config.getString("withdrawal-items.$currencyType.material")!!)
    val name = plugin.config.getString("withdrawal-items.$currencyType.name")!!
    val customModelData = plugin.config.getInt("withdrawal-items.$currencyType.custom-model-data")
    val enchantGlint = plugin.config.getBoolean("withdrawal-items.$currencyType.enchant-glint")
    val lore = plugin.config.getStringList("withdrawal-items.$currencyType.lore").toMutableList()

    fun getItem(): ItemStack { // Help me
        val item = ItemStack(material)
        val meta = item.itemMeta!!
        if (customModelData != 0) meta.setCustomModelData(customModelData)
        if (enchantGlint) {
            meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        meta.persistentDataContainer.set(NamespacedKey(plugin, "currency"), PersistentDataType.STRING, currencyType.toString())
        val stringAmount: String = when (currencyType) {
            CurrencyType.EXPERIENCE, CurrencyType.MONEY -> {
                meta.persistentDataContainer.set(NamespacedKey(plugin, "amount"), PersistentDataType.DOUBLE, amount)
                String.format("%,.2f", amount)
            }

            CurrencyType.POINTS -> {
                meta.persistentDataContainer.set(NamespacedKey(plugin, "amount"), PersistentDataType.INTEGER, amount.toInt())
                String.format("%,d", amount.toInt())
            }
        }

        for (i in lore.indices) {
            lore[i] = IridiumColorAPI.process(Util.colorcode(lore[i].replace("%amount%", stringAmount)))
        }
        if (lore.isNotEmpty()) meta.lore = lore
        meta.setDisplayName(IridiumColorAPI.process(Util.colorcode(name.replace("%amount%", stringAmount))))


        item.itemMeta = meta
        return item
    }
}