package de.stuermerbenjamin.ble.ui.scan

import de.stuermerbenjamin.ble.data.bluetooth.model.Device

sealed interface ScanEvent {
    class Connect(val device: Device): ScanEvent
    object StartScan: ScanEvent
    object StopScan: ScanEvent
}
