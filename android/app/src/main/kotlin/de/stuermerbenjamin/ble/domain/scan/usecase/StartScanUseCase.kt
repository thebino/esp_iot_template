package de.stuermerbenjamin.ble.domain.scan.usecase

import android.Manifest
import androidx.annotation.RequiresPermission
import de.stuermerbenjamin.ble.data.bluetooth.repository.ConnectionRepository

class StartScanUseCase(
    private val connectionRepository: ConnectionRepository
) {
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    suspend operator fun invoke() {
        connectionRepository.startScanning()
    }
}
