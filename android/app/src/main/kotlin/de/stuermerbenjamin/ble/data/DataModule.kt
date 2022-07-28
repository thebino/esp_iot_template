package de.stuermerbenjamin.ble.data

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import de.stuermerbenjamin.ble.data.bluetooth.BleConnectionService
import de.stuermerbenjamin.ble.data.bluetooth.BleConnectionServiceImpl
import de.stuermerbenjamin.ble.data.bluetooth.repository.ConnectionRepository
import de.stuermerbenjamin.ble.data.bluetooth.repository.ConnectionRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single {
        provideBluetoothManager(
            context = get()
        )
    }
    single {
        provideBleAdapter(
            bleManager = get()
        )
    }
    single<BleConnectionService> {
        BleConnectionServiceImpl(
            context = get(),
            bluetoothAdapter = get(),
        )
    }

    single<ConnectionRepository> {
        ConnectionRepositoryImpl(
            connectionService = get(),
        )
    }
}

private fun provideBluetoothManager(context: Context): BluetoothManager {
    return context.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
}

private fun provideBleAdapter(bleManager: BluetoothManager): BluetoothAdapter? {
    return bleManager.adapter
}
