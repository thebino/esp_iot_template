package de.stuermerbenjamin.ble.data.bluetooth.model

import android.bluetooth.BluetoothDevice
import java.time.Instant

data class Device(
    val name: String,
    val address: String,
    val device: BluetoothDevice? = null,
    val lastSeen: Instant,
)
