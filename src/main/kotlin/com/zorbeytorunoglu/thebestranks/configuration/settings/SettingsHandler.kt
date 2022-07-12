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

}