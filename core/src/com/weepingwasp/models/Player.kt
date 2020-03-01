package com.deltadex.models

import com.deltadex.event_manager.*
import com.deltadex.network_manager.*
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Disposable
import com.deltadex.getFont
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Timer

class Player(val self: Boolean, val storage: Storage): Group() {
    val cards = arrayListOf<Card>()
    val board = arrayOf<Monster?>(null, null, null)
    var turnArrow: Image? = null
    var healthBar: ProgressBar? = null
    var healthLabel: Label? = null
    var energyBar: ProgressBar? = null
    var energyLabel: Label? = null
    var nameLabel: Label? = null

    var refreshTurn = false

    val disposables = arrayListOf<Disposable>()

    var myTurn = false
    set(value) {
        field = value
        if(turnArrow != null) {
            removeActor(turnArrow)
        }
        refreshTurn = true
    }
    get() = field
    var playerName = ""
    set(value) {
        field = value
        if(nameLabel != null)
            nameLabel!!.setText(value)
    }
    get() = field
    var energy = 0
    set(value) {
        field = value
        if(energyBar != null) 
            energyBar!!.setValue(value.toFloat())
        if(energyLabel != null)
            energyLabel!!.setText("${value}/${maxEnergy}")
    }
    get() = field
    val maxEnergy = 10
    var health = 50
    set(value) {
        field = value
        if(healthBar != null)
            healthBar!!.setValue(value.toFloat())
        if(healthLabel != null)
            healthLabel!!.setText("${value}/${maxHealth}")
    }
    get() = field
    val maxHealth = 50

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
        registerHandler(::playerDamaged, EventType.PLAYERDAMAGE)
        registerHandler(::monsterSpawn, EventType.SPAWNMONSTER)
        registerHandler(::endTurn, EventType.ENDTURN)
        registerHandler(::startTurn, EventType.STARTTURN)
        registerHandler(::drawCard, EventType.DRAWCARD)
        registerHandler(::changeEnergy, EventType.CHANGEENERGY)
    }

    fun playerDamaged(event: Event) {
        if(self == event.data["ownership"]!!.toBoolean()) {
            health = event.data["health"]!!.toInt()
        }
    }

    fun changeEnergy(event: Event) {
        if(self == event.data["self"]!!.toBoolean()) {
            energy = event.data["energy"]!!.toDouble().toInt()
        }
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

    fun turnChange() {
        turnArrow?.addAction(Actions.removeActor())

        turnArrow = Image(storage.assetManager.get(if(myTurn) "turnArrowOn.png" else "turnArrowOff.png", Texture::class.java))
        if(self) {
            turnArrow!!.setBounds(storage.stage!!.width.toFloat() - turnArrow!!.width, storage.stage!!.height.toFloat() / 2 - turnArrow!!.height / 2 * 3, turnArrow!!.width, turnArrow!!.height)
        } else {
            turnArrow!!.setBounds(storage.stage!!.width.toFloat() - turnArrow!!.width, storage.stage!!.height.toFloat() / 2 - turnArrow!!.height / 2 * -1, turnArrow!!.width, turnArrow!!.height)
            turnArrow!!.setOrigin(turnArrow!!.width / 2, turnArrow!!.height / 2)
            turnArrow!!.rotation = 180f
        }
        addActor(turnArrow)
        refreshTurn = false
    }

    fun initGraphics() {
        turnArrow = Image(storage.assetManager.get(if(myTurn) "turnArrowOn.png" else "turnArrowOff.png", Texture::class.java))
        if(self) {
            turnArrow!!.setBounds(storage.stage!!.width.toFloat() - turnArrow!!.width, storage.stage!!.height.toFloat() / 2 - turnArrow!!.height / 2 * 3, turnArrow!!.width, turnArrow!!.height)
        } else {
            turnArrow!!.setBounds(storage.stage!!.width.toFloat() - turnArrow!!.width, storage.stage!!.height.toFloat() / 2 - turnArrow!!.height / 2 * -1, turnArrow!!.width, turnArrow!!.height)
            turnArrow!!.setOrigin(turnArrow!!.width / 2, turnArrow!!.height / 2)
            turnArrow!!.rotation = 180f
        }

        val pixmapRed = Pixmap(20, 20, Pixmap.Format.RGBA8888)
        pixmapRed.setColor(Color.RED)
        pixmapRed.fill()
        val textureRed = Texture(pixmapRed)
        pixmapRed.dispose()
        disposables.add(textureRed)

        val pixmapYellow = Pixmap(20, 20, Pixmap.Format.RGBA8888)
        pixmapYellow.setColor(Color.YELLOW)
        pixmapYellow.fill()
        val textureYellow = Texture(pixmapYellow)
        pixmapYellow.dispose()
        disposables.add(textureYellow)

        val pixmapGrey = Pixmap(20, 20, Pixmap.Format.RGBA8888)
        pixmapGrey.setColor(Color.GRAY)
        pixmapGrey.fill()
        val textureGrey = Texture(pixmapGrey)
        pixmapGrey.dispose()
        disposables.add(textureGrey)


        val healthStyle = ProgressBar.ProgressBarStyle(TextureRegionDrawable(textureGrey), null)
        var drawable = TextureRegionDrawable(textureRed)
        drawable.setMinWidth(0f)
        healthStyle.knobBefore = drawable
        healthBar = ProgressBar(0f, maxHealth.toFloat(), 1f, false, healthStyle)

        healthLabel = Label("${health}/${maxHealth}", Label.LabelStyle(getFont(), Color.WHITE))

        val energyStyle = ProgressBar.ProgressBarStyle(TextureRegionDrawable(textureGrey), null)
        drawable = TextureRegionDrawable(textureYellow)
        drawable.setMinWidth(0f)
        energyStyle.knobBefore = drawable
        energyBar = ProgressBar(0f, maxEnergy.toFloat(), 1f, false, energyStyle)

        energyLabel = Label("${energy}/${maxEnergy}", Label.LabelStyle(getFont(), Color.WHITE))

        nameLabel = Label(playerName, Label.LabelStyle(getFont(), Color.WHITE))
        nameLabel!!.setAlignment(Align.center, Align.center)
        nameLabel!!.setEllipsis(true)
        if(self) {
            healthBar!!.setPosition(storage.stage!!.width.toFloat() - healthBar!!.width * 2.1f, 0f)
            energyBar!!.setPosition(storage.stage!!.width.toFloat() - energyBar!!.width, 0f)
            nameLabel!!.setBounds(healthBar!!.x, healthBar!!.height * 1.1f, healthBar!!.width * 2.1f, healthBar!!.height)
        } else {
            healthBar!!.setPosition(0f, storage.stage!!.height.toFloat() - healthBar!!.height)
            energyBar!!.setPosition(energyBar!!.width * 1.1f, storage.stage!!.height.toFloat() - energyBar!!.height)
            nameLabel!!.setBounds(healthBar!!.x, healthBar!!.y - healthBar!!.height * 1.1f, healthBar!!.width * 2.1f, healthBar!!.height)
        }
        healthLabel!!.setBounds(healthBar!!.x, healthBar!!.y, healthBar!!.width, healthBar!!.height)
        healthLabel?.setAlignment(Align.center, Align.center)
        energyLabel!!.setBounds(energyBar!!.x, energyBar!!.y, energyBar!!.width, energyBar!!.height)
        energyLabel?.setAlignment(Align.center, Align.center)
        addActor(turnArrow)
        addActor(healthBar)
        addActor(healthLabel)
        addActor(energyBar)
        addActor(energyLabel)
        addActor(nameLabel)
        energyBar!!.setValue(energy.toFloat())
        healthBar!!.setValue(health.toFloat())
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
            card.isKnown = true
            card.cardName = event.data["name"]!!
            card.text = event.data["abilityName"] + ": " + event.data["abilityDescription"]
            card.cost = 0
            card.attack = event.data["attack"]!!.toFloat().toInt()
            card.health = event.data["health"]!!.toFloat().toInt()
            board[place] = Monster(card, place, self)
            storage.stage!!.addActor(board[place])
            if (self == false) {
                val newEnlargedCard = card.clone()
                newEnlargedCard.removeListener(newEnlargedCard.inputListener)
                newEnlargedCard.scaleBy(1f)
                val stage = storage.stage!!
                stage.addActor(newEnlargedCard)
                newEnlargedCard.setPosition(stage.width / 2 - 360f * newEnlargedCard.scaleX / 2, stage.height / 2 - 540f * newEnlargedCard.scaleY / 2)
                Timer.schedule(object: Timer.Task() {
                    override fun run() {
                        newEnlargedCard.addAction(Actions.removeActor())
                    }
                }, 3f);
            }
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
        if(refreshTurn) {
            turnChange()
        }
        super.draw(batch, parentAlpha)
    }

    override
    fun remove(): Boolean {
        for(disposable in disposables) {
            disposable.dispose()
        }
        return super.remove()
    }
}
