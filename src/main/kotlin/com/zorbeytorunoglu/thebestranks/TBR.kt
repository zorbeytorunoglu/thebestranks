package com.zorbeytorunoglu.thebestranks

import com.zorbeytorunoglu.thebestranks.commands.CmdRank
import com.zorbeytorunoglu.thebestranks.configuration.Resource
import com.zorbeytorunoglu.thebestranks.configuration.messages.MessageContainer
import com.zorbeytorunoglu.thebestranks.configuration.messages.MessageHandler
import com.zorbeytorunoglu.thebestranks.configuration.ranks.Rank
import com.zorbeytorunoglu.thebestranks.configuration.settings.SettingsContainer
import com.zorbeytorunoglu.thebestranks.configuration.settings.SettingsHandler
import com.zorbeytorunoglu.thebestranks.listeners.Chat
import com.zorbeytorunoglu.thebestranks.utils.RankUtils
import com.zorbeytorunoglu.thebestranks.utils.Utils
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID
import java.util.logging.Level

class TBR: JavaPlugin() {

    private lateinit var configResource: Resource
    private lateinit var ranksResource: Resource
    private lateinit var dataResource: Resource

    private lateinit var ranks: ArrayList<Rank>

    private lateinit var playerRanks: HashMap<UUID, Rank>

    private lateinit var settingsHandler: SettingsHandler

    private lateinit var messageHandler: MessageHandler

    private var permissionHook: Permission? = null

    override fun onEnable() {

        configResource=Resource(this,"config.yml")
        configResource.load()
        ranksResource=Resource(this,"ranks.yml")
        ranksResource.load()
        dataResource= Resource(this,"data.yml")
        dataResource.load()

        ranks=Rank.loadRanks(ranksResource)

        settingsHandler= SettingsHandler(SettingsContainer(configResource))

        messageHandler= MessageHandler(MessageContainer(configResource))

        if (settingsHandler.getChatFormatEnabled()) {

            if (!setupPermissions()) {
                logger.log(Level.WARNING,"[TheBestRanks] Vault or a proper permission system could not be found. Chat format system won't work")
            } else {
                Bukkit.getServer().pluginManager.registerEvents(Chat(this),this)
            }

        }

        getCommand("rank").executor = CmdRank(this)

    }

    override fun onDisable() {

    }

    fun getPermissionHook(): Permission? {
        return permissionHook
    }

    fun getRanks(): ArrayList<Rank> {
        return ranks
    }

    fun getPlayerRanks(): HashMap<UUID, Rank> {
        return playerRanks
    }

    fun getSettingsHandler(): SettingsHandler {
        return settingsHandler
    }

    fun getMessageHandler(): MessageHandler {
        return messageHandler
    }

    fun getConfigResource(): Resource {
        return configResource
    }

    fun getDataResource(): Resource {
        return dataResource
    }

    fun getRanksResource(): Resource {
        return ranksResource
    }

    private fun setupPermissions(): Boolean {
        if (Bukkit.getServer().pluginManager.getPlugin("Vault")==null) return false
        val rsp = Bukkit.getServer().servicesManager.getRegistration(
            Permission::class.java)
        permissionHook = rsp.provider
        return permissionHook != null
    }

}