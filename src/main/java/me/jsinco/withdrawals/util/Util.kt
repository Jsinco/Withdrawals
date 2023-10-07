package me.jsinco.withdrawals.util

import me.jsinco.withdrawals.Withdrawals
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object Util {

    val plugin: Withdrawals = Withdrawals.plugin

    private const val WITH_DELIMITER = "((?<=%1\$s)|(?=%1\$s))"

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    @JvmStatic
    fun colorcode(text: String): String {
        val texts = text.split(String.format(WITH_DELIMITER, "&").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val finalText = StringBuilder()
        var i = 0
        while (i < texts.size) {
            if (texts[i].equals("&", ignoreCase = true)) {
                //get the next string
                i++
                if (texts[i][0] == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7)).toString() + texts[i].substring(7))
                } else {
                    finalText.append(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&" + texts[i]))
                }
            } else {
                finalText.append(texts[i])
            }
            i++
        }
        return finalText.toString()
    }

    fun colorList(list: List<String?>): List<String> {
        val coloredList: MutableList<String> = ArrayList()
        for (string in list) {
            string?.let { colorcode(it) }?.let { coloredList.add(it) }
        }
        return coloredList
    }

    fun giveItem(player: Player, item: ItemStack?) {
        for (i in 0..35) {
            if (player.inventory.getItem(i) == null || player.inventory.getItem(i)!!.isSimilar(item)) {
                player.inventory.addItem(item)
                break
            } else if (i == 35) {
                player.world.dropItem(player.location, item!!)
            }
        }
    }

}