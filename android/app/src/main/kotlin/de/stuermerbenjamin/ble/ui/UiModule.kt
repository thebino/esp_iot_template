package de.stuermerbenjamin.ble.ui

import de.stuermerbenjamin.ble.ui.connected.ConnectedViewModel
import de.stuermerbenjamin.ble.ui.scan.ScanViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel {
        ScanViewModel(
            startScanUseCase = get(),
            cancelScanUseCase = get(),
            getScanResultsUseCase = get(),
            connectToBleDeviceUseCase = get(),
            getBleConnectionState = get(),
        )
    }

    viewModel {
        ConnectedViewModel(
            getBleConnectionState = get(),
        )
    }
}
