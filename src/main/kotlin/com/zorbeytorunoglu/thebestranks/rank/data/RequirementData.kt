package com.zorbeytorunoglu.thebestranks.rank.data

data class RequirementData(
    val requirementPlaceholder: String,
    val requirementType: String,
    val requirementValue: String,
    val case: String?,
    val guiMessage: String,
    val denyMessage: String
)
