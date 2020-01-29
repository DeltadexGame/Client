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

class Card() : Group() {
    val image = Image(Texture("card.png"))
    var text: String
    set(value) {
        label.setText(value)
    }
    get() = label.getText().toString()
    private val label = Label("", Label.LabelStyle(getFont(), Color.WHITE))
    var cardName: String
    set(value) {
        nameLabel.setText(value)
    }
    get() = nameLabel.getText().toString()
    private val nameLabel = Label("Test Name", Label.LabelStyle(getFont(), Color.WHITE))
    var attack: Int
    set(value) {
        attackLabel.setText(value.toString())
    }
    get() = attackLabel.getText().toString().toInt()
    private val attackLabel = Label("1", Label.LabelStyle(getBigFont(), Color.WHITE))
    var health: Int
    set(value) {
        healthLabel.setText(value.toString())
    }
    get() = healthLabel.getText().toString().toInt()
    private val healthLabel = Label("2", Label.LabelStyle(getBigFont(), Color.WHITE))
    var cost: Int
    set(value) {
        costLabel.setText(value.toString())
    }
    get() = costLabel.getText().toString().toInt()
    private val costLabel = Label("3", Label.LabelStyle(getBigFont(), Color.WHITE))
    var maxHealth: Int
    set(value) {
        nameLabel.setText(value)
    }
    get() = nameLabel.getText().toString().toInt()
    init {
        label.setBounds(42f, 122f, 279f, 117f)
        label.setAlignment(Align.topLeft, Align.left)
        label.setWrap(true)
        nameLabel.setBounds(42f, 505f, 279f, 25f)
        nameLabel.setAlignment(Align.bottomLeft, Align.center)
        attackLabel.setBounds(40f, 40f, 56f, 56f)
        attackLabel.setAlignment(Align.center, Align.center)
        healthLabel.setBounds(264f, 40f, 56f, 56f)
        healthLabel.setAlignment(Align.center, Align.center)
        costLabel.setBounds(261f, 441f, 63f, 63f)
        costLabel.setAlignment(Align.center, Align.center)
        addActor(image)
        addActor(label)
        addActor(healthLabel)
        addActor(attackLabel)
        addActor(costLabel)
        addActor(nameLabel)
        this.scaleBy(-0.7f)
        this.addListener(CardInputListener())
    }

    fun clone(): Card {
        val card = Card()
        card.text = this.text
        return card
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