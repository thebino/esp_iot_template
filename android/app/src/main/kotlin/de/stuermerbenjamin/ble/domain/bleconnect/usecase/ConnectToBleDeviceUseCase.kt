package de.stuermerbenjamin.ble.domain.bleconnect.usecase

import android.bluetooth.BluetoothDevice
import de.stuermerbenjamin.ble.data.bluetooth.repository.ConnectionRepository

class ConnectToBleDeviceUseCase(
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(bluetoothDevice: BluetoothDevice) {
        connectionRepository.bluetoothConnect(bluetoothDevice = bluetoothDevice)
    }
}
