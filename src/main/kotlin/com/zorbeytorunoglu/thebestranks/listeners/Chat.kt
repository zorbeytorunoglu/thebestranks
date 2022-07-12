package com.zorbeytorunoglu.thebestranks.listeners

import com.zorbeytorunoglu.thebestranks.TBR
import com.zorbeytorunoglu.thebestranks.utils.StringUtils
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class Chat(private val plugin: TBR): Listener {

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {

        if (!plugin.getSettingsHandler().getChatFormatEnabled()) return

        var format: String = plugin.getSettingsHandler().getChatFormatDefaultFormat()

        var group: String?=null

        try {
            group=plugin.getPermissionHook()!!.getPrimaryGroup(event.player)
            if (plugin.getSettingsHandler().getChatFormatPerGroupFormats().containsKey(group)) {
                format = plugin.getSettingsHandler().getChatFormatPerGroupFormats()[group]!!
                if (PlaceholderAPI.containsPlaceholders(format)) {
                    format=PlaceholderAPI.setPlaceholders(event.player, format)
                }
                format=format.replace("%message%", event.message)
                event.format=StringUtils.hex(format.toString())
                return
            }
        } catch (e: UnsupportedOperationException) {

        }

        if (PlaceholderAPI.containsPlaceholders(format)) {
            format=PlaceholderAPI.setPlaceholders(event.player, format)
        }
        event.format=format.replace("%message%", event.message)

    }

}