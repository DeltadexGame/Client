package com.deltadex.models

import com.deltadex.event_manager.*
import com.deltadex.network_manager.*
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.Texture

class Player(val self: Boolean, val storage: Storage): Group() {
    val cards = arrayListOf<Card>()
    val board = arrayOf<Monster?>(null, null, null)
    var turnArrow: Image? = null
    var myTurn = false
    set(value) {
        field = value
        if(turnArrow != null) {
            removeActor(turnArrow)
        }
        graphicsInitialised = false
    }
    get() = field
    var playerName = ""
    var energy = 0
    var health = 50

    var graphicsInitialised = false

    init {
        if (self) {
            registerHandler(::handle, EventType.PLAYCARD)
            registerHandler(::placeResult, EventType.PLAYCARDRESULT)
            registerHandler(::startingInfo, EventType.SELFSTARTINGINFO)
        } else {
            registerHandler(::enemyPlace, EventType.ENEMYPLAYCARD)
            registerHandler(::startingInfo, EventType.ENEMYSTARTINGINFO)
        }
        registerHandler(::monsterDamaged, EventType.MONSTERDAMAGE)
        registerHandler(::monsterSpawn, EventType.SPAWNMONSTER)
        registerHandler(::endTurn, EventType.ENDTURN)
        registerHandler(::startTurn, EventType.STARTTURN)
        registerHandler(::drawCard, EventType.DRAWCARD)
    }

    fun endTurn(event: Event) {
        if(self == event.data["self"]!!.toBoolean()) {
            myTurn = false
        }
    }

    fun startTurn(event: Event) {
        if(self == event.data["self"]!!.toBoolean()) {
            myTurn = true
        }
    }

    fun drawCard(event: Event) {
        if(event.data["self"]!!.toBoolean() == self)
            if(self) {
                val card = Card(this)
                card.text = event.data["abilityName"]!! + ": " + event.data["abilityDescription"]!!
                card.cardName = event.data["name"]!!
                card.cost = event.data["cost"]!!.toDouble().toInt()
                card.attack = event.data["attack"]!!.toDouble().toInt()
                card.health = event.data["health"]!!.toDouble().toInt()
                storage.addCard(card, false)
            } else {
                storage.addCard(Card(this), true)
            }
    }

    fun initGraphics() {
        turnArrow = Image(Texture(if(myTurn) "turnArrowOn.png" else "turnArrowOff.png"))
        if(self) {
            turnArrow!!.setBounds(storage.stage!!.width.toFloat() - turnArrow!!.width, storage.stage!!.height.toFloat() / 2 - turnArrow!!.height / 2 * 3, turnArrow!!.width, turnArrow!!.height)
        } else {
            turnArrow!!.setBounds(storage.stage!!.width.toFloat() - turnArrow!!.width, storage.stage!!.height.toFloat() / 2 - turnArrow!!.height / 2 * -1, turnArrow!!.width, turnArrow!!.height)
            turnArrow!!.setOrigin(turnArrow!!.width / 2, turnArrow!!.height / 2)
            turnArrow!!.rotation = 180f
        }
        addActor(turnArrow)
        graphicsInitialised = true
    }

    fun startingInfo(event: Event) {
        myTurn = event.data["starting"]!!.toBoolean()
        playerName = event.data["name"]!!
        energy = event.data["energy"]!!.toDouble().toInt()
        health = event.data["health"]!!.toDouble().toInt()
    }

    fun monsterSpawn(event: Event) {
        if (event.data["ownership"]!!.toBoolean() == self) {
            val place = event.data["position"]!!.toDouble().toInt()
            val card = Card(this)
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
        val cardsCopy = cards.clone() as ArrayList<Card>
        cards.clear()
        for(card in cardsCopy) {
            if(self)
                card.setPosition(0f, 0f)
            else
                card.setPosition(stage.width - card.image!!.width * card.scaleX, stage.height - card.image!!.height * card.scaleY)
            addCard(card)
        }
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

    override
    fun draw(batch: Batch, parentAlpha: Float) {
        if(!graphicsInitialised) {
            initGraphics()
        }
        super.draw(batch, parentAlpha)
    }
}
