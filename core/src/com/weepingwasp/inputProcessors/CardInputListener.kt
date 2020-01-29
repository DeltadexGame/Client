package com.weepingwasp.input_processors

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.Input
import com.weepingwasp.models.Card

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
                        println("Left")
                    }
                    in stage.width/8*3 .. stage.width/8*5 -> {
                        println("Middle")
                    }
                    in stage.width/8*5 .. stage.width/8*7 -> {
                        println("Right")
                    }
                    else -> {placed = false}
                }
            }
            if(!placed) {
                event.listenerActor.setPosition(cardOriginalPos.x, cardOriginalPos.y)
                event.listenerActor.scaleBy(-0.1f)
                event.listenerActor.zIndex = originalZIndex
            }
            
        }
    }

    override
    fun touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int) {
        val stage = event.listenerActor.stage
        stage.actors.removeValue(enlargedCard, false)
        tempPos.set(x, y)
        tempPos.set(event.listenerActor.localToStageCoordinates(tempPos))
        event.listenerActor.setPosition(cardOriginalPos.x + tempPos.x - touchPos.x, cardOriginalPos.y + tempPos.y - touchPos.y)
    }

    override
    fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
        enlargedCard = (event.listenerActor as Card).clone()
        enlargedCard!!.scaleBy(1f)
        val stage = event.listenerActor.stage
        enlargedCard!!.setPosition(stage.width / 2 - enlargedCard!!.image.width * enlargedCard!!.scaleX / 2, stage.height / 2 - enlargedCard!!.image.height * enlargedCard!!.scaleY / 2)
        stage.addActor(enlargedCard)
    }

    override
    fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor?) {
        val stage = event.listenerActor.stage
        stage.actors.removeValue(enlargedCard!!, false)
    }
}
