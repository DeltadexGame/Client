package com.deltadex.input_processors

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.deltadex.event_manager.*
import com.deltadex.models.Card
import com.badlogic.gdx.scenes.scene2d.actions.Actions

class CardInputListener : InputListener() {
    val touchPos = Vector2()
    val cardOriginalPos = Vector2()
    val tempPos = Vector2()
    var originalZIndex = 0
    var enlargedCard: Card? = null
    var draggingCard = false
    var on = false
    override
    fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if (button == Input.Buttons.LEFT) {
            enlargedCard?.addAction(Actions.removeActor())
            enlargedCard = null
            touchPos.set(x, y)
            touchPos.set(event.listenerActor.localToStageCoordinates(touchPos))
            cardOriginalPos.set(event.listenerActor.getX(), event.listenerActor.getY())
            event.listenerActor.scaleBy(0.1f)
            originalZIndex = event.listenerActor.zIndex
            event.listenerActor.zIndex = 1000
        }
        return true
    }

    override
    fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
        if (button == Input.Buttons.LEFT) {
            val stage = event.listenerActor.stage
            tempPos.set(x, y)
            tempPos.set(event.listenerActor.localToStageCoordinates(tempPos))
            var placed = false
            if (((event.listenerActor) as Card).player.myTurn && tempPos.y in stage.height / 4..stage.height / 2) {
                placed = true
                when (tempPos.x) {
                    in stage.width / 8..stage.width / 8 * 3 -> {
                        val card = event.listenerActor as Card
                        val position = card.player.cards.indexOf(card)
                        val placeEvent = Event(EventType.PLAYCARD, hashMapOf(
                            "place" to "0",
                            "from" to "$position"
                        ))
                        pushEvent(placeEvent)
                    }
                    in stage.width / 8 * 3..stage.width / 8 * 5 -> {
                        val card = event.listenerActor as Card
                        val position = card.player.cards.indexOf(card)
                        val placeEvent = Event(EventType.PLAYCARD, hashMapOf(
                            "place" to "1",
                            "from" to "$position"
                        ))
                        pushEvent(placeEvent)
                    }
                    in stage.width / 8 * 5..stage.width / 8 * 7 -> {
                        val card = event.listenerActor as Card
                        val position = card.player.cards.indexOf(card)
                        val placeEvent = Event(EventType.PLAYCARD, hashMapOf(
                            "place" to "2",
                            "from" to "$position"
                        ))
                        pushEvent(placeEvent)
                    }
                    else -> { placed = false }
                }
            }
            if (!placed) {
                resetLocation(event.listenerActor)
            }
        }
    }

    fun resetLocation(actor: Actor) {
        actor.setPosition(cardOriginalPos.x, cardOriginalPos.y)
        actor.scaleBy(-0.1f)
        actor.zIndex = originalZIndex
    }

    override
    fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
        enlargedCard?.addAction(Actions.removeActor())
        enlargedCard = null
        tempPos.set(x, y)
        tempPos.set(event.listenerActor.localToStageCoordinates(tempPos))
        event.listenerActor.setPosition(cardOriginalPos.x + tempPos.x - touchPos.x, cardOriginalPos.y + tempPos.y - touchPos.y)
    }

    override
    fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
        if(!on && enlargedCard == null) {
            
            val card = (event.listenerActor as Card)
            val newEnlargedCard = card.clone()
            newEnlargedCard.removeListener(newEnlargedCard.inputListener)
            newEnlargedCard.scaleBy(1f)
            val stage = event.listenerActor.stage
            if (stage != null) {
                newEnlargedCard.setPosition(stage.width / 2 - card.image!!.width * newEnlargedCard.scaleX / 2, stage.height / 2 - card.image!!.height * newEnlargedCard.scaleY / 2)
                stage.addActor(newEnlargedCard)
                enlargedCard = newEnlargedCard
            }
            on = true
        }
    }
    
    override
    fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor?) {
        enlargedCard?.addAction(Actions.removeActor())
        enlargedCard = null
        on = false
    }
}
