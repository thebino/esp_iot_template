package de.stuermerbenjamin.ble.domain

import de.stuermerbenjamin.ble.domain.bleconnect.usecase.ConnectToBleDeviceUseCase
import de.stuermerbenjamin.ble.domain.bleconnect.usecase.GetBleConnectionState
import de.stuermerbenjamin.ble.domain.scan.usecase.CancelScanUseCase
import de.stuermerbenjamin.ble.domain.scan.usecase.GetScanResultsUseCase
import de.stuermerbenjamin.ble.domain.scan.usecase.StartScanUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { StartScanUseCase(connectionRepository = get()) }

    factory { CancelScanUseCase(connectionRepository = get()) }

    factory { GetScanResultsUseCase(connectionRepository = get()) }

    factory { ConnectToBleDeviceUseCase(connectionRepository = get()) }

    factory { GetBleConnectionState(connectionRepository = get()) }
}
