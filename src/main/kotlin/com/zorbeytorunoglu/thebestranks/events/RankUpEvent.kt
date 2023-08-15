package com.zorbeytorunoglu.thebestranks.events

import com.zorbeytorunoglu.thebestranks.rank.Rank
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class RankUpEvent(private val player: Player, private val rank: Rank): Cancellable, Event() {

    companion object {

        private val handlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }

    }

    private var cancelled = false
    private var submitCommands = true

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

    fun setSubmitCommands(submitCommands: Boolean) {
        this.submitCommands = submitCommands
    }

    fun getSubmitCommands(): Boolean {
        return submitCommands
    }

    fun getPlayer(): Player {
        return player
    }

    fun getRank(): Rank {
        return rank
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

}