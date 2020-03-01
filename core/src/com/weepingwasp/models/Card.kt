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
import com.deltadex.graphics.CostLabel
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable

class Card(val player: Player) : Group() {

    private var graphicsInitialised = false

    var image: Image? = null

    var picture: Image? = null

    var isKnown = false

    var pictureLocation = ""
    set(value) {
        if(field != "")
            player.storage.assetManager.unload(field)
        field = value
        player.storage.assetManager.load(value, Texture::class.java)
        graphicsInitialised = false
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
        costLabel?.value = value
    }
    get() = field
    private var costLabel: CostLabel? = null

    var maxHealth: Int = 0

    val inputListener = CardInputListener()

    init {
        this.scaleBy(-0.7f)
    }

    fun clone(): Card {
        val card = Card(this.player)
        card.isKnown = this.isKnown
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
        if(this.player.self || this.isKnown) {
            if(player.storage.assetManager.isLoaded("card.png") == true)
                image = Image(player.storage.assetManager.get("card.png", Texture::class.java))
            else
                return
            if(pictureLocation != "") {
                if(player.storage.assetManager.isLoaded(pictureLocation) == true)
                    picture = Image(player.storage.assetManager.get(pictureLocation, Texture::class.java))
                else{
                    return
                }
            } else
                picture = Image()
            this.addListener(inputListener)
            label = Label(text, Label.LabelStyle(getFont(), Color.WHITE))
            nameLabel = Label(cardName, Label.LabelStyle(getFont(), Color.WHITE))
            attackLabel = AttackLabel(attack, player.storage.assetManager)
            healthLabel = HealthLabel(health, player.storage.assetManager)
            costLabel = CostLabel(cost, player.storage.assetManager)
            label?.setBounds(42f, 122f, 279f, 117f)
            label?.setAlignment(Align.topLeft, Align.left)
            label?.setWrap(true)
            nameLabel?.setBounds(42f, 505f, 279f, 25f)
            nameLabel?.setAlignment(Align.bottomLeft, Align.center)
            attackLabel?.setBounds(36f, 36f, 64f, 64f)
            healthLabel?.setBounds(260f, 36f, 64f, 64f)
            costLabel?.setBounds(261f, 441f, 63f, 63f)
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
            if(player.storage.assetManager.isLoaded("cardBack.png") == true)
                image = Image(player.storage.assetManager.get("cardBack.png", Texture::class.java))
            else
                return
            this.moveBy(stage.width - image!!.width * this.scaleX, stage.height - image!!.height * this.scaleY)
            addActor(image)
        }
        graphicsInitialised = true
    }

    override
    fun draw(batch: Batch, parentAlpha: Float) {
        if(!graphicsInitialised) {
            initGraphics()
        }
        super.draw(batch, parentAlpha)
    }

    override
    fun hit(x: Float, y: Float, touchable: Boolean): Actor? {
        if (touchable && this.getTouchable() == Touchable.disabled) {
            return null
        } else if (!this.isVisible()) {
            return null
        } else {
            return if(image != null && x <= image!!.width && y <= image!!.height && x >= 0 && y>= 0) this else null
        }
    }
}