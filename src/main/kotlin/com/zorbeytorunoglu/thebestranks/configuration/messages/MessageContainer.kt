package com.zorbeytorunoglu.thebestranks.configuration.messages

import com.zorbeytorunoglu.thebestranks.configuration.Resource
import com.zorbeytorunoglu.thebestranks.utils.StringUtils

class MessageContainer {

    val noPerm: String
    val yourRank: String
    val onlyInGame: String
    val rankUpMessage: String
    val noGreaterRank: String
    val rankSetUsage: String
    val playerNotFound: String
    val rankDoesntExists: String
    val rankSet: String
    val noRankPrefix: String
    val noGreaterRankPrefix: String
    val helpPlayers: List<String>
    val helpAdmin: List<String>
    val checkRank: String
    val unknownArg: String
    val statusDone: String
    val statusNotDone: String

    constructor(configResource: Resource) {
        this.noPerm=StringUtils.hex(configResource.getString("messages.no-perm"))
        this.yourRank=StringUtils.hex(configResource.getString("messages.your-rank"))
        this.onlyInGame=StringUtils.hex(configResource.getString("messages.only-in-game"))
        this.rankUpMessage=StringUtils.hex(configResource.getString("messages.rank-up"))
        this.noGreaterRank=StringUtils.hex(configResource.getString("messages.no-greater-rank"))
        this.rankSetUsage=StringUtils.hex(configResource.getString("messages.rank-set-usage"))
        this.playerNotFound=StringUtils.hex(configResource.getString("messages.player-not-found"))
        this.rankDoesntExists=StringUtils.hex(configResource.getString("messages.rank-doesnt-exists"))
        this.rankSet=StringUtils.hex(configResource.getString("messages.rank-set"))
        this.noRankPrefix=StringUtils.hex(configResource.getString("messages.no-rank-prefix"))
        this.noGreaterRankPrefix=StringUtils.hex(configResource.getString("messages.no-greater-rank-prefix"))
        this.helpPlayers= configResource.getList("messages.help-players") as List<String>
        this.helpAdmin = configResource.getList("messages.help-admin") as List<String>
        this.checkRank=StringUtils.hex(configResource.getString("messages.check-rank"))
        this.unknownArg=StringUtils.hex(configResource.getString("messages.unknown-arg"))
        this.statusDone=StringUtils.hex(configResource.getString("messages.status-done"))
        this.statusNotDone=StringUtils.hex(configResource.getString("messages.status-not-done"))
    }

}