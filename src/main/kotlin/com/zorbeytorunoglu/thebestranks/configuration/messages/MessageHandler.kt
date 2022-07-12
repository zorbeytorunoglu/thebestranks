package com.zorbeytorunoglu.thebestranks.configuration.messages

import com.zorbeytorunoglu.thebestranks.utils.StringUtils

class MessageHandler(private val container: MessageContainer) {

    fun getNoPerm(): String {
        return StringUtils.hex(container.noPerm)
    }

}