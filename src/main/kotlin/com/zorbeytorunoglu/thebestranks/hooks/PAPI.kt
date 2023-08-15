package com.zorbeytorunoglu.thebestranks.hooks

import com.zorbeytorunoglu.thebestranks.TBR
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer

class PAPI(private val plugin: TBR): PlaceholderExpansion() {

    override fun getIdentifier(): String {
        return "thebestranks"
    }

    override fun getAuthor(): String {
        return "thelegend"
    }

    override fun getVersion(): String {
        return plugin.description.version
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onRequest(player: OfflinePlayer, params: String): String? {

        if (params.equals("rank_prefix", ignoreCase = true)) {
            return plugin.rankManager.getRank(player.uniqueId)?.prefix
        }

        if (params.equals("rank_displayname", ignoreCase = true)) {
            return plugin.rankManager.getRank(player.uniqueId)?.displayName
        }

        return if (params.equals("rank_order", ignoreCase = true)) {
            return plugin.rankManager.getRank(player.uniqueId)?.order.toString()
        } else null

    }

}