package com.zorbeytorunoglu.thebestranks.commands

import com.zorbeytorunoglu.thebestranks.TBR
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CmdRank(private val plugin: TBR): CommandExecutor {

    override fun onCommand(
        sender: CommandSender?,
        command: Command?,
        label: String?,
        args: Array<out String>?
    ): Boolean {

        if (command!!.name.equals("rank",true)) {



        }

        return false

    }


}