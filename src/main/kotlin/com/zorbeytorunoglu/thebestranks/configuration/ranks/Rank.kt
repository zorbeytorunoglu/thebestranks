package com.zorbeytorunoglu.thebestranks.configuration.ranks

import com.zorbeytorunoglu.thebestranks.configuration.Resource

class Rank {

    private val id: String
    private val prefix: String
    private val commands: ArrayList<String>

    private lateinit var requirements: MutableList<Requirement>

    constructor(id: String, prefix: String, commands: ArrayList<String>) {
        this.id=id
        this.prefix=prefix
        this.commands=commands
    }

    fun getId(): String {
        return id
    }

    fun getPrefix(): String {
        return prefix
    }

    fun getCommands(): ArrayList<String> {
        return commands
    }

    companion object {
        fun loadRanks(ranksResource: Resource): ArrayList<Rank> {

            var rankList: ArrayList<Rank> = ArrayList()

            if (ranksResource.getKeys(false).isEmpty()) return rankList

            val set:Set<String> = ranksResource.getKeys(false)

            for (id in set) {

                val prefix=ranksResource.getString("$id.prefix")

                val commands: List<String> = ranksResource.getStringList("$id.commands")

                val commandsArray: ArrayList<String> = ArrayList()

                commandsArray.addAll(commands)

                val rank: Rank = Rank(id,prefix,commandsArray)

                val reqSet: Set<String> = ranksResource.getConfigurationSection("$id.requirements.papi").getKeys(false)

                if (reqSet.isEmpty()) continue

                val reqList: MutableList<Requirement> = mutableListOf()

                for (req in reqSet) {

                    val requirement: Requirement = Requirement(rank,req,
                    ranksResource.getInt("$id.requirements.papi.$req.required"),
                    ranksResource.getString("$id.requirements.papi.$req.deny_message"),
                        ranksResource.getString("$id.requirements.papi.$req.gui_message"))

                    reqList.add(requirement)

                }

                rank.requirements=reqList

                rankList.add(rank)

            }

            return rankList

        }
    }

}