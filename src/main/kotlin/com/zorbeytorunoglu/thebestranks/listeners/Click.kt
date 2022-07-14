package com.zorbeytorunoglu.thebestranks.listeners

import com.zorbeytorunoglu.thebestranks.TBR
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class Click(private val plugin: TBR): Listener {

    @EventHandler
    fun onClick(event: InventoryClickEvent) {

        if (!plugin.getMenu().getEnabled()) return

        if (event.inventory==null) return

        if (event.currentItem==null) return

        if (event.currentItem.type == Material.AIR) return

        if (event.view.title != plugin.getMenu().getTitle()) return

        event.isCancelled = true

        if (event.currentItem.type == plugin.getMenu().getInProgressItem().type) {
            Bukkit.getServer().dispatchCommand(event.whoClicked as Player, "rank up")
            val player = event.whoClicked as Player
            player.closeInventory()
        }

    }

}