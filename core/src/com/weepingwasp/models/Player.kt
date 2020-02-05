package com.weepingwasp.models

import com.weepingwasp.event_manager.*

class Player(val self: Boolean): EventHandler {
    val cards = arrayListOf<Card>()
    var numCards = 0

    init{
        registerHandler(this, EventType.PLAYCARD)
    }

    fun addCard(card: Card) {
        cards.add(card)
        numCards++
        card.moveBy(120f * (numCards-1), 0f)
    }

    fun removeCard(index: Int) {
        cards.removeAt(index)
        numCards--
    }

    override
    fun handle(event: Event) {
        println("card Played")
    }
}