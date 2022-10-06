package com.zorbeytorunoglu.thebestranks.configuration.menu

import com.zorbeytorunoglu.thebestranks.TBR
import com.zorbeytorunoglu.thebestranks.configuration.Resource
import com.zorbeytorunoglu.thebestranks.configuration.ranks.Rank
import com.zorbeytorunoglu.thebestranks.configuration.ranks.Requirement
import com.zorbeytorunoglu.thebestranks.utils.StringUtils
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class Menu {

    private val plugin: TBR
    private val enabled: Boolean
    private val title: String
    private val openSound: Sound?
    private val closeSound: Sound?
    private val lockedItem: ItemStack
    private val currentItem: ItemStack
    private val inProgressItem: ItemStack
    private val passedItem: ItemStack
    private val size: Int

    constructor(plugin: TBR, enabled: Boolean, title: String,
                openSound: Sound?, closeSound: Sound?, lockedItem: ItemStack,
                currentItem: ItemStack, inProgressItem: ItemStack,
                passedItem: ItemStack, size: Int) {
        this.plugin=plugin
        this.enabled=enabled
        this.title=title
        this.openSound=openSound
        this.closeSound=closeSound
        this.lockedItem=lockedItem
        this.currentItem=currentItem
        this.inProgressItem=inProgressItem
        this.passedItem=passedItem
        this.size=size

    }

    fun getEnabled(): Boolean {
        return enabled
    }

    fun getTitle(): String {
        return title
    }

    fun getOpenSound(): Sound? {
        return openSound
    }

    fun getCloseSound(): Sound? {
        return closeSound
    }

    fun getLockedItem(): ItemStack {
        return lockedItem
    }

    fun getCurrentItem(): ItemStack {
        return currentItem
    }

    fun getInProgressItem(): ItemStack {
        return inProgressItem
    }

    fun getPassedItem(): ItemStack {
        return passedItem
    }

    fun getSize(): Int {
        return size
    }

    fun createInventory(player: Player): Inventory {

        val inventory: Inventory= Bukkit.createInventory(player,getSize(),getTitle())

        for (i: Int in 0 until plugin.getRanks().size) {

            if (plugin.getUtils().getRankUtils().rankPassed(player,plugin.getRanks()[i])) {
                inventory.setItem(i,replaceItemMeta(getPassedItem(), plugin.getRanks()[i]))
            } else {
                if (plugin.getUtils().getRankUtils().getRank(player.uniqueId).getId() == plugin.getRanks()[i].getId()) {
                    inventory.setItem(i,replaceItemMeta(getCurrentItem(), plugin.getRanks()[i]))
                } else {

                    if (plugin.getUtils().getRankUtils().nextRankExists(plugin.getUtils().getRankUtils().getRank(player)) &&
                            plugin.getUtils().getRankUtils().getNextRank(plugin.getUtils().getRankUtils().getRank(player)).getId() ==
                            plugin.getRanks()[i].getId()) {
                        inventory.setItem(i, replaceInProgressItem(getInProgressItem(),plugin.getRanks()[i],player))
                    } else {
                        inventory.setItem(i, replaceItemMeta(getLockedItem(),plugin.getRanks()[i]))
                    }

                }

            }

        }

        return inventory

    }

    private fun replaceItemMeta(itemStack: ItemStack, rank: Rank): ItemStack {

        val item = ItemStack(itemStack)
        val itemMeta: ItemMeta = item.itemMeta

        itemMeta.displayName=itemMeta.displayName.replace("%rank%", StringUtils.hex(rank.getPrefix()))

        val lore: ArrayList<String> = ArrayList()

        for (line in itemMeta.lore) {
            lore.add(line.replace("%rank%", StringUtils.hex(rank.getPrefix())))
        }

        itemMeta.lore = lore

        item.itemMeta = itemMeta

        return item

    }

    private fun replaceInProgressItem(itemStack: ItemStack, rank: Rank, player: Player): ItemStack {

        val item = ItemStack(itemStack)
        val itemMeta: ItemMeta = item.itemMeta

        itemMeta.displayName=itemMeta.displayName.replace("%rank%", StringUtils.hex(rank.getPrefix()))

        val lore: ArrayList<String> = ArrayList()

        for (line in rank.getGuiLore()) {
            var newLine: String = line
            if (line.contains("%requirement_")) {

                val requirementNo: Int = StringUtils.getNumberFromString(
                    org.apache.commons.lang.StringUtils.substringBetween(line,"%", "%")
                )

                val requirement: Requirement = rank.getRequirements()[requirementNo]

                var guiMessage: String = requirement.getGuiMessage()

                guiMessage = if (plugin.getUtils().getRankUtils().requirementFulfilled(player,requirement)) {
                    guiMessage.replace("%status%", plugin.getMessageHandler().getStatusDone())
                } else {
                    guiMessage.replace("%status%", plugin.getMessageHandler().getStatusNotDone())
                }

                newLine=line.replace("%requirement_$requirementNo%", guiMessage.replace("%your%",
                PlaceholderAPI.setPlaceholders(player,requirement.getPlaceholder())))

            }
            lore.add(StringUtils.hex(newLine))
        }

        itemMeta.lore = lore

        item.itemMeta = itemMeta

        return item

    }

    companion object {

        fun loadMenu(plugin: TBR, menuResource: Resource): Menu {

            val enabled: Boolean = menuResource.getBoolean("enabled")
            val title: String = StringUtils.hex(menuResource.getString("title"))

            val openSound: Sound? =
                if (menuResource.getString("open-sound")=="none") null
                else Sound.valueOf(
                    menuResource.getString("open-sound"))

            val closeSound: Sound? =
                if (menuResource.getString("close-sound")=="none") null
                else Sound.valueOf(
                    menuResource.getString("close-sound"))

            val lockedItem: ItemStack = loadItem("locked-item", menuResource)
            val currentItem: ItemStack = loadItem("current-item", menuResource)
            val inProgressItem: ItemStack = loadItem("in-progress-item", menuResource)
            val passedItem: ItemStack = loadItem("passed-item", menuResource)

            val size: Int = getSize(plugin.getRanks())

            return Menu(plugin, enabled, title, openSound, closeSound, lockedItem, currentItem,
                inProgressItem, passedItem, size)

        }

        private fun loadItem(path: String, menuResource: Resource): ItemStack {

            val itemStack: ItemStack = ItemStack(Material.valueOf(menuResource.getString("$path.material")))

            val itemMeta: ItemMeta = itemStack.itemMeta

            itemMeta.displayName = StringUtils.hex(menuResource.getString("$path.display_name"))

            val coloredLore: ArrayList<String> = ArrayList()

            if (menuResource.isList("$path.lore")) {
                val lore: List<String> = menuResource.getStringList("$path.lore")
                for (line in lore) coloredLore.add(StringUtils.hex(line))
            }

            itemMeta.lore = coloredLore

            itemStack.itemMeta = itemMeta

            return itemStack

        }

        private fun getSize(ranks: ArrayList<Rank>): Int {

            return if (ranks.size<=9) {
                9
            } else if (ranks.size<=18) {
                18
            } else if (ranks.size<=27) {
                27
            } else if (ranks.size<=36) {
                36
            } else if (ranks.size<=45) {
                45
            } else {
                54
            }

        }

    }

}