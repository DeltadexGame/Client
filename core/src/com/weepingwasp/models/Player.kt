package com.deltadex.models

import com.deltadex.event_manager.*
import com.deltadex.network_manager.Packet

class Player(val self: Boolean, val storage: Storage) {
    val cards = arrayListOf<Card>()
    var numCards = 0

    init{
        if(self)
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
        val packet = Packet(2, event.data)
        storage.networkManager?.sendPacket(packet)
        println("Sent packet")
    }
}