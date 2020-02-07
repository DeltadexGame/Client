package com.deltadex.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.deltadex.Main

fun main (args: Array<String>) {
	val config = LwjglApplicationConfiguration()
	config.width = 1920
	config.height = 1080
	config.title = "DeltaDex"
	LwjglApplication(Main(), config)
}
