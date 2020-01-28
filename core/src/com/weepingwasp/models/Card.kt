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
import com.weepingwasp.inputProcessors.CardInputListener

class Card() : Group() {
    val image = Image(Texture("card.png"))
    var text: String
    set(value) {
        label.setText(value)
    }
    get() = label.getText().toString()
    private val label = Label("TEST", Label.LabelStyle(getFont(), Color.WHITE))
    init {
        addActor(image)
        label.setPosition(42f, 215f)
        addActor(label)
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
