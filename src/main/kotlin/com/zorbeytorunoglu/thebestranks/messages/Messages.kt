package com.zorbeytorunoglu.thebestranks.messages

import com.zorbeytorunoglu.kLib.configuration.Resource
import com.zorbeytorunoglu.kLib.extensions.colorHex

class Messages(file: Resource) {

    val noPerm = file.getString("no_permission")!!.colorHex
    val onlyPlayers = file.getString("only_players")!!.colorHex
    val noRequirementsLore = file.getStringList("no_requirements_lore").map { it.colorHex }
    val adminHelp = file.getStringList("admin_help").map { it.colorHex }
    val playerHelp = file.getStringList("player_help").map { it.colorHex }
    val setUsage = file.getString("set_usage")!!.colorHex
    val playerNotFound = file.getString("player_not_found")!!.colorHex
    val rankNotFound = file.getString("rank_not_found")!!.colorHex
    val rankSet = file.getString("rank_set")!!.colorHex
    val hasNoRank = file.getString("has_no_rank")!!.colorHex
    val check = file.getString("check")!!.colorHex
    val checkUsage = file.getString("check_usage")!!.colorHex
    val noNextRank = file.getString("no_next_rank")!!.colorHex
    val rankUp = file.getString("rank_up")!!.colorHex
    val reloaded = file.getString("reloaded")!!.colorHex

}