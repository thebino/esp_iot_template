package de.stuermerbenjamin.ble.domain.bleconnect.usecase

import de.stuermerbenjamin.ble.data.bluetooth.repository.ConnectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class GetBleConnectionState(
    private val connectionRepository: ConnectionRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return connectionRepository.isBluetoothConnected.distinctUntilChanged()
    }
}
