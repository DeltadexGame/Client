package com.deltadex

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.freetype.*
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.deltadex.event_manager.*
import com.deltadex.event_manager.Event
import com.deltadex.graphics.Graphics
import com.deltadex.models.Card
import com.deltadex.models.Storage
import com.deltadex.network_manager.NetworkManager
import com.deltadex.network_manager.Packet
import com.deltadex.network_manager.PacketID
import com.google.gson.internal.*
import com.badlogic.gdx.Game

class Main(val name: String, val token: String, val game: Game, val ip: String) : ScreenAdapter() {
    var boardImg: Texture? = null
    var boardSprite: Image? = null

    val storage = Storage()

    val newCard: ArrayList<ArrayList<String>> = arrayListOf()

    var graphics: Graphics? = null

    fun createBoardPixmap(): Pixmap {
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)

        pixmap.setColor(Color.BROWN)
        pixmap.fill()

        pixmap.setColor(Color.GREEN)
        pixmap.fillRectangle(width / 4 * 1 - height / 16, height / 2 + height / 10 - height / 16, height / 8, height / 8)
        pixmap.fillRectangle(width / 4 * 2 - height / 16, height / 2 + height / 10 - height / 16, height / 8, height / 8)
        pixmap.fillRectangle(width / 4 * 3 - height / 16, height / 2 + height / 10 - height / 16, height / 8, height / 8)

        pixmap.setColor(Color.BLACK)
        pixmap.drawLine(0, height / 2, width, height / 2)

        pixmap.setColor(Color.RED)
        pixmap.fillRectangle(width / 4 * 1 - height / 16, height / 2 - height / 10 - height / 16, height / 8, height / 8)
        pixmap.fillRectangle(width / 4 * 2 - height / 16, height / 2 - height / 10 - height / 16, height / 8, height / 8)
        pixmap.fillRectangle(width / 4 * 3 - height / 16, height / 2 - height / 10 - height / 16, height / 8, height / 8)

        return pixmap
    }

    fun packetReceived(packet: Packet) {
        if(System.getenv("DEBUG") ?: "false" == "true") {
            println(packet)
        }
        when (packet.PacketID) {
            PacketID.SELF_INIT.id -> {
                val content = (packet.Content as LinkedTreeMap<*, *>)
                val eventData = hashMapOf(
                    "name" to content.get("username").toString(),
                    "energy" to content.get("energy").toString(),
                    "health" to content.get("health").toString(),
                    "starting" to content.get("starting").toString()
                )
                pushEvent(Event(EventType.SELFSTARTINGINFO, eventData))
            }
            PacketID.OPPONENT_INIT.id -> {
                val content = (packet.Content as LinkedTreeMap<*, *>)
                val eventData = hashMapOf(
                    "name" to content.get("username").toString(),
                    "energy" to content.get("energy").toString(),
                    "health" to content.get("health").toString(),
                    "starting" to content.get("starting").toString()
                )
                pushEvent(Event(EventType.ENEMYSTARTINGINFO, eventData))
            }
            PacketID.OPPONENT_STARTING_HAND.id -> {
                val cards = ((packet.Content as LinkedTreeMap<*, *>).get("hand") as Double).toInt()
                for (i in 0 until cards) {
                    storage.addCard(Card(storage.opponent), true)
                }
            }
            PacketID.STARTING_HAND.id -> {
                val content = (packet.Content as LinkedTreeMap<*, *>).get("hand") as List<LinkedTreeMap<*, *>>
                for (cardData in content) {
                    val abilityDesc = (cardData.get("Ability") as LinkedTreeMap<*, *>).get("Description") as String
                    val abilityName = (cardData.get("Ability") as LinkedTreeMap<*, *>).get("Name") as String

                    val cardName = (cardData.get("Name") as String)
                    val cardCost = (cardData.get("EnergyCost") as Double)
                    val cardAttack = (cardData.get("Attack") as Double)
                    val cardHealth = (cardData.get("Health") as Double)

                    val card = Card(storage.player)
                    card.text = abilityName + ": " + abilityDesc
                    card.cardName = cardName
                    card.cost = cardCost.toInt()
                    card.attack = cardAttack.toInt()
                    card.health = cardHealth.toInt()

                    storage.addCard(card, false)
                }
            }
            PacketID.PLAY_CARD_RESULT.id -> {
                val result = (packet.Content as LinkedTreeMap<String, Any>).toMap()
                var stringResult = hashMapOf<String, String>()
                for ((key, value) in result) {
                    stringResult[key] = value.toString()
                }
                pushEvent(Event(EventType.PLAYCARDRESULT, stringResult))
            }
            PacketID.MONSTER_SPAWN.id -> {
                val content = (packet.Content as LinkedTreeMap<*, *>)
                val eventData = hashMapOf(
                    "name" to (content.get("monster") as LinkedTreeMap<*, *>).get("Name").toString(),
                    "abilityName" to ((content.get("monster") as LinkedTreeMap<*, *>).get("Ability") as LinkedTreeMap<*, *>).get("Name").toString(),
                    "cost" to (content.get("monster") as LinkedTreeMap<*, *>).get("EnergyCost").toString(),
                    "attack" to (content.get("monster") as LinkedTreeMap<*, *>).get("Attack").toString(),
                    "health" to (content.get("monster") as LinkedTreeMap<*, *>).get("Health").toString(),
                    "maxHealth" to (content.get("monster") as LinkedTreeMap<*, *>).get("MaxHealth").toString(),
                    "abilityDescription" to ((content.get("monster") as LinkedTreeMap<*, *>).get("Ability") as LinkedTreeMap<*, *>).get("Description").toString(),
                    "position" to content.get("position").toString(),
                    "ownership" to content.get("ownership").toString()
                )
                pushEvent(Event(EventType.SPAWNMONSTER, eventData))
            }
            PacketID.OPPONENT_PLAY_CARD.id -> {
                var content = (packet.Content as LinkedTreeMap<*, *>)
                val eventData = hashMapOf(
                    "name" to (content.get("card") as LinkedTreeMap<*, *>).get("Name").toString(),
                    "abilityName" to ((content.get("card") as LinkedTreeMap<*, *>).get("Ability") as LinkedTreeMap<*, *>).get("Name").toString(),
                    "cost" to (content.get("card") as LinkedTreeMap<*, *>).get("EnergyCost").toString(),
                    "attack" to (content.get("card") as LinkedTreeMap<*, *>).get("Attack").toString(),
                    "health" to (content.get("card") as LinkedTreeMap<*, *>).get("Health").toString(),
                    "position" to content.get("position").toString()
                )
                pushEvent(Event(EventType.ENEMYPLAYCARD, eventData))
            }
            PacketID.CHANGE_ENERGY.id -> {
                val eventData = hashMapOf("self" to "true", "energy" to (packet.Content as LinkedTreeMap<*, *>).get("energy").toString())
                pushEvent(Event(EventType.CHANGEENERGY, eventData))
            }
            PacketID.END_TURN_MONSTER_ATTACKED.id -> {
                val content = packet.Content as LinkedTreeMap<*, *>
                val eventData = hashMapOf(
                    "ownership" to (!(content.get("ownership") as Boolean)).toString(),
                    "position" to (content.get("position") as Double).toInt().toString(),
                    "health" to ((content.get("monster") as LinkedTreeMap<*, *>).get("Health") as Double).toInt().toString(),
                    "died" to (content.get("died") as Boolean).toString()
                )
                pushEvent(Event(EventType.MONSTERDAMAGE, eventData))
            }
            PacketID.END_TURN_PLAYER_ATTACKED.id -> {
                val content = packet.Content as LinkedTreeMap<*, *>
                val eventData = hashMapOf(
                    "ownership" to (!(content.get("ownership") as Boolean)).toString(),
                    "health" to ((content.get("health") as Double).toInt().toString()),
                    "died" to (content.get("died") as Boolean).toString(),
                    "position" to (content.get("position") as Double).toInt().toString()
                )
                pushEvent(Event(EventType.PLAYERDAMAGE, eventData))
            }
            PacketID.END_TURN.id -> {
                pushEvent(Event(EventType.ENDTURN, hashMapOf("self" to (packet.Content as LinkedTreeMap<*, *>).get("self").toString())))
            }
            PacketID.START_TURN.id -> {
                pushEvent(Event(EventType.STARTTURN, hashMapOf("self" to (packet.Content as LinkedTreeMap<*, *>).get("self").toString())))
            }
            PacketID.YOU_DRAW_CARD.id -> {
                var content = (packet.Content as LinkedTreeMap<*, *>)
                pushEvent(Event(EventType.DRAWCARD, hashMapOf(
                    "self" to "true",
                    "name" to (content.get("card") as LinkedTreeMap<*, *>).get("Name").toString(),
                    "abilityName" to ((content.get("card") as LinkedTreeMap<*, *>).get("Ability") as LinkedTreeMap<*, *>).get("Name").toString(),
                    "cost" to (content.get("card") as LinkedTreeMap<*, *>).get("EnergyCost").toString(),
                    "attack" to (content.get("card") as LinkedTreeMap<*, *>).get("Attack").toString(),
                    "abilityDescription" to ((content.get("card") as LinkedTreeMap<*, *>).get("Ability") as LinkedTreeMap<*, *>).get("Description").toString(),
                    "health" to (content.get("card") as LinkedTreeMap<*, *>).get("Health").toString()
                )))
            }
            PacketID.ENEMY_DRAW_CARD.id -> {
                pushEvent(Event(EventType.DRAWCARD, hashMapOf("self" to "false")))
            }
            else -> {
                println(packet)
            }
        }
    }

    override
    fun show() {
        val boardPixmap = createBoardPixmap()
        boardImg = Texture(boardPixmap)
        boardPixmap.dispose()
        boardSprite = Image(boardImg)
        boardSprite!!.setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        val inputMultiplexer = InputMultiplexer()

        storage.stage = Stage()
        storage.stage!!.addActor(boardSprite)
        storage.assetManager.load("endturn.png", Texture::class.java)
        storage.assetManager.load("card.png", Texture::class.java)
        storage.assetManager.load("cardBack.png", Texture::class.java)
        storage.assetManager.load("healthBox.png", Texture::class.java)
        storage.assetManager.load("attackBox.png", Texture::class.java)
        storage.assetManager.load("costBox.png", Texture::class.java)
        storage.assetManager.load("turnArrowOn.png", Texture::class.java)
        storage.assetManager.load("turnArrowOff.png", Texture::class.java)
        storage.assetManager.finishLoading()
        val endTurn = Image(storage.assetManager.get("endturn.png", Texture::class.java))
        endTurn.setBounds(storage.stage!!.width.toFloat() - endTurn.width, storage.stage!!.height.toFloat() / 2 - endTurn.height / 2, endTurn.width, endTurn.height)
        endTurn.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                storage.networkManager?.sendPacket(Packet(PacketID.END_TURN.id, hashMapOf<String, String>()))
                return true
            }
        })

        storage.stage!!.addActor(endTurn)
        storage.stage!!.addActor(storage.player)
        storage.stage!!.addActor(storage.opponent)
        // var card = Card()
        // card.pictureLocation = "Zombie.png"
        // card.cardName = "Zombie"
        // card.text = "Walking Dead:\nMonster resurrected at half health upon death"
        // card.attack = 1
        // card.health = 2
        // card.cost = 1
        // storage.addCard(card, false)
        // var card1 = Card()
        // storage.addCard(card1, true)
        // card1 = Card()
        // storage.addCard(card1, true)
        // storage.addCard(Card(), false)

        inputMultiplexer.addProcessor(storage.stage!!)
        Gdx.input.inputProcessor = inputMultiplexer

        val port = ip.split(":").getOrNull(1)?.toInt() ?: 8080
        var ip = ip.split(":")[0]
        if(ip == "")
            ip = "localhost"
        storage.networkManager = NetworkManager(ip, port, ::packetReceived)
        storage.networkManager!!.sendPacket(Packet(PacketID.AUTH_INFO.id, hashMapOf("username" to name, "token" to token)))

        graphics = Graphics(storage)
    }

    override
    fun resize(width: Int, height: Int) {
        storage.stage!!.getViewport().update(width, height, true)
    }

    override
    fun render(delta: Float) {
        graphics!!.render()
        storage.assetManager.update()
    }

    override
    fun dispose() {
        boardImg?.dispose()
        storage.assetManager.dispose()
    }
}
