package com.deltadex.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.deltadex.models.Storage

class Graphics(val storage: Storage) {
    fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        storage.stage!!.act()
        storage.stage!!.draw()
    }
}
