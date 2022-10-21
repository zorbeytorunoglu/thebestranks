package com.zorbeytorunoglu.thebestranks.hooks

import com.zorbeytorunoglu.thebestranks.TBR
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

class PAPI(private val plugin: TBR) : PlaceholderExpansion() {

    override fun getAuthor(): String {
        return "thelegend"
    }

    override fun getIdentifier(): String {
        return "thebestranks"
    }

    override fun getVersion(): String {
        return plugin.description.version
    }

    override fun getRequiredPlugin(): String? {
        return "TheBestRanks"
    }

    override fun canRegister(): Boolean {
        return Bukkit.getServer().pluginManager.getPlugin(requiredPlugin)!=null
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onRequest(player: OfflinePlayer, params: String): String? {

        if (params.equals("rank", ignoreCase = true)) {

            return plugin.getUtils().getRankUtils().getRank(player.uniqueId).getPrefix()

        }

        return if (params.equals("next_rank", ignoreCase = true)) {
            plugin.getUtils().getRankUtils()
                .getNextRank(plugin.getUtils().getRankUtils().getRank(player.uniqueId)).getPrefix()
        } else plugin.getMessageHandler().getNoGreaterRankPrefix()

    }
}