package de.stuermerbenjamin.ble.domain.scan.usecase

import de.stuermerbenjamin.ble.data.bluetooth.repository.ConnectionRepository

class CancelScanUseCase(
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke() {
        connectionRepository.cancelScanning()
    }
}
