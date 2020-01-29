package com.weepingwasp.models

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
import com.weepingwasp.input_processors.CardInputListener
import com.badlogic.gdx.graphics.g2d.Batch

class Card() : Group() {

    private var graphicsInitialised = false

    var image: Image? = null

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
        this.addListener(inputListener)
    }

    fun clone(): Card {
        val card = Card()
        card.text = this.text
        card.cardName = this.cardName
        card.health = this.health
        card.attack = this.attack
        card.maxHealth = this.maxHealth
        card.cost = this.cost
        return card
    }

    fun initGraphics() {
        label = Label("", Label.LabelStyle(getFont(), Color.WHITE))
        nameLabel = Label("", Label.LabelStyle(getFont(), Color.WHITE))
        attackLabel = Label("0", Label.LabelStyle(getBigFont(), Color.WHITE))
        healthLabel = Label("0", Label.LabelStyle(getBigFont(), Color.WHITE))
        costLabel = Label("0", Label.LabelStyle(getBigFont(), Color.WHITE))
        image = Image(Texture("card.png"))

        label!!.setBounds(42f, 122f, 279f, 117f)
        label!!.setAlignment(Align.topLeft, Align.left)
        label!!.setWrap(true)
        nameLabel!!.setBounds(42f, 505f, 279f, 25f)
        nameLabel!!.setAlignment(Align.bottomLeft, Align.center)
        attackLabel!!.setBounds(40f, 40f, 56f, 56f)
        attackLabel!!.setAlignment(Align.center, Align.center)
        healthLabel!!.setBounds(264f, 40f, 56f, 56f)
        healthLabel!!.setAlignment(Align.center, Align.center)
        costLabel!!.setBounds(261f, 441f, 63f, 63f)
        costLabel!!.setAlignment(Align.center, Align.center)
        addActor(image)
        addActor(label)
        addActor(healthLabel)
        addActor(attackLabel)
        addActor(costLabel)
        addActor(nameLabel)
        graphicsInitialised = true
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