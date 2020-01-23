package com.weepingwasp

class Player(val self: Boolean) {
    val cards = arrayListOf<Card>()
    var numCards = 0

    fun addCard(card: Card) {
        cards.add(card)
        numCards++
    }

    fun removeCard(index: Int) {
        cards.removeAt(index)
        numCards--
    }

}