package com.deltadex.network_manager

import java.net.*
import java.io.*
import com.google.gson.*
import com.badlogic.gdx.Gdx

data class Packet(val PacketID: Int, val Content: Any)

class NetworkManager(ip: String, port: Int, received: (Packet) -> Unit) {
    val socket: Socket
    val out: PrintWriter

    init {
        socket = Socket(ip, port)
        out = PrintWriter(socket.getOutputStream(), true)
        Thread {
            var in_stream = BufferedReader(InputStreamReader(socket.getInputStream()))
            while(true) {
                val read: String? = in_stream.readLine()
                val packet: Packet? = Gson().fromJson(read, Packet::class.java)
                if(packet == null) {
                    Gdx.app.exit()
                    continue
                }
                received(packet)
            }
        }.start()
    }

    fun sendPacket(packet: Packet) {
        val output = Gson().toJson(packet)
        out.println(output)
    }

}

enum class PacketID(val id: Int) {
    AUTH_INFO(1),
    AUTH_RESULT(2),
    GAME_INIT(101),
    STARTING_HAND(102),
    PLAY_CARD(201),
    PLAY_CARD_RESULT(202),
    OPPONENT_PLAY_CARD(203),
    END_TURN(301),
}