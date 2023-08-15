package com.zorbeytorunoglu.thebestranks.menu

import org.bukkit.inventory.Inventory

data class Page(val uuid: String, var currentPage: Int, val pages: List<Inventory>)