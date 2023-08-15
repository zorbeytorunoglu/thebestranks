package com.zorbeytorunoglu.thebestranks.rank

data class Requirement(
    val placeholder: String,
    val type: RequirementType,
    val value: Any,
    val case: RequirementCase?,
    val guiMessage: String,
    val denyMessage: String
)