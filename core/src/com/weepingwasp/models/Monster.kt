package com.deltadex.models

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.deltadex.graphics.AttackLabel
import com.deltadex.graphics.HealthLabel
import com.deltadex.input_processors.MonsterInputListener

class Monster(val card: Card, val position: Int, val own: Boolean) : Group() {
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

    val inputListener = MonsterInputListener()

    override
    fun draw(batch: Batch, parentAlpha: Float) {
        if (!graphicsInitialised) {
            initGraphics()
        }
        super.draw(batch, parentAlpha)
    }

    fun initGraphics() {
        attackLabel = AttackLabel(attack, card.player.storage.assetManager)
        healthLabel = HealthLabel(health, card.player.storage.assetManager)
        if(card.player.storage.assetManager.isLoaded(pictureLocation) == true) {
            picture = Image(card.player.storage.assetManager.get(pictureLocation, Texture::class.java))
            picture!!.setSize(256f, 256f)
        } else{
            return
        }
        attackLabel!!.setBounds(picture!!.width / 2 - 64f, 0f, 64f, 64f)
        healthLabel!!.setBounds(picture!!.width / 2, 0f, 64f, 64f)
        addActor(picture)
        addActor(attackLabel)
        addActor(healthLabel)
        graphicsInitialised = true
        val forwardMove = if (own) -1 else 1
        setOrigin(picture!!.width / 2, picture!!.height / 2)
        setScale(0.5f, 0.5f)
        if(stage != null)
            moveBy((stage.width / 4 * (position + 1) - picture!!.width / 2).toFloat(), (stage.height / 2 + stage.height / 10 * forwardMove - picture!!.height / 2).toFloat())
        addListener(inputListener)
        graphicsInitialised = true
    }
}
