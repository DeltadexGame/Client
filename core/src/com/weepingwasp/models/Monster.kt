package com.deltadex.models

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.deltadex.getBigFont
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.deltadex.graphics.HealthLabel
import com.deltadex.graphics.AttackLabel

class Monster(val card: Card, val position: Int, val own: Boolean): Group() {
    var attack = card.attack
    set(value) {
        field = value
        attackLabel?.value = value
    }
    get() = field
    var attackLabel: AttackLabel? = null

    var health = card.health
    set(value) {
        field = value
        healthLabel?.value = value
    }
    get() = field
    var healthLabel: HealthLabel? = null

    var maxHealth = card.maxHealth

    val pictureLocation = card.pictureLocation
    var picture: Image? = null

    var graphicsInitialised = false

    override
    fun draw(batch: Batch, parentAlpha: Float) {
        if(!graphicsInitialised) {
            initGraphics()
        }
        super.draw(batch, parentAlpha)
    }

    fun initGraphics() {
        attackLabel = AttackLabel(attack)
        healthLabel = HealthLabel(health)
        picture = Image(Texture(pictureLocation))
        attackLabel!!.setBounds(picture!!.width/2-64f, 0f, 64f, 64f)
        healthLabel!!.setBounds(picture!!.width/2, 0f, 64f, 64f)
        addActor(picture)
        addActor(attackLabel)
        addActor(healthLabel)
        graphicsInitialised = true
        var tempVector = Vector2(picture!!.width, picture!!.height)
        tempVector = this.localToStageCoordinates(tempVector)
        val forwardMove = if(own) -1 else 1
        setOrigin(picture!!.width/2, picture!!.height/2)
        setScale(0.5f, 0.5f)
        moveBy((stage.width / 4 * (position + 1) - picture!!.width/2).toFloat(), (stage.height / 2 + stage.height / 10 * forwardMove - picture!!.height/2).toFloat())
    }
}