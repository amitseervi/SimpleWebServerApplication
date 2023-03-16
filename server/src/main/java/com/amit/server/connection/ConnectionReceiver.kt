package com.amit.server.connection

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ServerSocket

class ConnectionReceiver {
    private val connectionHandler = ConnectionHandler()

    suspend fun receiveNewConnection(serverSocket: ServerSocket) {
        withContext(Dispatchers.IO){
            val socket = serverSocket.accept()
            connectionHandler.handleSocket(socket)
        }
    }
}