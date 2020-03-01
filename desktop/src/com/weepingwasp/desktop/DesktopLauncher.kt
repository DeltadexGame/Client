package com.deltadex.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.deltadex.game.MyGame

fun main (args: Array<String>) {
	if(args.size < 2) {
		println(args.size)
		System.exit(1)
	}
	val name = args[0]
	val token = args[1]
	println("$name $token")
	val config = LwjglApplicationConfiguration()
	config.width = 1920
	config.height = 1080
	config.title = "DeltaDex"
	LwjglApplication(MyGame(name, token), config)
}
