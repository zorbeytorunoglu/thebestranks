package com.zorbeytorunoglu.thebestranks.rank

import com.zorbeytorunoglu.kLib.configuration.Resource
import com.zorbeytorunoglu.kLib.configuration.createYamlResource
import com.zorbeytorunoglu.kLib.extensions.colorHex
import com.zorbeytorunoglu.kLib.extensions.info
import com.zorbeytorunoglu.thebestranks.TBR
import com.zorbeytorunoglu.thebestranks.events.RankUpEvent
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import java.util.UUID

class RankManager(private val plugin: TBR) {

    val ranks: ArrayList<Rank> = arrayListOf()
    val playerRanks = HashMap<String, Rank>()

    init {
        loadRanks(plugin.createYamlResource("ranks.yml").load())
        loadPlayerRanks(plugin.createYamlResource("save.yml").load())
    }

    fun getRank(player: Player): Rank? = playerRanks[player.uniqueId.toString()]

    fun getRank(rankName: String): Rank? = ranks.find { it.name == rankName }

    fun getRank(order: Int): Rank? = ranks.find { it.order == order }

    fun getRank(uuid: UUID): Rank? = playerRanks[uuid.toString()]

    fun getPrefix(rank: Rank): String = rank.prefix

    fun rankExists(name: String): Boolean = ranks.any { it.name == name }

    fun getNextRank(rank: Rank?): Rank? {

        return if (rank != null) {
            ranks.find { it.order == rank.order+1 }
        } else {
            getFirstRank()
        }

    }

    fun getNextRank(player: Player): Rank? {

        val rank = getRank(player)

        return if (rank != null) {
            ranks.find { it.order == rank.order+1 }
        } else {
            getFirstRank()
        }

    }

    fun getFirstRank(): Rank? = ranks.find { it.order == 0 }

    fun requirementMet(player: Player, requirement: Requirement): Boolean {

        val placeholder = PlaceholderAPI.setPlaceholders(player, requirement.placeholder)

        if (requirement.type == RequirementType.STRING) {

            val case = requirement.case ?: RequirementCase.EQUALS

            return if (case == RequirementCase.EQUALS) {
                placeholder == requirement.value
            } else {
                placeholder != requirement.value
            }

        } else {

            val case = requirement.case ?: RequirementCase.GREATER

            castPlaceholderToValueType(requirement.type, placeholder) ?: run {
                plugin.logger.severe("Placeholder ${requirement.placeholder} could not be casted to ${requirement.type}! You need to fix this!")
                return false
            }

            when (requirement.type) {

                RequirementType.INT -> {

                    return when (case) {

                        RequirementCase.GREATER -> placeholder.toInt() > requirement.value as Int
                        RequirementCase.EQUALS -> placeholder.toInt() == requirement.value as Int
                        RequirementCase.LESSER -> placeholder.toInt() < requirement.value as Int
                        RequirementCase.EQUALS_OR_LESSER -> placeholder.toInt() <= requirement.value as Int
                        RequirementCase.NOT_EQUALS -> placeholder.toInt() != requirement.value as Int
                        RequirementCase.EQUALS_OR_GREATER -> placeholder.toInt() >= requirement.value as Int

                    }

                }

                RequirementType.FLOAT -> {

                    return when (case) {

                        RequirementCase.GREATER -> placeholder.toFloat() > requirement.value as Float
                        RequirementCase.EQUALS -> placeholder.toFloat() == requirement.value as Float
                        RequirementCase.LESSER -> placeholder.toFloat() < requirement.value as Float
                        RequirementCase.EQUALS_OR_LESSER -> placeholder.toFloat() <= requirement.value as Float
                        RequirementCase.NOT_EQUALS -> placeholder.toFloat() != requirement.value as Float
                        RequirementCase.EQUALS_OR_GREATER -> placeholder.toFloat() >= requirement.value as Float

                    }

                }

                RequirementType.LONG -> {

                    return when (case) {

                        RequirementCase.GREATER -> placeholder.toLong() > requirement.value as Long
                        RequirementCase.EQUALS -> placeholder.toLong() == requirement.value as Long
                        RequirementCase.LESSER -> placeholder.toLong() < requirement.value as Long
                        RequirementCase.EQUALS_OR_LESSER -> placeholder.toLong() <= requirement.value as Long
                        RequirementCase.NOT_EQUALS -> placeholder.toLong() != requirement.value as Long
                        RequirementCase.EQUALS_OR_GREATER -> placeholder.toLong() >= requirement.value as Long

                    }

                }

                else -> {

                    return when (case) {

                        RequirementCase.GREATER -> placeholder.toDouble() > requirement.value as Double
                        RequirementCase.EQUALS -> placeholder.toDouble() == requirement.value as Double
                        RequirementCase.LESSER -> placeholder.toDouble() < requirement.value as Double
                        RequirementCase.EQUALS_OR_LESSER -> placeholder.toDouble() <= requirement.value as Double
                        RequirementCase.NOT_EQUALS -> placeholder.toDouble() != requirement.value as Double
                        RequirementCase.EQUALS_OR_GREATER -> placeholder.toDouble() >= requirement.value as Double

                    }

                }

            }

        }

    }

    private fun castPlaceholderToValueType(type: RequirementType, placeholder: String): Any? {

        return when (type) {

            RequirementType.STRING -> placeholder
            RequirementType.INT -> placeholder.toIntOrNull()
            RequirementType.DOUBLE -> placeholder.toDoubleOrNull()
            RequirementType.FLOAT -> placeholder.toFloatOrNull()
            RequirementType.LONG -> placeholder.toLongOrNull()

        }

    }

    fun loadRanks(file: Resource) {

        if (!file.file.exists()) return

        for ((index, key) in file.getKeys(false).withIndex()) {

            val prefix = file.getString("$key.prefix")?.colorHex

            if (prefix == null) {
                plugin.logger.severe("Prefix of $key is not set! Skipping...")
                continue
            }

            val displayName = file.getString("$key.display_name")?.colorHex

            if (displayName == null) {
                plugin.logger.severe("Display name of $key is not set! Skipping...")
                continue
            }

            val lore = file.getStringList("$key.lore")?.map { it.colorHex }

            if (lore == null) {
                plugin.logger.severe("Lore of $key is not set! Skipping...")
                continue
            }

            val commands = file.getStringList("$key.commands") ?: null

            val requirements: MutableList<Requirement> = mutableListOf()

            if (file.getConfigurationSection("$key.requirements") != null) {

                for (req in file.getConfigurationSection("$key.requirements").getKeys(false)) {

                    if (!PlaceholderAPI.containsPlaceholders(req)) {
                        plugin.logger.severe("Problem in rank $key: $req is not a valid placeholder! Skipping...")
                        continue
                    }

                    val typeString = file.getString("$key.requirements.$req.type")

                    if (typeString == null) {
                        plugin.logger.severe("Requirement type of $key in $req is not set! Skipping...")
                        continue
                    }

                    val type = try {
                        RequirementType.valueOf(typeString)
                    } catch (e: IllegalArgumentException) {
                        plugin.logger.severe("Requirement type of $key in $req is not set properly! Your options are 'STRING, INT, DOUBLE, LONG, FLOAT'! Skipping...")
                        continue
                    }

                    var case: RequirementCase? = null

                    val caseString = file.getString("$key.requirements.$req.case")

                    if (caseString != null) {

                        case = try {
                            RequirementCase.valueOf(caseString.replace(" ", "_").uppercase())
                        } catch (e: IllegalArgumentException) {
                            plugin.logger.severe("Requirement case of $key in $req is not set properly! Your options are 'equals, not equals, greater, lesser, equals or greater, equals or lesser'. Skipping...")
                            continue
                        }

                    }

                    val value = file.getString("$key.requirements.$req.value")

                    if (value == null) {
                        plugin.logger.severe("Requirement value of $key in $req is not set! Skipping...")
                        continue
                    }

                    val valueType = when (type) {

                        RequirementType.STRING -> value
                        RequirementType.INT -> value.toIntOrNull()
                        RequirementType.DOUBLE -> value.toDoubleOrNull()
                        RequirementType.FLOAT -> value.toFloatOrNull()
                        RequirementType.LONG -> value.toLongOrNull()

                    }

                    if (valueType == null) {
                        plugin.logger.severe("A problem in $key: Value could not be cast to that type for $req. Skipping...")
                        continue
                    }

                    val guiMessage = file.getString("$key.requirements.$req.gui_message")?.colorHex

                    if (guiMessage == null) {
                        plugin.logger.severe("Requirement gui_message of $key in $req is not set! Skipping...")
                        continue
                    }

                    val denyMessage = file.getString("$key.requirements.$req.deny_message")?.colorHex

                    if (denyMessage == null) {
                        plugin.logger.severe("Requirement deny_message of $key in $req is not set! Skipping...")
                        continue
                    }

                    val requirement = Requirement(req, type, valueType, case, guiMessage, denyMessage)

                    requirements.add(requirement)

                }

            }

            val rank = Rank(key,displayName,prefix,index,requirements.toList(),lore,commands)

            ranks.add(rank)

            plugin.info("Rank $key has been loaded successfully!")

        }

    }

    fun loadPlayerRanks(file: Resource) {
        if (!file.file.exists()) return

        file.getKeys(false).forEach { playerKey ->
            val rankName = file.getString(playerKey)
            val rank = rankName?.let { getRank(it) }
            rank?.let { playerRanks[playerKey] = it }
        }
    }

    fun savePlayerRanks(file: Resource) {

        if (playerRanks.isEmpty()) return

        playerRanks.forEach { (key, value) ->
            file.set(key, value.name)
        }

        file.save()

    }

    fun rankPassed(player: Player, playerRank: Rank?, rank: Rank): Boolean {

        if (playerRank == null) return false

        return playerRank.order > rank.order

    }

    fun isCurrentRank(player: Player, playerRank: Rank?, rank: Rank): Boolean {

        if (playerRank == null) return false

        return playerRank.order == rank.order

    }

    fun isInProgress(player: Player, playerRank: Rank?, rank: Rank): Boolean {

        if (playerRank == null && rank == getFirstRank()) return true

        if (getNextRank(playerRank) == rank) return true

        return false

    }

    fun setRank(player: Player, rank: Rank) {
        playerRanks[player.uniqueId.toString()] = rank
    }

    fun rankUp(player: Player, nextRank: Rank) {

        if (nextRank.requirements.isEmpty()) {
            val event = RankUpEvent(player, nextRank)
            plugin.server.pluginManager.callEvent(event)
            if (event.isCancelled) return
            plugin.rankManager.setRank(player, nextRank)
            player.sendMessage(plugin.messages.rankUp.replace("%rank%", nextRank.displayName))
            submitCommands(player, nextRank, event)
            return
        } else {

            var allMet = true

            nextRank.requirements.forEach {

                if (!plugin.rankManager.requirementMet(player, it)) {
                    player.sendMessage(it.denyMessage)
                    allMet = false
                }

            }

            if (allMet) {
                val event = RankUpEvent(player, nextRank)
                plugin.server.pluginManager.callEvent(event)
                if (!event.isCancelled) {
                    setRank(player, nextRank)
                    player.sendMessage(plugin.messages.rankUp.replace("%rank%", nextRank.displayName))
                    submitCommands(player, nextRank, event)
                }
                return
            }

            return

        }

    }

    private fun submitCommands(player: Player, rank: Rank, event: RankUpEvent) {

        if (rank.commands == null) return

        if (!event.getSubmitCommands()) return

        rank.commands.forEach {
            plugin.server.dispatchCommand(plugin.server.consoleSender, it.replace("%player%", player.name))
        }

    }

}