package com.weepingwasp

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.Sprite

class Main: ApplicationAdapter() {
	var batch: SpriteBatch? = null

	var boardImg: Texture? = null
	var boardSprite: Sprite? = null

	var camera: OrthographicCamera? = null

	var cardImg: Texture? = null

	val storage = Storage()

	val inputProcessor = InputProcessor(storage)

	fun createBoardPixmap(): Pixmap {
		val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)

		pixmap.setColor(Color.BROWN)
		pixmap.fill()

		pixmap.setColor(Color.GREEN)
		pixmap.fillCircle(width/4 * 1, height / 2 + height/10, height/16)
		pixmap.fillCircle(width/4 * 2, height / 2 + height/10, height/16)
		pixmap.fillCircle(width/4 * 3, height / 2 + height/10, height/16)

		pixmap.setColor(Color.BLACK)
		pixmap.drawLine(0, height/2, width, height/2)

		pixmap.setColor(Color.RED)
		pixmap.fillCircle(width/4 * 1, height / 2 - height/10, height/16)
		pixmap.fillCircle(width/4 * 2, height / 2 - height/10, height/16)
		pixmap.fillCircle(width/4 * 3, height / 2 - height/10, height/16)

		return pixmap
	}

	override
	fun create() {
		camera = OrthographicCamera();
		camera?.setToOrtho(false, width.toFloat(), height.toFloat());

		batch = SpriteBatch()
		
		val boardPixmap = createBoardPixmap()
		boardImg = Texture(boardPixmap)
		boardPixmap.dispose()
		boardSprite = Sprite(boardImg)

		val cardPixmap = Pixmap(cardWidth, cardHeight, Pixmap.Format.RGBA8888)
		cardPixmap.setColor(Color.TAN)
		cardPixmap.fill()
		cardPixmap.setColor(Color.BLACK)
		cardPixmap.drawRectangle(0, 0, cardWidth, cardHeight)

		cardImg = Texture(cardPixmap)
		cardPixmap.dispose()

		storage.player.addCard(Card())
		storage.player.addCard(Card())
		inputProcessor.camera = camera
		Gdx.input.inputProcessor = inputProcessor
	}

	override
	fun render() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
		batch?.setProjectionMatrix(camera?.combined);

		batch?.begin()
		boardSprite?.draw(batch)
		var index = 0
		for (card in storage.player.cards) {
			if (card.sprite == null) {			
				card.sprite = Sprite(cardImg)
				card.sprite?.translateX(cardWidth * 1.1f * index)
			}
			card.sprite?.draw(batch)
			index ++	
		}
		batch?.end()
	}
	
	override
	fun dispose () {
		batch?.dispose()
		boardImg?.dispose()
	}
}
