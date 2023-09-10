package com.zorbeytorunoglu.thebestranks.menu

import com.zorbeytorunoglu.kLib.configuration.Resource
import com.zorbeytorunoglu.kLib.extensions.colorHex
import com.zorbeytorunoglu.kLib.extensions.severe
import com.zorbeytorunoglu.kLib.version.Version
import com.zorbeytorunoglu.kLib.version.VersionEnum
import com.zorbeytorunoglu.thebestranks.TBR
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MenuConfig(private val plugin: TBR, private val file: Resource) {

    val customDesignEnabled = file.getBoolean("custom_design.enabled")

    val name: String = file.getString("name")?.colorHex ?: run {
        plugin.severe("Menu name is not set, default value will be used.")
        "&e&lRANKS &7Page &r%page%&7/&r%total_pages%".colorHex
    }

    val size: Int = file.getString("size").toIntOrNull()?.let {
        if (it <= 9) {
            plugin.severe("Menu size must be at least 18! Default value will be used.")
            18
        } else {
            it
        }
    } ?: run {
        plugin.severe("Menu size is not set properly! Default value will be used.")
        54
    }

    val passedItem: ItemStack = getItem("passed")

    val inProgressItem: ItemStack = getItem("in_progress")

    val currentItem: ItemStack = getItem("current")

    val lockedItem: ItemStack = getItem("locked")

    val nextPageItem: ItemStack = getItem("next")

    val previousPageItem: ItemStack = getItem("previous")

    val statusDone: String = file.getString("done_status")?.colorHex ?: "&a&l✔".colorHex

    val statusNotDone: String = file.getString("not_done_status")?.colorHex ?: "&c&l✘".colorHex

    private fun getItem(key: String): ItemStack {

        val name = file.getString("$key.name")?.colorHex ?: run {
            plugin.severe("Item name of $key is not set properly!")
            "&cNot set properly! Check your menu.yml"
        }

        val material = try {
            Material.valueOf(file.getString("$key.material"))
        } catch (e: Exception) {
            plugin.severe("Material of $key is not set properly! We will replace it with ANVIL")
            Material.ANVIL
        }

        val data = file.getString("$key.data").toShortOrNull() ?: "0".toShort()

        val lore: List<String> = file.getStringList("$key.lore")?.map {
            it.colorHex
        } ?: emptyList()

        val item = if (Version.getVersion().isPre(VersionEnum.V1_13_R1)) ItemStack(material, 1, data) else ItemStack(material, 1)

        val meta = if (item.hasItemMeta()) item.itemMeta else plugin.server.itemFactory.getItemMeta(item.type)

        meta.displayName = name
        meta.lore = lore

        item.itemMeta = meta

        return item

    }

}