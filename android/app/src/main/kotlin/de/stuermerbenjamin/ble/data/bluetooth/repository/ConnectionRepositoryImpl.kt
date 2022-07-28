package de.stuermerbenjamin.ble.data.bluetooth.repository

import android.bluetooth.BluetoothDevice
import de.stuermerbenjamin.ble.data.bluetooth.BleConnectionService
import de.stuermerbenjamin.ble.data.bluetooth.model.Device
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ConnectionRepositoryImpl(
    private val connectionService: BleConnectionService
) : ConnectionRepository {
    companion object {
        private const val POLL_INTERVAL = 1_000L
    }

    override val isBluetoothConnected: Flow<Boolean> = flow {
        while (true) {
            emit(connectionService.isBluetoothConnected)

            delay(POLL_INTERVAL)
        }
    }

    override val foundDevices: Flow<List<Device>> = flow {
        while (true) {
            val sortedDevices = connectionService.foundDevices.map { it.value }.sortedWith(
                compareBy<Device> {
                    it.name
                }.reversed().thenBy {
                    it.address
                }
            )
            emit(sortedDevices)

            delay(POLL_INTERVAL)
        }
    }

    override suspend fun startScanning() {
        connectionService.startScanning()
    }

    override suspend fun cancelScanning() {
        connectionService.cancelScanning()
    }

    override suspend fun bluetoothConnect(bluetoothDevice: BluetoothDevice) {
        connectionService.bluetoothConnect(bluetoothDevice = bluetoothDevice)
    }

    override suspend fun bluetoothDisconnect() {
        connectionService.bluetoothDisconnect()
    }

    override suspend fun transferFirmware(byteArray: ByteArray) {
        connectionService.transferFirmware(byteArray)
    }
}
