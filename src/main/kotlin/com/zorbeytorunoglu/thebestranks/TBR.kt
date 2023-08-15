package com.zorbeytorunoglu.thebestranks

import com.zorbeytorunoglu.kLib.MCPlugin
import com.zorbeytorunoglu.kLib.configuration.createYamlResource
import com.zorbeytorunoglu.kLib.extensions.registerEvents
import com.zorbeytorunoglu.kLib.extensions.severe
import com.zorbeytorunoglu.thebestranks.api.TBRAPI
import com.zorbeytorunoglu.thebestranks.commands.RankCommand
import com.zorbeytorunoglu.thebestranks.config.Config
import com.zorbeytorunoglu.thebestranks.hooks.PAPI
import com.zorbeytorunoglu.thebestranks.listeners.OnJoin
import com.zorbeytorunoglu.thebestranks.listeners.OnMenu
import com.zorbeytorunoglu.thebestranks.menu.MenuManager
import com.zorbeytorunoglu.thebestranks.messages.Messages
import com.zorbeytorunoglu.thebestranks.rank.RankManager

class TBR: MCPlugin() {

    lateinit var rankManager: RankManager
    lateinit var menuManager: MenuManager

    lateinit var config: Config
    lateinit var messages: Messages

    override fun onEnable() {

        if (server.pluginManager.getPlugin("PlaceholderAPI") == null) {
            logger.severe("PlaceholderAPI is needed for thebestranks to run! Disabling the plugin...")
            this.isEnabled = false
            return
        }

        config = Config(createYamlResource("config.yml").load())
        messages = Messages(createYamlResource("messages.yml").load())

        rankManager = RankManager(this)
        menuManager = MenuManager(this)

        RankCommand(this)

        registerEvents(OnJoin(this), OnMenu(this))

        PAPI(this).register()

        rankManager.loadPlayerRanks(createYamlResource("save.yml").load())

    }

    override fun onDisable() {

        rankManager.savePlayerRanks(createYamlResource("save.yml").load())

    }

    override fun onLoad() {

        try {
            TBRAPI.setPluginInstance(this)
        } catch (error: UnsupportedOperationException) {
            severe("The API instance was already initialized." +
                    " This can be caused by a reload or another plugin initializing it.")
        }

    }

}