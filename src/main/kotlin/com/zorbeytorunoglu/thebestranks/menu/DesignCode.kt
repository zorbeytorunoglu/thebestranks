package com.zorbeytorunoglu.thebestranks.menu

import org.bukkit.inventory.ItemStack

data class DesignCode(
    val char: Char,
    val functionType: FunctionType,
    val item: ItemStack? = null
)
