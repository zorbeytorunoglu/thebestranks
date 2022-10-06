package com.zorbeytorunoglu.thebestranks.configuration.settings

class SettingsHandler(private val settingsContainer: SettingsContainer) {

    fun getChatFormatEnabled(): Boolean {
        return settingsContainer.chatFormatEnabled
    }

    fun getChatFormatDefaultFormat(): String {
        return settingsContainer.chatFormatDefaultFormat
    }

    fun getChatFormatPerGroupFormats(): MutableMap<String, String> {
        return settingsContainer.chatFormatPerGroupFormats
    }

    fun getSqlEnabled(): Boolean {
        return settingsContainer.sqlEnabled
    }

    fun getSqlHost(): String {
        return settingsContainer.sqlHost
    }

    fun getSqlDatabaseName(): String {
        return settingsContainer.sqlDatabaseName
    }

    fun getSqlUsername(): String {
        return settingsContainer.sqlUsername
    }

    fun getSqlPassword(): String {
        return settingsContainer.sqlPassword
    }

    fun getSqlRanksTableName(): String {
        return settingsContainer.sqlRanksTableName
    }

    fun getSqlRanksUUIDColumn(): String {
        return settingsContainer.sqlRanksUUIDColumn
    }

    fun getSqlRanksRankColumn(): String {
        return settingsContainer.sqlRanksRankColumn
    }

}