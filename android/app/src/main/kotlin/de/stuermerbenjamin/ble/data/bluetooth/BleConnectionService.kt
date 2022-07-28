package de.stuermerbenjamin.ble.data.bluetooth

import android.bluetooth.BluetoothDevice
import de.stuermerbenjamin.ble.data.bluetooth.model.Device

interface BleConnectionService {
    val isBluetoothConnected: Boolean
    val foundDevices: HashMap<String, Device>
    val bluetoothMTU: Int

    // scan advertisement
    suspend fun startScanning()
    suspend fun cancelScanning()

    // Bluetooth dis/connect
    suspend fun bluetoothConnect(bluetoothDevice: BluetoothDevice)
    suspend fun bluetoothDisconnect()

    // communication
    suspend fun transferFirmware(byteArray: ByteArray)
    suspend fun writeCharacteristic()
    suspend fun readCharacteristic()
}
