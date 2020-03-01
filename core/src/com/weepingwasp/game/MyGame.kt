package com.deltadex.game

import com.badlogic.gdx.Game
import com.deltadex.title_screen.Title

class MyGame(val name: String, val token: String): Game() {
    override fun create() {
        setScreen(Title(name, token, this))
    }

    override fun dispose() {

    }
}