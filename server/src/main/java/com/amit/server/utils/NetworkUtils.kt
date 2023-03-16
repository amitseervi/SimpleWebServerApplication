package com.amit.server.utils

import android.util.Log
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface

private const val LOG_TAG = "com.amit.server.utils.NetworkUtils"

object NetworkUtils {
    fun getInetAddress(useIpv4:Boolean): Result<InetAddress> {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        if (networkInterfaces == null) {
            Log.i(LOG_TAG, "Network interfaces list is null")
            return Result.failure(Throwable("No network interface found"))
        }
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            if (!networkInterface.isLoopback && !networkInterface.isVirtual && networkInterface.isUp) {
                if (networkInterface.name.contains("wlan0")) {
                    val wifiNetworkInetAddress = networkInterface.inetAddresses
                    if (wifiNetworkInetAddress != null) {
                        while (wifiNetworkInetAddress.hasMoreElements()) {
                            val inetAddress = wifiNetworkInetAddress.nextElement()
                            if(!inetAddress.isLoopbackAddress){
                                if(useIpv4){
                                    if(inetAddress is Inet4Address) {
                                        return Result.success(inetAddress)
                                    }
                                }else{
                                    if(inetAddress is Inet6Address) {
                                        return Result.success(inetAddress)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.failure(Throwable("Not implemented yet"))
    }
}