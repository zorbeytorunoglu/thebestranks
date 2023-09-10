package com.zorbeytorunoglu.thebestranks.menu

import com.zorbeytorunoglu.kLib.configuration.Resource
import com.zorbeytorunoglu.kLib.extensions.colorHex
import com.zorbeytorunoglu.kLib.version.Version
import com.zorbeytorunoglu.kLib.version.VersionEnum
import com.zorbeytorunoglu.thebestranks.TBR
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CustomDesign(private val plugin: TBR, private val file: Resource) {

    private val designList = file.getStringList("custom_design.design")

    val invSize: Int = if (designList.size < 1 || designList.size > 6) {
        plugin.logger.severe("Custom design's rows can not be lower than 1 or it can not be lower than 6! Using 2 as default.")
        2 * 9
    } else {
        designList.size * 9
    }

    private val codes = loadCodes()

    val slots = loadSlots()

    private fun loadCodes(): List<DesignCode> {

        val path = "custom_design.design_codes"

        val keySet = file.getConfigurationSection(path).getKeys(false)

        val codes = arrayListOf<DesignCode>()

        for (key in keySet) {

            val char = key.singleOrNull()

            if (char == null) {
                plugin.logger.severe("Design code of $key could not be casted to char! Use only single letter or a symbol! It will be skipped!")
                continue
            }

            val functionType: FunctionType? = file.getString("$path.$char.function")?.let {
                when (it) {
                    "item" -> FunctionType.ITEM
                    "next" -> FunctionType.NEXT_PAGE
                    "previous" -> FunctionType.PREVIOUS_PAGE
                    "rank" -> FunctionType.RANK
                    else -> null

                }
            }

            if (functionType == null) {
                plugin.logger.severe("We do not recognize this function type in design_codes.$char! Skipping this one! Your function options: item/rank/next/previous")
                continue
            }

            val item = if (functionType == FunctionType.ITEM)
                getItem(file, "$path.$char")
            else null

            codes.add(DesignCode(char, functionType, item))

        }

        return codes

    }

    private fun loadSlots(): ArrayList<DesignCode?> {

        val designCodes: ArrayList<DesignCode?> = arrayListOf()

        val codesArray = designList.joinToString("").toCharArray()

        for (char in codesArray) {

            val designCode = codes.find { it.char == char }

            if (designCode == null) {
                plugin.logger.severe("'$char' code used in 'design' could not be found in 'design_codes'! Skipping this one!")
                designCodes.add(null)
            } else {
                designCodes.add(designCode)
            }

        }

        return designCodes

    }

    private fun getItem(file: Resource, path: String): ItemStack {

        val material = try {
            Material.valueOf(file.getString("$path.material"))
        } catch (e: IllegalArgumentException) {
            plugin.logger.severe("Material of the item in '$path' is not valid! It will be replaced with ANVIL! You need to fix this!")
            Material.ANVIL
        }

        val data = file.getString("$path.data").toShortOrNull() ?: run {
            plugin.logger.severe("Data value of the item in '$path' is not valid! It will be replaced with 0! You need to fix this!")
            0
        }

        val item = if (Version.getVersion().isPre(VersionEnum.V1_13_R1)) ItemStack(material, 1, data) else ItemStack(material, 1)

        val meta = if (item.hasItemMeta()) item.itemMeta else plugin.server.itemFactory.getItemMeta(item.type)

        file.getString("$path.name")?.also {
            meta.displayName = it.colorHex
        } ?: run { plugin.logger.severe("Name of the item in '$path' is not valid! You need to fix this!") }

        file.getStringList("$path.lore")?.also {
            meta.lore = it.map { line -> line.colorHex }
        } ?: run { plugin.logger.severe("Lore of the item in '$path' is not valid! You need to fix this!") }

        item.itemMeta = meta

        return item

    }

}
