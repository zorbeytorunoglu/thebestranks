package com.zorbeytorunoglu.thebestranks.listeners

import com.zorbeytorunoglu.thebestranks.TBR
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class Join(private val plugin: TBR): Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {

        if (!plugin.getUtils().getRankUtils().playerHasRank(event.player.uniqueId)) {
            plugin.getUtils().getRankUtils().setRank(plugin.getUtils().getRankUtils().getFirstRank()!!,event.player)
        }

    }

}