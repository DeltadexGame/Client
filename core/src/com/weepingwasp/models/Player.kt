package com.deltadex.models

import com.deltadex.event_manager.*
import com.deltadex.network_manager.*
import com.badlogic.gdx.Gdx

class Player(val self: Boolean, val storage: Storage) {
    val cards = arrayListOf<Card>()
    val board = arrayOf<Monster?>(null, null, null)

    init{
        if(self) {
            registerHandler(::handle, EventType.PLAYCARD)
            registerHandler(::placeResult, EventType.PLAYCARDRESULT)
        }
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

    fun placeResult(event: Event) {
        if(event.data["result"] == "true") {
            place(event.data["from"]!!.toFloat().toInt(), event.data["place"]!!.toFloat().toInt())
        } else {
            cards[event.data["from"]!!.toFloat().toInt()].inputListener.resetLocation(cards[event.data["from"]!!.toFloat().toInt()])
        }
    }

    fun place(from: Int, place: Int) {
        cards[from].scaleBy(-0.1f)
        board[place] = Monster(cards[from], place, self)
        storage.stage!!.addActor(board[place])
        cards[from].remove()
        removeCard(from)
    }

    fun handle(event: Event) {
        val packet = Packet(PacketID.PLAY_CARD.id, event.data)
        storage.networkManager?.sendPacket(packet)
        println("Sent packet")
    }
}