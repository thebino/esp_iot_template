package de.stuermerbenjamin.ble.data.wifi

import de.stuermerbenjamin.ble.data.wifi.model.SecurityMode

interface WifiConnectionService {
    val isWifiConnected: Boolean

    // wifi station mode
    suspend fun startAccessPoint()
    suspend fun stopAccessPoint()

    // wifi AP mode
    suspend fun connectToNetwork(securityMode: SecurityMode, ssid: String, password: String)
    suspend fun disconnectFromNetwork()
}
