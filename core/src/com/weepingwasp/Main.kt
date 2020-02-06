package com.deltadex

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.freetype.*
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.deltadex.models.Storage
import com.deltadex.models.Card
import com.deltadex.graphics.Graphics
import com.deltadex.network_manager.NetworkManager
import com.deltadex.network_manager.Packet
import com.google.gson.internal.*

class Main : ApplicationAdapter() {
    val connectToServer = false

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
        pixmap.fillCircle(width / 4 * 1, height / 2 + height / 10, height / 16)
        pixmap.fillCircle(width / 4 * 2, height / 2 + height / 10, height / 16)
        pixmap.fillCircle(width / 4 * 3, height / 2 + height / 10, height / 16)

        pixmap.setColor(Color.BLACK)
        pixmap.drawLine(0, height / 2, width, height / 2)

        pixmap.setColor(Color.RED)
        pixmap.fillCircle(width / 4 * 1, height / 2 - height / 10, height / 16)
        pixmap.fillCircle(width / 4 * 2, height / 2 - height / 10, height / 16)
        pixmap.fillCircle(width / 4 * 3, height / 2 - height / 10, height / 16)

        return pixmap
    }

    fun packetReceived(packet: Packet): Unit {
        when(packet.PacketID) {
            3 -> {
                var content = (packet.Content as LinkedTreeMap<*, *>).get("hand") as List<LinkedTreeMap<*, *>>
                for(card in content) {
                    var ability = (card.get("Ability") as LinkedTreeMap<*, *>).get("Description") as String
                    var name = (card.get("Ability") as LinkedTreeMap<*, *>).get("Name") as String

                    newCard.add(arrayListOf(name, ability))
                }
            } 
            else -> {
                println(packet)
            }
        }
    }

    override
    fun create() {
        val boardPixmap = createBoardPixmap()
        boardImg = Texture(boardPixmap)
        boardPixmap.dispose()
        boardSprite = Image(boardImg)
        boardSprite!!.setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        val inputMultiplexer = InputMultiplexer()

        storage.stage = Stage()
        storage.stage!!.addActor(boardSprite)
        // storage.addCard(Card(), false)
        // storage.addCard(Card(), false)

        inputMultiplexer.addProcessor(storage.stage!!)
        Gdx.input.inputProcessor = inputMultiplexer

        if(connectToServer) {
            var networkManager = NetworkManager("127.0.0.1", 8080, ::packetReceived)
            networkManager.sendPacket(Packet(0, hashMapOf("username" to "oisin", "token" to "abcdefg")))
        }

        graphics = Graphics(storage)
    }

    override
    fun resize(width: Int, height: Int) {
        storage.stage!!.getViewport().update(width, height, true);
    }

    override
    fun render() {
        if(newCard.size != 0) {
            for(card in newCard) {
                var cad = Card()
                cad.text = "Ability: " + card[0] + "\n" + card[1]
                storage.addCard(cad, false)
            }
            newCard.clear()
        }
        graphics!!.render()
    }

    override
    fun dispose() {
        boardImg?.dispose()
    }
}
