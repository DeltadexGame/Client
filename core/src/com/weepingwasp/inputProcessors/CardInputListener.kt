package com.deltadex.input_processors

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.Input
import com.deltadex.models.Card

import com.deltadex.event_manager.*

class CardInputListener : InputListener() {
    val touchPos = Vector2()
    val cardOriginalPos = Vector2()
    val tempPos = Vector2()
    var originalZIndex = 0
    var enlargedCard: Card? = null
    var draggingCard = false
    override
    fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
        if(button == Input.Buttons.LEFT) {
            val stage = event.listenerActor.stage
            stage.actors.removeValue(enlargedCard, false)
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
        if(button == Input.Buttons.LEFT) {
            val stage = event.listenerActor.stage
            tempPos.set(x, y)
            tempPos.set(event.listenerActor.localToStageCoordinates(tempPos))
            var placed = false
            if (tempPos.y in stage.height / 4 .. stage.height/2) {
                placed = true
                when(tempPos.x) {
                    in stage.width/8 .. stage.width/8*3 -> {
                        val card = event.listenerActor as Card
                        val position = card.player!!.cards.indexOf(card)
                        val placeEvent = Event(EventType.PLAYCARD, hashMapOf(
                            "place" to "0",
                            "from" to "${position}"
                        ))
                        pushEvent(placeEvent)
                    }
                    in stage.width/8*3 .. stage.width/8*5 -> {
                        val card = event.listenerActor as Card
                        val position = card.player!!.cards.indexOf(card)
                        val placeEvent = Event(EventType.PLAYCARD, hashMapOf(
                            "place" to "1",
                            "from" to "${position}"
                        ))
                        pushEvent(placeEvent)
                    }
                    in stage.width/8*5 .. stage.width/8*7 -> {
                        val card = event.listenerActor as Card
                        val position = card.player!!.cards.indexOf(card)
                        val placeEvent = Event(EventType.PLAYCARD, hashMapOf(
                            "place" to "2",
                            "from" to "${position}"
                        ))
                        pushEvent(placeEvent)
                    }
                    else -> {placed = false}
                }
            }
            if(!placed) {
                
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
        enlargedCard?.remove()
        tempPos.set(x, y)
        tempPos.set(event.listenerActor.localToStageCoordinates(tempPos))
        event.listenerActor.setPosition(cardOriginalPos.x + tempPos.x - touchPos.x, cardOriginalPos.y + tempPos.y - touchPos.y)
    }

    override
    fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
        enlargedCard = (event.listenerActor as Card).clone()
        enlargedCard!!.initGraphics()
        enlargedCard!!.scaleBy(1f)
        val stage = event.listenerActor.stage
        if(stage != null){
            enlargedCard!!.setPosition(stage.width / 2 - enlargedCard!!.image!!.width * enlargedCard!!.scaleX / 2, stage.height / 2 - enlargedCard!!.image!!.height * enlargedCard!!.scaleY / 2)
            stage.addActor(enlargedCard)
        }
    }

    override
    fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor?) {
        enlargedCard?.remove()
    }
}
