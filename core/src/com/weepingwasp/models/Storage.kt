package com.weepingwasp.models

import com.badlogic.gdx.scenes.scene2d.*

class Storage {
    val opponent = Player(true)
    val player = Player(false)
    var stage: Stage? = null
    var enlargedCard: Card? = null

    fun addCard(card: Card, isEnemy: Boolean) {
        if (isEnemy) {
            opponent.addCard(card)
        } else {
            player.addCard(card)
        }
        stage!!.addActor(card)
    }
}
