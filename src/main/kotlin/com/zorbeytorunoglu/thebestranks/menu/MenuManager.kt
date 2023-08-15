package com.zorbeytorunoglu.thebestranks.menu

import com.zorbeytorunoglu.kLib.configuration.createYamlResource
import com.zorbeytorunoglu.kLib.extensions.colorHex
import com.zorbeytorunoglu.kLib.extensions.numbers
import com.zorbeytorunoglu.thebestranks.TBR
import com.zorbeytorunoglu.thebestranks.rank.Rank
import me.clip.placeholderapi.PlaceholderAPI
import org.apache.commons.lang.StringUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MenuManager(private val plugin: TBR) {

    val config = MenuConfig(plugin, plugin.createYamlResource("menu.yml").load())
    val pages = HashMap<String, Page>()

    fun createInventory(player: Player): Page {

        val items = getMenuRankItems(player)

        val inventories: MutableList<Inventory> = mutableListOf()

        var currentPage = 1

        var inv = plugin.server.createInventory(null, config.size,
            config.name.replace("%page%", currentPage.toString()))

        var index = 0

        for ((innerIndex, item) in items.withIndex()) {

            inv.setItem(index, item)

            if (items.lastIndex == innerIndex) {

                inv.setItem((config.size-1)-3, config.nextPageItem)
                inv.setItem((config.size-1)-5, config.previousPageItem)

                inventories.add(inv)
                break
            }

            if (index == config.size-10) {

                inv.setItem((config.size-1)-3, config.nextPageItem)
                inv.setItem((config.size-1)-5, config.previousPageItem)

                inventories.add(inv)
                currentPage++
                inv = plugin.server.createInventory(null, config.size, config.name.replace("%page%", currentPage.toString()))
                index = 0
                continue
            }

            index++

        }

        val page = Page(player.uniqueId.toString(), 0, inventories.toList())

        pages[player.uniqueId.toString()] = page

        return page

    }

    fun getMenuRankItems(player: Player): List<ItemStack> {

        if (plugin.rankManager.ranks.isEmpty()) return emptyList()

        val playerRank = plugin.rankManager.getRank(player)

        val itemList: MutableList<ItemStack> = mutableListOf()

        plugin.rankManager.ranks.forEach { rank ->

            itemList.add(getRankItem(player, playerRank, rank))

        }

        return itemList

    }

    fun getRankItem(player: Player, playerRank: Rank?, rank: Rank): ItemStack {

        if (playerRank == null) {

            return if (rank == plugin.rankManager.getFirstRank()!!) {
                getItemStackWPlaceholders(player, config.inProgressItem, rank, true)
            } else {
                getItemStackWPlaceholders(player, config.lockedItem, rank, false)
            }

        }

        return if (plugin.rankManager.isCurrentRank(player, playerRank, rank)) {

            getItemStackWPlaceholders(player, config.currentItem, rank, false)

        } else if (plugin.rankManager.rankPassed(player, playerRank, rank)) {

            getItemStackWPlaceholders(player, config.passedItem, rank, false)

        } else if (plugin.rankManager.isInProgress(player,playerRank,rank)) {

            getItemStackWPlaceholders(player,config.inProgressItem, rank, true)

        } else {

            getItemStackWPlaceholders(player, config.lockedItem, rank, false)

        }

    }

    fun getItemStackWPlaceholders(player: Player, itemStack: ItemStack, rank: Rank, isInProgressItem: Boolean): ItemStack {

        val item = itemStack.clone()
        val meta = item.itemMeta

        meta.displayName = meta.displayName.replace("%rank%", rank.displayName).colorHex

        if (isInProgressItem) {

            val lore: List<String>

            if (rank.requirements.isEmpty()) {
                lore = plugin.messages.noRequirementsLore
            } else {
                lore = rank.lore.map {

                    if (it.contains("%requirement_")) {
                        val no = StringUtils.substringBetween(it, "%", "%").numbers
                        val req = rank.requirements[no]

                        it.replace("%requirement_$no%",
                            req.guiMessage.replace("%your%",
                                PlaceholderAPI.setPlaceholders(player, req.placeholder)))
                            .replace("%status%", if (plugin.rankManager.requirementMet(player, req))
                                config.statusDone else config.statusNotDone).colorHex
                    } else {
                        it.replace("%rank%", rank.displayName).colorHex
                    }

                }
            }

            meta.lore = lore

        } else {
            if (meta.lore != null)
                meta.lore = meta.lore.map { it.replace("%rank%", rank.displayName).colorHex }
        }

        item.itemMeta = meta

        return item

    }

}