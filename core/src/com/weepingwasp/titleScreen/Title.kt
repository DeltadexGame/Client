package com.deltadex.title_screen

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.Gdx
import com.deltadex.Main
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.deltadex.getBigFont
import com.deltadex.getFont
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.graphics.GL20

class Title(val name: String, val token: String, val game: Game) : ScreenAdapter() {
    var stage: Stage? = null

    val disposables = arrayListOf<Disposable>()

    override fun show() {
        val inputMultiplexer = InputMultiplexer()
        
        stage = Stage()
        inputMultiplexer.addProcessor(stage)

        val pixmapBlack = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmapBlack.setColor(Color.BLACK)
        pixmapBlack.fill()
        val textureBlack = Texture(pixmapBlack)
        pixmapBlack.dispose()
        disposables.add(textureBlack)

        val image = Image(textureBlack)
        image.setSize(stage!!.width, stage!!.height)
        stage!!.addActor(image)

        val pixmapGrey = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmapGrey.setColor(Color.GRAY)
        pixmapGrey.fill()
        val textureGrey = Texture(pixmapGrey)
        pixmapGrey.dispose()
        disposables.add(textureGrey)
        val button = TextButton("Play", TextButton.TextButtonStyle(TextureRegionDrawable(textureGrey), TextureRegionDrawable(textureGrey), TextureRegionDrawable(textureGrey), getBigFont()))
        button.align(Align.center)
        button.setSize(stage!!.width/4 * 3, button.width * 1.2f)
        button.setPosition(stage!!.width/2 - button.width / 2, stage!!.height/2 - button.height / 2)
        stage!!.addActor(button)

        val welcome = Label("${name}", Label.LabelStyle(getFont(), Color.WHITE))
        welcome.setPosition(stage!!.width - welcome.width * 1.1f, stage!!.height - welcome.height * 1.1f)
        stage!!.addActor(welcome)

        button.addListener(object: InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                game.screen = Main(name, token, game)
                return true
            }
        })
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun resize(width: Int, height: Int) {
        stage!!.getViewport().update(width, height, true)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage!!.act()
        stage!!.draw()
    }

    override fun dispose() {
        Gdx.input.inputProcessor = null
        for (disposable in disposables) {
            disposable.dispose()
        }
    }
}