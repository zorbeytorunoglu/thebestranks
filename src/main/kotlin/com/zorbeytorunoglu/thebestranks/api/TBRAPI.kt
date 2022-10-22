package com.zorbeytorunoglu.thebestranks.api

import com.zorbeytorunoglu.thebestranks.TBR
import com.zorbeytorunoglu.thebestranks.utils.RankUtils
import java.lang.UnsupportedOperationException

class TBRAPI private constructor() {

    companion object {

        private lateinit var plugin: TBR

        @JvmStatic
        fun getTBR(): TBR {
            return plugin
        }

        fun setPluginInstance(plugin: TBR) {

            try {
                TBRAPI.plugin = plugin
            } catch (error: UnsupportedOperationException) {
                throw UnsupportedOperationException("You cannot initialize the plugin instance after it was initialized.")
            }

        }

        @JvmStatic
        fun getRankManager(): RankUtils {
            return plugin.getUtils().getRankUtils()
        }

    }

}