package com.zorbeytorunoglu.thebestranks.commands

import com.zorbeytorunoglu.thebestranks.TBR
import com.zorbeytorunoglu.thebestranks.configuration.ranks.Rank
import com.zorbeytorunoglu.thebestranks.utils.StringUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CmdRank(private val plugin: TBR): CommandExecutor {

    override fun onCommand(
        sender: CommandSender?,
        command: Command?,
        label: String?,
        args: Array<out String>?): Boolean {

        if (command==null || sender==null || args==null) return false

        if (command.name.equals("rank",true)) {

            if (!sender.hasPermission("thebestranks.rank")) {
                sender.sendMessage(plugin.getMessageHandler().getNoPerm())
                return false
            }

            if (args.isEmpty()) {

                if (sender !is Player) {
                    sender.sendMessage(plugin.getMessageHandler().getOnlyInGame())
                    return false
                } else {

                    val player: Player = sender

                    return if (plugin.getMenu().getEnabled()) {

                        if (plugin.getMenu().getOpenSound()!=null) {
                            player.playSound(player.location,plugin.getMenu().getOpenSound(),1.0F, 1.0F)
                        }

                        player.openInventory(plugin.getMenu().createInventory(player))

                        true

                    } else {

                        sender.sendMessage(plugin.getMessageHandler().getYourRank().replace("%rank%",
                            StringUtils.hex(plugin.getUtils().getRankUtils().getRank(player).getPrefix())))

                        true
                    }

                }

            }

            if (args[0].equals("up",true)) {

                if (!sender.hasPermission("thebestranks.rank")) {
                    sender.sendMessage(plugin.getMessageHandler().getNoPerm())
                    return false
                }

                if (sender !is Player) {
                    sender.sendMessage(plugin.getMessageHandler().getOnlyInGame())
                    return false
                }

                val player: Player = sender

                val rank: Rank = plugin.getUtils().getRankUtils().getRank(player)

                if (!plugin.getUtils().getRankUtils().nextRankExists(rank)) {
                    player.sendMessage(plugin.getMessageHandler().getNoGreaterRank())
                    return false
                }

                val nextRank: Rank = plugin.getUtils().getRankUtils().getNextRank(rank)

                var canRankUp: Boolean = true

                for (requirement in nextRank.getRequirements()) {

                    if (!plugin.getUtils().getRankUtils().requirementFulfilled(player,requirement)) {

                        player.sendMessage(StringUtils.hex(requirement.getDenyMessage()))

                        canRankUp = false

                    }

                }

                if (!canRankUp) return false

                plugin.getUtils().getRankUtils().rankUp(player)

                plugin.getUtils().getRankUtils().submitCommands(player, nextRank)

                player.sendMessage(plugin.getMessageHandler().getRankUp().replace("%next_rank%", StringUtils.hex(plugin.getUtils().getRankUtils().getNextRank(rank).getPrefix()))
                    .replace("%rank%", StringUtils.hex(rank.getPrefix())))

                return true

            } else if (args[0].equals("set", true)) {

                if (!sender.hasPermission("thebestranks.setrank")) {
                    sender.sendMessage(plugin.getMessageHandler().getNoPerm())
                    return false
                }

                if (args.size!=3) {
                    sender.sendMessage(plugin.getMessageHandler().getRankSetUsage())
                    return false
                }

                if (Bukkit.getServer().getPlayer(args[1])==null) {
                    sender.sendMessage(plugin.getMessageHandler().getPlayerNotFound())
                    return false
                }

                if (!plugin.getUtils().getRankUtils().rankExists(args[2])) {
                    sender.sendMessage(plugin.getMessageHandler().getRankNotFound())
                    return false
                }

                val player: Player = Bukkit.getServer().getPlayer(args[1])

                val rank: Rank = plugin.getUtils().getRankUtils().getRank(args[2])!!

                plugin.getUtils().getRankUtils().setRank(rank, player)

                sender.sendMessage(plugin.getMessageHandler().getRankSet().replace("%player%", player.name)
                    .replace("%rank%", StringUtils.hex(rank.getPrefix())))

                return true

            } else if (args[0].equals("help", true)) {

                if (sender !is Player || sender.hasPermission("thebestranks.setrank")) {
                    for (line in plugin.getMessageHandler().getHelpAdmin()) {
                        sender.sendMessage(StringUtils.hex(line))
                    }
                    return true
                }

                return if (sender.hasPermission("thebestranks.rank")) {
                    for (line in plugin.getMessageHandler().getHelpPlayers()) {
                        sender.sendMessage(StringUtils.hex(line))
                    }
                    true
                } else {
                    sender.sendMessage(plugin.getMessageHandler().getNoPerm())
                    false
                }

            } else {

                if (Bukkit.getServer().getPlayer(args[0])!=null) {
                    if (!sender.hasPermission("thebestranks.check")) {
                        sender.sendMessage(plugin.getMessageHandler().getNoPerm())
                        return false
                    }

                    val player: Player = Bukkit.getServer().getPlayer(args[0])

                    sender.sendMessage(plugin.getMessageHandler().getCheckRank()
                        .replace("%rank%", StringUtils.hex(plugin.getUtils().getRankUtils().getRank(player.uniqueId).getPrefix()))
                        .replace("%player%", player.name))

                    return true

                }

            }

            sender.sendMessage(plugin.getMessageHandler().getUnknownArg())

        }

        return false

    }


}