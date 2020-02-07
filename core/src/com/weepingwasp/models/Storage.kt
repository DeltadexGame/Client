package com.deltadex.models

import com.badlogic.gdx.scenes.scene2d.*
import com.deltadex.network_manager.NetworkManager

class Storage() {
    val opponent = Player(false, this)
    val player = Player(true, this)
    var stage: Stage? = null
    var enlargedCard: Card? = null
    var networkManager: NetworkManager? = null

    fun addCard(card: Card, isEnemy: Boolean) {
        if (isEnemy) {
            opponent.addCard(card)
        } else {
            player.addCard(card)
        }
        stage!!.addActor(card)
    }
}
