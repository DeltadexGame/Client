package com.weepingwasp.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.weepingwasp.Main

fun main (args: Array<String>) {
	val config = LwjglApplicationConfiguration()
	config.width = 1920
	config.height = 1080
	config.title = "Weeping Wasp"
	LwjglApplication(Main(), config)
}
