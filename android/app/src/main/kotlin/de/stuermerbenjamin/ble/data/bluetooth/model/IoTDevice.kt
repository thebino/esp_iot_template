package de.stuermerbenjamin.ble.data.bluetooth.model

import java.io.Closeable
import java.lang.ref.WeakReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class IoTDevice {

    internal fun disconnect() {
//        connections.removeObserver(connection)
    }

    class Connection(
        ioTDevice: IoTDevice
    ): Closeable {
        private var ioTDeviceRef: WeakReference<IoTDevice>? = WeakReference(ioTDevice)
        private val deviceLock = ReentrantLock(true)

        /**
         * Disconnect IoT Device and close all ongoing connections.
         */
        override fun close() {
            deviceLock.withLock {
                val device = ioTDeviceRef?.get()
                device?.disconnect()
                ioTDeviceRef = null
            }
        }
    }
}
