package com.amit.server

import android.util.Log
import com.amit.server.connection.ConnectionReceiver
import com.amit.server.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ServerSocket

private const val LOG_TAG = "com.amit.server.Server"

class Server {
    private var serverSocket: ServerSocket? = null
    private var isRunning: Boolean = false
    private var connectionReceiver = ConnectionReceiver()

    suspend fun start(port: Int) {
        if (isRunning) {
            Log.w(LOG_TAG, "server is already running")
            return
        }
        val inetAddress = NetworkUtils.getInetAddress(true)
        if (inetAddress.isSuccess) {
            inetAddress.getOrThrow().let {
                Log.i(LOG_TAG, "Start = ${it.hostName}:${port}")
                serverSocket = ServerSocket(port, 0, it)
                isRunning = true
                run()
            }
        } else {
            Log.e(LOG_TAG, "Error = ${inetAddress.exceptionOrNull()}")
        }
    }

    private suspend fun run() = withContext(Dispatchers.IO) {
        Log.i(LOG_TAG, "Run started")
        while (isRunning) {
            connectionReceiver.receiveNewConnection(serverSocket!!)
        }
        Log.i(LOG_TAG, "Run stopped")
    }

    fun stop() {
        isRunning = false
        serverSocket?.close()
    }
}