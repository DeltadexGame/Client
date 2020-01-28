package com.weepingwasp.network_manager

import java.net.*
import java.io.*
import com.google.gson.*

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
                val read = in_stream.readLine()
                val packet: Packet = Gson().fromJson(read, Packet::class.java)
                received(packet)
            }
        }.start()
    }

    fun sendPacket(packet: Packet) {
        val output = Gson().toJson(packet)
        out.println(output)
    }

}