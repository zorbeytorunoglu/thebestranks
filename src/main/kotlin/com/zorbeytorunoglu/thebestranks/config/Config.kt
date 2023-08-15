package com.zorbeytorunoglu.thebestranks.config

import com.zorbeytorunoglu.kLib.configuration.Resource

class Config(file: Resource) {

    val rankOnJoin = file.getBoolean("autoGiveFirstRankOnJoin")

}