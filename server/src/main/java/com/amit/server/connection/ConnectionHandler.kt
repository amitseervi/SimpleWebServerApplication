package com.amit.server.connection

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.PrintWriter
import java.lang.StringBuilder
import java.net.Socket

private const val LOG_TAG = "com.amit.server.connection.ConnectionHandler"

class ConnectionHandler {
    suspend fun handleSocket(socket: Socket?) {
        socket ?: return
        withContext(Dispatchers.IO) {
            socket.getInputStream().let { inputStream ->
                val sb=StringBuilder()
                while (inputStream.available()>0){
                    sb.append(inputStream.read().toChar())
                }
                Log.i(LOG_TAG,"Input = ${sb}")
            }
            Log.i(LOG_TAG,"Start writing into socket")
            try {
                PrintWriter(socket.getOutputStream()).let {
                    it.write("HTTP/1.1 200 OK")
                    it.write("\n")
                    it.write("Access-Control-Allow-Origin: *")
                    it.write("\n")
                    it.write("Connection: Keep-Alive")
                    it.write("\n")
                    it.write("Content-Type: text/html; charset=utf-8")
                    it.write("\n\n")
                    it.write("""
                        <HTML>
                        <BODY>
                        <H1>Hello world</H1>
                        </BODY>
                        </HTML>
                    """.trimIndent())
                    it.flush()
                }
                socket.shutdownOutput()
            }catch (e:Exception){
                Log.e(LOG_TAG,"ERROR = $e")
            }
            socket.close()
        }
    }
}