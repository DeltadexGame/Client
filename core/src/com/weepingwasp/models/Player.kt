package com.weepingwasp.models

class Player(val self: Boolean) {
    val cards = arrayListOf<Card>()
    var numCards = 0

    fun addCard(card: Card) {
        cards.add(card)
        numCards++
        card.moveBy(120f * (numCards-1), 0f)
    }

    fun removeCard(index: Int) {
        cards.removeAt(index)
        numCards--
    }

}