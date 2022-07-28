package de.stuermerbenjamin.ble.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import de.stuermerbenjamin.ble.data.bluetooth.model.Device
import java.time.Instant
import timber.log.Timber

@SuppressLint("MissingPermission")
class BleConnectionServiceImpl(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter,
) : BleConnectionService {
    private var mtu: Int = 0

    override var isBluetoothConnected: Boolean = false
    override var foundDevices: HashMap<String, Device> = HashMap()
    override val bluetoothMTU: Int = mtu

    private var bluetoothGatt: BluetoothGatt? = null

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
            this@BleConnectionServiceImpl.mtu = mtu
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (status == BluetoothGatt.GATT_FAILURE) {
                disconnectGattServer("Bluetooth Gatt Fialure")
                return
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                disconnectGattServer("Disconnected")
                return
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                isBluetoothConnected = true

                gatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                isBluetoothConnected = false
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (status != BluetoothGatt.GATT_SUCCESS) {
                disconnectGattServer("Device service discovery failed, status: $status")
                return
            }
            bluetoothGatt = gatt
            bluetoothGatt?.requestMtu(512)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            characteristic?.let {
                readCharacteristic(it)
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Timber.i("Characteristic read successfully")
                characteristic?.let {
                    readCharacteristic(it)
                }
            } else {
                Timber.e("Characteristic read unsuccessful, status: $status")
                 disconnectGattServer("Couldn't read characteristic")
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Timber.i("Characteristic written successfully")
            } else {
                disconnectGattServer("Characteristic write unsuccessful, status: $status")
            }
        }
    }

    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            addScanResult(result)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                addScanResult(result)
            }
        }

        override fun onScanFailed(error: Int) {
            Timber.e("scan failed with error = $error")
            foundDevices = hashMapOf()
        }

        private fun addScanResult(result: ScanResult) {
            val device = result.device
            foundDevices[device.address] = Device(
                name = device.name ?: "",
                address = device.address,
                device = device,
                lastSeen = Instant.now()
            )
        }
    }

    override suspend fun startScanning() {
        if (bluetoothAdapter.isEnabled) {
            val scanFilter: ScanFilter = ScanFilter.Builder().build()

            val filters: MutableList<ScanFilter> = arrayListOf(scanFilter)

            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                .build()

            bluetoothAdapter.bluetoothLeScanner.startScan(filters, settings, scanCallback)
        }
    }

    override suspend fun cancelScanning() {
        bluetoothAdapter.bluetoothLeScanner.stopScan(scanCallback)
    }

    override suspend fun bluetoothConnect(bluetoothDevice: BluetoothDevice) {
        bluetoothGatt = bluetoothDevice.connectGatt(context, false, bluetoothGattCallback)
    }

    override suspend fun bluetoothDisconnect() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
    }

    override suspend fun transferFirmware(byteArray: ByteArray) {
        // TODO: split firmware file into packages the size of the mtu
    }

    override suspend fun writeCharacteristic() {
        // TODO: handle write
    }

    override suspend fun readCharacteristic() {
        // handle read
    }

    private fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        // TODO: read characteristic
        Timber.e("read characteristic not implemented yet! ${characteristic.value}")
    }

    fun disconnectGattServer(msg: String) {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        isBluetoothConnected = false
    }
}
