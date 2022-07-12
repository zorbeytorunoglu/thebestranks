package com.zorbeytorunoglu.thebestranks.configuration

import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException

class Resource(plugin: Plugin, name: String?) : YamlConfiguration() {

    private val file: File

    init {
        file = File(plugin.dataFolder, name)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists()) {
            plugin.saveResource(name!!, true)
        }
    }

    fun load() {
        try {
            super.load(file)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }

    fun save() {
        try {
            super.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getFile(): File {
        return file
    }
}