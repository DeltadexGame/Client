package com.deltadex.network_manager

import com.badlogic.gdx.Gdx
import com.google.gson.*
import java.io.*
import java.net.*

data class Packet(val PacketID: Int, val Content: Any)

class NetworkManager(ip: String, port: Int, received: (Packet) -> Unit) {
    val socket: Socket
    val out: PrintWriter

    init {
        socket = Socket(ip, port)
        out = PrintWriter(socket.getOutputStream(), true)
        Thread {
            var in_stream = BufferedReader(InputStreamReader(socket.getInputStream()))
            while (true) {
                val read: String? = in_stream.readLine()
                val packet: Packet? = Gson().fromJson(read, Packet::class.java)
                if (packet == null) {
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
    SELF_INIT(101),
    OPPONENT_INIT(102),
    STARTING_HAND(103),
    OPPONENT_STARTING_HAND(104),
    PLAY_CARD(201),
    PLAY_CARD_RESULT(202),
    OPPONENT_PLAY_CARD(203),
    CHANGE_ENERGY(204),
    MONSTER_SPAWN(205),
    YOU_DRAW_CARD(206),
    ENEMY_DRAW_CARD(207),
    END_TURN(301),
    END_TURN_MONSTER_ATTACKED(302),
    END_TURN_PLAYER_ATTACKED(303),
    START_TURN(401),
    PACKET_NOT_FOUND(404),
}
