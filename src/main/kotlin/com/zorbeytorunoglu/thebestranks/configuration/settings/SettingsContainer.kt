package com.zorbeytorunoglu.thebestranks.configuration.settings

import com.zorbeytorunoglu.thebestranks.configuration.Resource

class SettingsContainer {

    private val configResource: Resource

    val chatFormatEnabled: Boolean
    val chatFormatDefaultFormat: String
    val chatFormatPerGroupFormats: MutableMap<String,String>

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



    }

}