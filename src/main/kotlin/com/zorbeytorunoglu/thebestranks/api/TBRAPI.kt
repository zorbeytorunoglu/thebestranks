package com.zorbeytorunoglu.thebestranks.api

import com.zorbeytorunoglu.thebestranks.TBR
import com.zorbeytorunoglu.thebestranks.rank.RankManager

class TBRAPI private constructor() {

    companion object {

        private lateinit var plugin: TBR

        fun getTBR(): TBR = plugin

        fun setPluginInstance(plugin: TBR) {
            try {
                TBRAPI.plugin = plugin
            } catch (error: UnsupportedOperationException) {
                throw UnsupportedOperationException("You cannot initialize the plugin instance after it was initialized.")
            }
        }

        @JvmStatic
        fun getRankManager(): RankManager = plugin.rankManager

    }

}