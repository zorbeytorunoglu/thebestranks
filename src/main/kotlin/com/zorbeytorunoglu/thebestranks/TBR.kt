package com.zorbeytorunoglu.thebestranks

import com.zorbeytorunoglu.thebestranks.commands.CmdRank
import com.zorbeytorunoglu.thebestranks.configuration.Resource
import com.zorbeytorunoglu.thebestranks.configuration.menu.Menu
import com.zorbeytorunoglu.thebestranks.configuration.messages.MessageContainer
import com.zorbeytorunoglu.thebestranks.configuration.messages.MessageHandler
import com.zorbeytorunoglu.thebestranks.configuration.ranks.Rank
import com.zorbeytorunoglu.thebestranks.configuration.settings.SettingsContainer
import com.zorbeytorunoglu.thebestranks.configuration.settings.SettingsHandler
import com.zorbeytorunoglu.thebestranks.databases.Sql
import com.zorbeytorunoglu.thebestranks.hooks.PAPI
import com.zorbeytorunoglu.thebestranks.listeners.Chat
import com.zorbeytorunoglu.thebestranks.listeners.Click
import com.zorbeytorunoglu.thebestranks.listeners.Join
import com.zorbeytorunoglu.thebestranks.utils.Utils
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.sql.ResultSet
import java.util.*
import java.util.logging.Level

class TBR: JavaPlugin() {

    private lateinit var configResource: Resource
    private lateinit var ranksResource: Resource
    private lateinit var dataResource: Resource
    private lateinit var menuResource: Resource

    private lateinit var ranks: ArrayList<Rank>

    private var playerRanks: HashMap<UUID, Rank> = HashMap()

    private lateinit var settingsHandler: SettingsHandler
    private lateinit var messageHandler: MessageHandler

    private lateinit var menu: Menu

    private var permissionHook: Permission? = null

    private lateinit var utils: Utils

    private lateinit var mysql: Sql

    override fun onEnable() {

        loadFiles()

        if (Bukkit.getServer().pluginManager.getPlugin("PlaceholderAPI")==null) {
            logger.log(Level.SEVERE, "[TheBestRanks] Plugin can't work without PlaceholderAPI.")
            Bukkit.getServer().pluginManager.disablePlugin(this)
            return
        } else {
            PAPI(this).register()
        }

        if (settingsHandler.getChatFormatEnabled()) {

            if (!setupPermissions()) {
                logger.log(Level.WARNING,"[TheBestRanks] Vault or/and a proper permission system could not be found. Chat format system won't work")
            } else {
                Bukkit.getServer().pluginManager.registerEvents(Chat(this),this)
            }

        }

        Bukkit.getServer().pluginManager.registerEvents(Join(this),this)
        Bukkit.getServer().pluginManager.registerEvents(Click(this),this)

        utils=Utils(this)

        getCommand("rank").executor = CmdRank(this)

        mysql= Sql(this)

        loadPlayerRanks(playerRanks)

    }

    override fun onDisable() {

        savePlayerRanks(getDataResource(),getPlayerRanks())

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

    fun getMenuResource(): Resource {
        return menuResource
    }

    fun getMenu(): Menu {
        return menu
    }

    fun getUtils(): Utils {
        return utils
    }

    fun getMysql(): Sql {
        return mysql
    }

    private fun setupPermissions(): Boolean {
        if (Bukkit.getServer().pluginManager.getPlugin("Vault")==null) return false
        val rsp = Bukkit.getServer().servicesManager.getRegistration(
            Permission::class.java)
        permissionHook = rsp.provider
        return permissionHook != null
    }

    private fun savePlayerRanks(dataResource: Resource, playerRanks: HashMap<UUID, Rank>) {

        if (getPlayerRanks().isEmpty()) return

        if (getSettingsHandler().getSqlEnabled()) {

            getMysql().connect()

            if (getMysql().getConnection()==null) {
                logger.log(Level.SEVERE, "[TheBestRanks] Player ranks could not be saved because there is something wrong with the database")
                return
            }

            if (!getMysql().tableExists(getSettingsHandler().getSqlRanksTableName(), false)) {
                logger.log(Level.WARNING, "[TheBestRanks] The table in the database could not be found. Creating one.")
                if (!getMysql().createTable(getSettingsHandler().getSqlRanksTableName(),getSettingsHandler().getSqlRanksUUIDColumn(),
                    getSettingsHandler().getSqlRanksRankColumn(),false)) {
                    logger.log(Level.SEVERE, "[TheBestRanks] Table could not be created. Since the database system won't work now," +
                            "we are disabling the plugin.")
                    Bukkit.getServer().pluginManager.disablePlugin(this)
                } else {
                    logger.log(Level.INFO, "[TheBestRanks] Table created")
                }
            }

            var saved: Int = 0

            for (uuid in playerRanks.keys) {
                getMysql().savePlayerRank(getSettingsHandler().getSqlRanksTableName(),getSettingsHandler().getSqlRanksUUIDColumn(),
                getSettingsHandler().getSqlRanksRankColumn(), uuid.toString(), playerRanks[uuid]!!.getId(), false)
                saved++
            }

            logger.log(Level.INFO, "[TheBestRanks] $saved player data is saved to database")

        }

        for (uuid in getPlayerRanks().keys) {

            dataResource.set("ranks.$uuid", getPlayerRanks()[uuid]!!.getId())

        }

        dataResource.save()

    }

    private fun loadPlayerRanks(playerRanks: HashMap<UUID, Rank>) {

        if (getSettingsHandler().getSqlEnabled()) {

            getMysql().connect()

            if (!getMysql().isConnected()) {
                logger.log(Level.SEVERE, "[TheBestRanks] MySQL is enabled but couldn't connect. Plugin will be disabled.")
                Bukkit.getServer().pluginManager.disablePlugin(this)
                return
            }

            logger.log(Level.INFO, "[TheBestRanks] Successfully connected to the database")

            if (!getMysql().tableExists(getSettingsHandler().getSqlRanksTableName(),false)) return

            val statement = getMysql().getConnection()!!.createStatement()

            val resultSet: ResultSet = statement.executeQuery("select "+getSettingsHandler().getSqlRanksUUIDColumn()+", "+
                    getSettingsHandler().getSqlRanksRankColumn()+" from "+getSettingsHandler().getSqlRanksTableName())

            var amount: Int = 0
            while (resultSet.next()) {
                if (getUtils().getRankUtils().getRank(resultSet.getString("rank"))!=null) {
                    playerRanks[UUID.fromString(resultSet.getString("uuid"))] =
                        getUtils().getRankUtils().getRank(resultSet.getString("rank"))!!
                    amount++
                }
            }

            logger.log(Level.INFO, "[TheBestRanks] $amount player data loaded from database")

            resultSet.close()

            getMysql().getConnection()!!.close()

            return

        } else {

            if (!getDataResource().getFile().exists()) return

            if (!getDataResource().contains("ranks")) return

            for (key in getDataResource().getConfigurationSection("ranks").getKeys(false)) {

                for (rank in getRanks()) {
                    if (rank.getId() == getDataResource().getString("ranks.$key")) {
                        playerRanks[UUID.fromString(key)] = rank
                        break
                    }
                }

            }
        }

    }

    fun loadFiles() {
        configResource=Resource(this,"config.yml")
        configResource.load()
        ranksResource=Resource(this,"ranks.yml")
        ranksResource.load()
        dataResource= Resource(this,"data.yml")
        dataResource.load()
        menuResource= Resource(this,"menu.yml")
        menuResource.load()

        ranks=Rank.loadRanks(ranksResource)

        settingsHandler= SettingsHandler(SettingsContainer(configResource))

        messageHandler= MessageHandler(MessageContainer(configResource))

        menu = Menu.loadMenu(this,menuResource)

    }

    fun reloadPlayerRanks() {
        if (playerRanks.isEmpty()) return
        val reloadedHash: HashMap<UUID, Rank> = hashMapOf()
        val ranksIdList: ArrayList<String> = arrayListOf()

        ranks.stream().forEach { ranksIdList.add(it.getId()) }

        for (key in playerRanks.keys) {
            for (rank in ranks) {

                if (!ranksIdList.contains(playerRanks[key]!!.getId())) {
                    if (utils.getRankUtils().getFirstRank() == null) continue
                    reloadedHash[key] = utils.getRankUtils().getFirstRank()!!
                }

                if (playerRanks[key]!!.getId() == rank.getId()) reloadedHash[key] = rank

            }
        }

        playerRanks = reloadedHash

    }

}