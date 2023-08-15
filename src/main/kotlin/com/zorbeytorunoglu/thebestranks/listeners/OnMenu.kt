package com.zorbeytorunoglu.thebestranks.listeners

import com.zorbeytorunoglu.kLib.extensions.colorHex
import com.zorbeytorunoglu.thebestranks.TBR
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent

class OnMenu(private val plugin: TBR): Listener {

    @EventHandler
    fun onMenu(event: InventoryClickEvent) {

        if (event.clickedInventory == null) return

        if (event.click != ClickType.LEFT) return

        if (event.view.title == null) return

        if (!plugin.menuManager.pages.containsKey(event.whoClicked.uniqueId.toString())) return

        if (event.currentItem == null) return

        if (!event.currentItem.hasItemMeta()) return

        val page = plugin.menuManager.pages[event.whoClicked.uniqueId.toString()]!!

        val currentPage = page.currentPage+1

        if (event.view.title != plugin.menuManager.config.name.replace("%page%", "$currentPage")
                .colorHex) return

        event.isCancelled = true

        val player = event.whoClicked as Player

        if (event.currentItem == plugin.menuManager.config.nextPageItem) {

            if (currentPage == page.pages.size) return

            player.closeInventory()

            player.openInventory(page.pages[page.currentPage+1])

            page.currentPage++

            return

        }

        if (event.currentItem == plugin.menuManager.config.previousPageItem) {

            if (currentPage == 1) return

            player.closeInventory()

            player.openInventory(page.pages[page.currentPage-1])

            page.currentPage--

            return

        }

        val nextRank = plugin.rankManager.getNextRank(player)

        if (nextRank != null) {

            val inProgressItemName = plugin.menuManager.config.inProgressItem.itemMeta.displayName
                .replace("%rank%", nextRank.displayName.colorHex)

            if (event.currentItem.itemMeta.displayName == inProgressItemName) {
                player.closeInventory()
                plugin.rankManager.rankUp(player, nextRank)
                return
            }
        }

    }

}