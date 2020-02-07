package com.deltadex.models

import com.deltadex.event_manager.*
import com.deltadex.network_manager.*
import com.badlogic.gdx.Gdx

class Player(val self: Boolean, val storage: Storage) {
    val cards = arrayListOf<Card>()

    init{
        if(self)
            registerHandler(::handle, EventType.PLAYCARD)
    }

    fun addCard(card: Card) {
        card.player = this
        cards.add(card)
        if(self) {
            card.moveBy(120f * (cards.size-1), 0f)
        }
        else {
            card.moveBy(-120f * (cards.size-1), 0f)
        }
    }

    fun removeCard(index: Int) {
        cards.removeAt(index)
    }

    fun handle(event: Event) {
        val packet = Packet(PacketID.PLAY_CARD, event.data)
        storage.networkManager?.sendPacket(packet)
        println("Sent packet")
    }
}