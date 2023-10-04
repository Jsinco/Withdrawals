package me.jsinco.withdrawals.hooks

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit


object VaultHook {

    private lateinit var econ: Economy

    fun setupEconomy(): Boolean {
        val rsp = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java) ?: return false
        econ = rsp.provider
        return true
    }

    fun getEconomy(): Economy {
        return econ
    }
}