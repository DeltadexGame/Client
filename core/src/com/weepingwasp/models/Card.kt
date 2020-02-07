package com.deltadex.models

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.*
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.deltadex.input_processors.CardInputListener
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

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
        attackLabel?.setText(value)
    }
    get() = field
    private var attackLabel: Label? = null

    var health: Int = 0
    set(value) {
        field = value
        healthLabel?.setText(value)
    }
    get() = field
    private var healthLabel: Label? = null

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
                attackLabel = Label(attack.toString(), Label.LabelStyle(getBigFont(), Color.WHITE))
                healthLabel = Label(health.toString(), Label.LabelStyle(getBigFont(), Color.WHITE))
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
                attackLabel?.setBounds(40f, 40f, 56f, 56f)
                attackLabel?.setAlignment(Align.center, Align.center)
                healthLabel?.setBounds(264f, 40f, 56f, 56f)
                healthLabel?.setAlignment(Align.center, Align.center)
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
                var tempVector = Vector2(image!!.width, image!!.height)
                tempVector = this.localToParentCoordinates(tempVector)
                this.moveBy(Gdx.graphics.width - tempVector.x, Gdx.graphics.height - tempVector.y)
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

fun getFont(): BitmapFont {
    val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/Arial.ttf"))
    val parameter = FreeTypeFontParameter()
    parameter.size = 20
    val font = generator.generateFont(parameter)
    generator.dispose()
    return font
}

fun getBigFont(): BitmapFont {
    val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/Arial.ttf"))
    val parameter = FreeTypeFontParameter()
    parameter.size = 40
    val font = generator.generateFont(parameter)
    generator.dispose()
    return font
}