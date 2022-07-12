package com.zorbeytorunoglu.thebestranks.utils

import com.zorbeytorunoglu.thebestranks.TBR

class Utils {

    private val plugin: TBR

    private val rankUtils: RankUtils
    private val stringUtils: StringUtils

    constructor(plugin: TBR) {
        this.plugin=plugin
        this.rankUtils=RankUtils(plugin)
        this.stringUtils=StringUtils(plugin)
    }

    fun getRankUtils(): RankUtils {
        return rankUtils
    }

    fun getStringUtils(): StringUtils {
        return stringUtils
    }

}