package de.stuermerbenjamin.ble.domain.scan.usecase

import de.stuermerbenjamin.ble.data.bluetooth.model.Device
import de.stuermerbenjamin.ble.data.bluetooth.repository.ConnectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class GetScanResultsUseCase(
    private val connectionRepository: ConnectionRepository
) {
    operator fun invoke(): Flow<List<Device>> = connectionRepository.foundDevices.distinctUntilChanged()
}
