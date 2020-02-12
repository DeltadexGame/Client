package com.deltadex.graphics

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.graphics.Color
import com.deltadex.getBigFont
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align

open class SpecialLabel(val boxTexture: String, value: Int): Group() {

    var value: Int = value
    get() = field
    set(value) {
        field = value
        label.setText(value.toString())
    }
    val picture = Image(Texture(boxTexture))
    val label = Label(value.toString(), Label.LabelStyle(getBigFont(), Color.WHITE))

    init {
        this.addActor(picture)
        label.setBounds(0f, 0f, picture.width, picture.height)
        label.setAlignment(Align.center, Align.center)
        this.addActor(label)
    }
}

class HealthLabel(value: Int): SpecialLabel("healthBox.png", value) {
    
}

class AttackLabel(value: Int): SpecialLabel("attackBox.png", value) {
    
}

class CostLabel(value: Int): SpecialLabel("costBox.png", value) {
    
}