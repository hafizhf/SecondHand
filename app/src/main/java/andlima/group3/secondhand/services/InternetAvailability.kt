package andlima.group3.secondhand.services

import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket
import javax.net.SocketFactory

object InternetAvailability {
    fun check(socketFactory: SocketFactory): Boolean {
        return try {
            Log.d("Checking connection", "Pinging to Google on port 53")
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}