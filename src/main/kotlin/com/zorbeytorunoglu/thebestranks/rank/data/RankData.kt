package com.zorbeytorunoglu.thebestranks.rank.data

data class RankData(
    val name: String,
    val displayName: String,
    val prefix: String,
    val order: Int,
    val requirementData: List<RequirementData>,
    val lore: List<String>,
    val commands: List<String>?
)
