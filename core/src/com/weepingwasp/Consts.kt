package com.deltadex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.*
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter

const val width = 1920
const val height = 1080
const val cardWidth = height / 6
const val cardHeight = height / 4

private var font: BitmapFont? = null
private var bigFont: BitmapFont? = null

fun getFont(): BitmapFont {
    if(font == null) {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/Arial.ttf"))
        val parameter = FreeTypeFontParameter()
        parameter.size = 20
        font = generator.generateFont(parameter)
        generator.dispose()
    }
    return font!!
}

fun getBigFont(): BitmapFont {
    if(bigFont == null) {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/Arial.ttf"))
        val parameter = FreeTypeFontParameter()
        parameter.size = 40
        bigFont = generator.generateFont(parameter)
        generator.dispose()
    }
    return bigFont!!
}
