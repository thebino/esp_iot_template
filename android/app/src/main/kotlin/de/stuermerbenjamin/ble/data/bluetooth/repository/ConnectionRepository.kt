package de.stuermerbenjamin.ble.data.bluetooth.repository

import android.bluetooth.BluetoothDevice
import de.stuermerbenjamin.ble.data.bluetooth.model.Device
import kotlinx.coroutines.flow.Flow

interface ConnectionRepository {
    val isBluetoothConnected: Flow<Boolean>
    val foundDevices: Flow<List<Device>>

    // scan advertisement
    suspend fun startScanning()
    suspend fun cancelScanning()

    // Bluetooth dis/connect
    suspend fun bluetoothConnect(bluetoothDevice: BluetoothDevice)
    suspend fun bluetoothDisconnect()

    // communication
    suspend fun transferFirmware(byteArray: ByteArray)
}
