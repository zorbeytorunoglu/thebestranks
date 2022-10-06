package com.zorbeytorunoglu.thebestranks.configuration.settings

import com.zorbeytorunoglu.thebestranks.configuration.Resource

class SettingsContainer {

    private val configResource: Resource

    val chatFormatEnabled: Boolean
    val chatFormatDefaultFormat: String
    val chatFormatPerGroupFormats: MutableMap<String,String>
    val sqlEnabled: Boolean
    val sqlHost: String
    val sqlDatabaseName: String
    val sqlUsername: String
    val sqlPassword: String
    val sqlRanksTableName: String
    val sqlRanksUUIDColumn: String
    val sqlRanksRankColumn: String

    constructor(configResource: Resource) {
        this.configResource=configResource

        this.chatFormatEnabled=configResource.getBoolean("chat-format.enabled")
        this.chatFormatDefaultFormat=configResource.getString("chat-format.default-format")
        this.chatFormatPerGroupFormats=mutableMapOf()
        if (configResource.getConfigurationSection("chat-format.per-group").getKeys(false).isNotEmpty()) {
            for (key in configResource.getConfigurationSection("chat-format.per-group").getKeys(false)) {
                this.chatFormatPerGroupFormats[key] = configResource.getString("chat-format.per-group.$key")
            }
        }
        this.sqlEnabled=configResource.getBoolean("sql.enabled")
        this.sqlHost=configResource.getString("sql.host")
        this.sqlDatabaseName=configResource.getString("sql.db-name")
        this.sqlUsername=configResource.getString("sql.username")
        this.sqlPassword=configResource.getString("sql.password")
        this.sqlRanksTableName=configResource.getString("sql.ranks.table-name")
        this.sqlRanksUUIDColumn=configResource.getString("sql.ranks.columns.uuid")
        this.sqlRanksRankColumn=configResource.getString("sql.ranks.columns.rank")

    }

}