package com.deltadex.graphics

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.deltadex.getBigFont
import com.badlogic.gdx.assets.AssetManager

open class SpecialLabel(val boxTexture: String, value: Int, assetManager: AssetManager) : Group() {

    var value: Int = value
    get() = field
    set(value) {
        field = value
        label.setText(value.toString())
    }
    val picture = Image(assetManager.get(boxTexture, Texture::class.java))
    val label = Label(value.toString(), Label.LabelStyle(getBigFont(), Color.WHITE))

    init {
        this.addActor(picture)
        label.setBounds(0f, 0f, picture.width, picture.height)
        label.setAlignment(Align.center, Align.center)
        this.addActor(label)
    }
}

class HealthLabel(value: Int, assetManager: AssetManager) : SpecialLabel("healthBox.png", value, assetManager)

class AttackLabel(value: Int, assetManager: AssetManager) : SpecialLabel("attackBox.png", value, assetManager)

class CostLabel(value: Int, assetManager: AssetManager) : SpecialLabel("costBox.png", value, assetManager)
