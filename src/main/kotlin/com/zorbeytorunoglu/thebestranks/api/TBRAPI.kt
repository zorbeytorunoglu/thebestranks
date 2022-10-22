package com.zorbeytorunoglu.thebestranks.api

import com.zorbeytorunoglu.thebestranks.TBR
import com.zorbeytorunoglu.thebestranks.utils.RankUtils

class TBRAPI private constructor() {

    companion object {

        private lateinit var plugin: TBR

        @JvmStatic
        fun getTBR(): TBR {
            return plugin
        }

        fun setPluginInstance(plugin: TBR) {

            TBRAPI.plugin = plugin

        }

        @JvmStatic
        fun getRankManager(): RankUtils {
            return plugin.getUtils().getRankUtils()
        }

    }

}