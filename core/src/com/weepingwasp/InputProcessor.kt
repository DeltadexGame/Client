package com.weepingwasp

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class InputProcessor(val storage: Storage): InputAdapter() {
    var camera: OrthographicCamera? = null
    val pressPos = Vector2()
    val cardPressPos = Vector2()
    val touchPoint = Vector3()
    var touchedCard: Card? = null
    override
    fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        touchPoint.set(screenX.toFloat(), screenY.toFloat(), 0f)
        camera?.unproject(touchPoint)
        if (button == Buttons.LEFT) {
            for (card in storage.player.cards) {
                if (card.sprite != null && card.sprite!!.boundingRectangle.contains(touchPoint.x, touchPoint.y)) {
                    touchedCard = card
                    pressPos.set(touchPoint.x, touchPoint.y)
                    cardPressPos.set(card.sprite!!.getX(), card.sprite!!.getY())
                    break
                }
            }
        }
        
        return true
    }

    override
    fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        touchPoint.set(screenX.toFloat(), screenY.toFloat(), 0f)
        camera?.unproject(touchPoint)
        if () {

        }
        touchedCard?.sprite?.setPosition(cardPressPos.x, cardPressPos.y)
        return true
    }

    override
    fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        touchPoint.set(screenX.toFloat(), screenY.toFloat(), 0f)
        camera?.unproject(touchPoint)
        touchedCard?.sprite?.setPosition(cardPressPos.x + touchPoint.x - pressPos.x, cardPressPos.y + touchPoint.y - pressPos.y)
        return true
    }
}