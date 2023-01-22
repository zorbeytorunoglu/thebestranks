package com.zorbeytorunoglu.thebestranks.configuration.ranks


class Requirement(
    private val rank: Rank,
    private val placeholder: String,
    private val required: Any,
    private val denyMessage: String,
    private val guiMessage: String
) {

    fun getRank(): Rank {
        return rank
    }

    fun getPlaceholder(): String {
        return placeholder
    }

    fun getRequired(): Any {
        return required
    }

    fun getDenyMessage(): String {
        return denyMessage
    }

    fun getGuiMessage(): String {
        return guiMessage
    }

}