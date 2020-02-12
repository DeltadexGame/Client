package com.deltadex.models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.deltadex.input_processors.CardInputListener
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.deltadex.event_manager.*
import com.deltadex.getFont
import com.deltadex.getBigFont
import com.deltadex.graphics.HealthLabel
import com.deltadex.graphics.AttackLabel

class Card() : Group() {

    private var graphicsInitialised = false

    var image: Image? = null

    var picture: Image? = null

    var player: Player? = null

    var pictureLocation = ""
    set(value) {
        field = value
        if(picture != null) {
            picture = Image(Texture(value))
        }
    }
    get() = field

    var text: String = ""
    set(value) {
        field = value
        label?.setText(value)
    }
    get() = field
    private var label: Label? = null

    var cardName: String = ""
    set(value) {
        field = value
        nameLabel?.setText(value)
        if(pictureLocation == "") {
            pictureLocation = value + ".png"
        }
    }
    get() = field
    private var nameLabel: Label? = null

    var attack: Int = 0
    set(value) {
        field = value
        attackLabel?.value = value
    }
    get() = field
    private var attackLabel: AttackLabel? = null

    var health: Int = 0
    set(value) {
        field = value
        healthLabel?.value = value
    }
    get() = field
    private var healthLabel: HealthLabel? = null

    var cost: Int = 0
    set(value) {
        field = value
        costLabel?.setText(value)
    }
    get() = field
    private var costLabel: Label? = null

    var maxHealth: Int = 0

    val inputListener = CardInputListener()

    init {
        this.scaleBy(-0.7f)
    }

    fun clone(): Card {
        val card = Card()
        card.player = this.player
        card.text = this.text
        card.cardName = this.cardName
        card.health = this.health
        card.attack = this.attack
        card.maxHealth = this.maxHealth
        card.cost = this.cost
        card.pictureLocation = this.pictureLocation
        return card
    }

    fun initGraphics() {
        if(this.player != null) {
            if(this.player!!.self) {
                this.addListener(inputListener)
                image = Image(Texture("card.png"))
                label = Label(text, Label.LabelStyle(getFont(), Color.WHITE))
                nameLabel = Label(cardName, Label.LabelStyle(getFont(), Color.WHITE))
                attackLabel = AttackLabel(attack)
                healthLabel = HealthLabel(health)
                costLabel = Label(cost.toString(), Label.LabelStyle(getBigFont(), Color.WHITE))
                if(pictureLocation != "")
                    picture = Image(Texture(pictureLocation))
                else
                    picture = Image()
                label?.setBounds(42f, 122f, 279f, 117f)
                label?.setAlignment(Align.topLeft, Align.left)
                label?.setWrap(true)
                nameLabel?.setBounds(42f, 505f, 279f, 25f)
                nameLabel?.setAlignment(Align.bottomLeft, Align.center)
                attackLabel?.setBounds(36f, 36f, 64f, 64f)
                healthLabel?.setBounds(260f, 36f, 64f, 64f)
                costLabel?.setBounds(261f, 441f, 63f, 63f)
                costLabel?.setAlignment(Align.center, Align.center)
                picture?.setBounds(38f, 262f, 284f, 240f)
                addActor(image)
                addActor(label)
                addActor(healthLabel)
                addActor(attackLabel)
                addActor(picture)
                addActor(costLabel)
                addActor(nameLabel)
            }
            else {
                image = Image(Texture("cardBack.png"))
                this.moveBy(stage.width - image!!.width * this.scaleX, stage.height - image!!.height * this.scaleY)
                addActor(image)
            }
            graphicsInitialised = true
        }
    }

    override
    fun draw(batch: Batch, parentAlpha: Float) {
        if(!graphicsInitialised) {
            initGraphics()
        }
        super.draw(batch, parentAlpha)
    }
}