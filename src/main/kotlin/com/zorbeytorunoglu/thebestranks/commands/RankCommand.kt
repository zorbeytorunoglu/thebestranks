package com.zorbeytorunoglu.thebestranks.commands

import com.zorbeytorunoglu.kLib.configuration.createYamlResource
import com.zorbeytorunoglu.thebestranks.TBR
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RankCommand(private val plugin: TBR): CommandExecutor {

    init {
        plugin.getCommand("rank").executor = this
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (command.name != "rank") return false

        if (args.isEmpty()) {

            if (sender !is Player) {
                sender.sendMessage(plugin.messages.adminHelp.joinToString("\n"))
                return false
            }

            if (!sender.hasPermission("thebestranks.rank")) {
                sender.sendMessage(plugin.messages.noPerm)
                return false
            }

            if (!plugin.menuManager.config.customDesignEnabled)
                sender.openInventory(plugin.menuManager.createInventory(sender).pages[0])
            else
                sender.openInventory(plugin.menuManager.createCustomInventory(sender).pages[0])


            return true

        }

        if (args[0] == "set") {

            if (!sender.hasPermission("thebestranks.set")) {
                sender.sendMessage(plugin.messages.noPerm)
                return false
            }

            if (args.size != 3) {
                sender.sendMessage(plugin.messages.setUsage)
                return false
            }

            val player = plugin.server.getPlayer(args[1]) ?: run {
                sender.sendMessage(plugin.messages.playerNotFound)
                return false
            }

            val rank = plugin.rankManager.getRank(args[2]) ?: run {
                sender.sendMessage(plugin.messages.rankNotFound)
                return false
            }

            plugin.rankManager.playerRanks[player.uniqueId.toString()] = rank

            sender.sendMessage(plugin.messages.rankSet
                .replace("%player%", player.name)
                .replace("%rank%", rank.displayName))

            return true

        } else if (args[0] == "check") {

            if (!sender.hasPermission("thebestranks.check")) {
                sender.sendMessage(plugin.messages.noPerm)
                return false
            }

            if (args.size != 2) {
                sender.sendMessage(plugin.messages.checkUsage)
                return false
            }

            val player = plugin.server.getPlayer(args[1]) ?: run {
                sender.sendMessage(plugin.messages.playerNotFound)
                return false
            }

            val rank = plugin.rankManager.getRank(player) ?: run {
                sender.sendMessage(plugin.messages.hasNoRank)
                return false
            }

            sender.sendMessage(plugin.messages.check.replace("%player%", player.name)
                .replace("%rank%", rank.displayName))

            return true

        } else if (args[0] == "help") {

            if (!sender.hasPermission("thebestranks.admin")) {
                sender.sendMessage(plugin.messages.playerHelp.joinToString("\n"))
            } else {
                sender.sendMessage(plugin.messages.adminHelp.joinToString("\n"))
            }

            return true

        } else if (args[0] == "up") {

            if (!sender.hasPermission("thebestranks.rankup") || !sender.hasPermission("thebestranks.rank")) {
                sender.sendMessage(plugin.messages.noPerm)
                return false
            }

            if (sender !is Player) {
                sender.sendMessage(plugin.messages.onlyPlayers)
                return false
            }

            val nextRank = plugin.rankManager.getNextRank(sender) ?: run {
                sender.sendMessage(plugin.messages.noNextRank)
                return false
            }

            plugin.rankManager.rankUp(sender, nextRank)

            return true

        } else if (args[0] == "reload") {

            if (!sender.hasPermission("thebestranks.reload")) {
                sender.sendMessage(plugin.messages.noPerm)
                return false
            }

            plugin.rankManager.ranks.clear()

            plugin.rankManager.loadRanks(plugin.createYamlResource("ranks.yml").load())

            sender.sendMessage(plugin.messages.reloaded)

            return true

        } else {

            if (!sender.hasPermission("thebestranks.admin")) {
                sender.sendMessage(plugin.messages.playerHelp.joinToString("\n"))
            } else {
                sender.sendMessage(plugin.messages.adminHelp.joinToString("\n"))
            }

        }

        return false

    }

}