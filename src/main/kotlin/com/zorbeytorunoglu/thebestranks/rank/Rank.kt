package com.zorbeytorunoglu.thebestranks.rank

data class Rank(
    val name: String,
    val displayName: String,
    val prefix: String,
    val order: Int,
    val requirements: List<Requirement>,
    val lore: List<String>,
    val commands: List<String>?
)