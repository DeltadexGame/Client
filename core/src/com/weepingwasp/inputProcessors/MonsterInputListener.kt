package com.deltadex.input_processors

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.deltadex.event_manager.*
import com.deltadex.models.Card
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.deltadex.models.Monster

class MonsterInputListener : InputListener() {
    val touchPos = Vector2()
    val cardOriginalPos = Vector2()
    val tempPos = Vector2()
    var originalZIndex = 0
    var enlargedCard: Card? = null
    var draggingCard = false
    var on = false

    override
    fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
        if(!on && enlargedCard == null) {
            
            val card = (event.listenerActor as Monster).card
            val newEnlargedCard = card.clone()
            newEnlargedCard.removeListener(newEnlargedCard.inputListener)
            newEnlargedCard.scaleBy(1f)
            newEnlargedCard.addListener(object: InputListener() {
                override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor?) {
                    event.listenerActor.addAction(Actions.removeActor())
                }
            })
            val stage = event.listenerActor.stage
            if (stage != null) {
                stage.addActor(newEnlargedCard)
                newEnlargedCard.initGraphics()
                newEnlargedCard.setPosition(stage.width / 2 - newEnlargedCard.image!!.width * newEnlargedCard.scaleX / 2, stage.height / 2 - newEnlargedCard.image!!.height * newEnlargedCard.scaleY / 2)
                enlargedCard = newEnlargedCard
            }
            on = true
        }
    }
    
    override
    fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor?) {
        if(toActor !is Card)
            enlargedCard?.addAction(Actions.removeActor())
        enlargedCard = null
        on = false
    }
}
