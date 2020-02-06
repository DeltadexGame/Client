package com.deltadex.models

import com.deltadex.event_manager.*

class Player(val self: Boolean) {
    val cards = arrayListOf<Card>()
    var numCards = 0

    init{
        registerHandler(::handle, EventType.PLAYCARD)
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

    fun handle(event: Event) {
        println("card Played")
    }
}