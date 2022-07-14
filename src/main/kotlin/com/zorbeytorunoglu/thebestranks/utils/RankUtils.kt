package com.zorbeytorunoglu.thebestranks.utils

import com.zorbeytorunoglu.thebestranks.TBR
import com.zorbeytorunoglu.thebestranks.configuration.ranks.Rank
import com.zorbeytorunoglu.thebestranks.configuration.ranks.Requirement
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID

class RankUtils(private val plugin: TBR) {

    fun playerHasRank(player: Player): Boolean {
        return plugin.getPlayerRanks().containsKey(player.uniqueId)
    }

    fun playerHasRank(uuid: UUID): Boolean {
        return plugin.getPlayerRanks().containsKey(uuid)
    }

    fun getRank(player: Player): Rank {
        return plugin.getPlayerRanks()[player.uniqueId]!!
    }

    fun getRank(uuid: UUID): Rank {
        return plugin.getPlayerRanks()[uuid]!!
    }

    fun nextRankExists(rank: Rank): Boolean {

        val nextRankInt: Int = rank.getId().toInt()+1

        for (r in plugin.getRanks()) {

            if (r.getId().toInt()==nextRankInt) return true

        }

        return false

    }

    fun getNextRank(rank: Rank): Rank {

        val nextRankInt: Int = rank.getId().toInt()+1

        return plugin.getRanks().first { rank: Rank -> rank.getId().toInt() == nextRankInt }

    }

    fun requirementFulfilled(player: Player, requirement: Requirement): Boolean {

        return requirement.getRequired()<=StringUtils.getNumberFromString(
                ChatColor.stripColor(PlaceholderAPI.setPlaceholders(player, requirement.getPlaceholder())))

    }

    fun submitCommands(player: Player, rank: Rank) {

        if (rank.getCommands().isEmpty()) return

        for (i: Int in 0 until rank.getCommands().size) {

            object : BukkitRunnable() {
                override fun run() {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender, rank.getCommands()[i].replace("%player_name%", player.name))
                }
            }.runTaskLater(plugin, i.toLong())
        }

    }

    fun rankUp(player: Player) {

        if (!plugin.getPlayerRanks().containsKey(player.uniqueId)) return

        val rank: Rank = getRank(player)

        plugin.getPlayerRanks()[player.uniqueId] = this.getNextRank(rank)

    }

    fun getFirstRank(): Rank? {

        if (plugin.getRanks().isEmpty()) return null

        for (rank in plugin.getRanks()) {
            if (rank.getId() == "0") return rank
        }

        return null

    }

    fun rankExists(rankId: String): Boolean {

        if (plugin.getRanks().isEmpty()) return false

        return plugin.getRanks().any { it.getId() == rankId }

    }

    fun getRank(rankId: String): Rank? {

        if (plugin.getRanks().isEmpty()) return null

        for (rank in plugin.getRanks()) {
            if (rank.getId() == rankId) return rank
        }

        return null

    }

    fun setRank(rankId: String, player: Player) {

        plugin.getPlayerRanks()[player.uniqueId] = getRank(rankId)!!

    }

    fun setRank(rank: Rank, player: Player) {

        plugin.getPlayerRanks()[player.uniqueId] = rank

    }

    fun rankPassed(player: Player, rank: Rank): Boolean {

        return getRank(player.uniqueId).getId().toInt() > rank.getId().toInt()

    }

}