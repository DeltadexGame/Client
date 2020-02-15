package com.deltadex.models

import com.deltadex.event_manager.*
import com.deltadex.network_manager.*

class Player(val self: Boolean, val storage: Storage) {
    val cards = arrayListOf<Card>()
    val board = arrayOf<Monster?>(null, null, null)

    init {
        if (self) {
            registerHandler(::handle, EventType.PLAYCARD)
            registerHandler(::placeResult, EventType.PLAYCARDRESULT)
        } else {
            registerHandler(::enemyPlace, EventType.ENEMYPLAYCARD)
        }
        registerHandler(::monsterDamaged, EventType.MONSTERDAMAGE)
        registerHandler(::monsterSpawn, EventType.SPAWNMONSTER)
    }

    fun monsterSpawn(event: Event) {
        if (event.data["ownership"]!!.toBoolean() == self) {
            val place = event.data["position"]!!.toDouble().toInt()
            val card = Card()
            card.player = this
            card.cardName = event.data["name"]!!
            card.text = event.data["abilityName"] + ": " + event.data["abilityDescription"]
            card.cost = 0
            card.attack = event.data["attack"]!!.toFloat().toInt()
            card.health = event.data["health"]!!.toFloat().toInt()
            board[place] = Monster(card, place, self)
            storage.stage!!.addActor(board[place])
        }
    }

    fun monsterDamaged(event: Event) {
        if (event.data["ownership"]!!.toBoolean() == self) {
            if (event.data["died"]!!.toBoolean()) {
                board[event.data["position"]!!.toInt()]!!.remove()
                board[event.data["position"]!!.toInt()] = null
            } else {
                board[event.data["position"]!!.toInt()]!!.health = event.data["health"]!!.toInt()
            }
        }
    }

    fun enemyPlace(event: Event) {
        removeCard(0)
    }

    fun addCard(card: Card) {
        card.player = this
        cards.add(card)
        if (self) {
            card.moveBy(120f * (cards.size - 1), 0f)
        } else {
            card.moveBy(-120f * (cards.size - 1), 0f)
        }
    }

    fun removeCard(index: Int) {
        cards[index].remove()
        cards.removeAt(index)
    }

    fun placeResult(event: Event) {
        if (event.data["result"] == "true") {
            removeCard(event.data["from"]!!.toFloat().toInt())
        } else {
            cards[event.data["from"]!!.toFloat().toInt()].inputListener.resetLocation(cards[event.data["from"]!!.toFloat().toInt()])
        }
    }

    fun handle(event: Event) {
        val packet = Packet(PacketID.PLAY_CARD.id, event.data)
        storage.networkManager?.sendPacket(packet)
    }
}
