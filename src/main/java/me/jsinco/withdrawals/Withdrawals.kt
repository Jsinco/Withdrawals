package me.jsinco.withdrawals

import me.jsinco.withdrawals.commands.CommandManager
import me.jsinco.withdrawals.hooks.VaultHook
import me.jsinco.withdrawals.listeners.Events
import org.bukkit.plugin.java.JavaPlugin

class Withdrawals : JavaPlugin() {
    override fun onEnable() {
        plugin = this

        VaultHook.setupEconomy()

        // No need for a file manager
        config.options().copyDefaults(true)
        saveDefaultConfig()

        server.pluginManager.registerEvents(Events(this), this)
        getCommand("withdrawals")!!.setExecutor(CommandManager(this))
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


    companion object {
        lateinit var plugin: Withdrawals
    }
}
