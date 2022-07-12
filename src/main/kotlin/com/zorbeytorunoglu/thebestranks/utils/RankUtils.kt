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

        for (i in 0..rank.getCommands().size) {
            object : BukkitRunnable() {
                override fun run() {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().consoleSender, rank.getCommands()[i])
                }
            }.runTaskLater(plugin, i.toLong())
        }

    }

    fun rankUp(player: Player) {

        if (!plugin.getPlayerRanks().containsKey(player.uniqueId)) return

        val rank: Rank = getRank(player)

        plugin.getPlayerRanks()[player.uniqueId] = this.getNextRank(rank)

    }

}