package com.zorbeytorunoglu.thebestranks.listeners

import com.zorbeytorunoglu.thebestranks.TBR
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class OnJoin(private val plugin: TBR): Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {

        if (!plugin.config.rankOnJoin) return

        if (!plugin.rankManager.playerRanks.containsKey(event.player.uniqueId.toString())) {
            plugin.rankManager.getFirstRank()?.let {
                plugin.rankManager.playerRanks[event.player.uniqueId.toString()] = it
            }
        }

    }

}