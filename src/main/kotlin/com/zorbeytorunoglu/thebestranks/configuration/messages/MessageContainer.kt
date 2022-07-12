package com.zorbeytorunoglu.thebestranks.configuration.messages

import com.zorbeytorunoglu.thebestranks.configuration.Resource
import com.zorbeytorunoglu.thebestranks.utils.StringUtils

class MessageContainer {

    val noPerm: String

    constructor(configResource: Resource) {
        this.noPerm=StringUtils.hex(configResource.getString("messages.no-perm"))
    }

}