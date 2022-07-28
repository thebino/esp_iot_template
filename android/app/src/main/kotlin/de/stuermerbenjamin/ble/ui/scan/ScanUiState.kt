package de.stuermerbenjamin.ble.ui.scan

import de.stuermerbenjamin.ble.data.bluetooth.model.Device

data class ScanUiState(
    val isBluetoothConnected: Boolean = false,
    val foundDevices: List<Device> = emptyList(),
)
