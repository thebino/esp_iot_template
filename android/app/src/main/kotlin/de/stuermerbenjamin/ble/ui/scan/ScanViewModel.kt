package de.stuermerbenjamin.ble.ui.scan

import android.Manifest
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.stuermerbenjamin.ble.data.bluetooth.model.Device
import de.stuermerbenjamin.ble.domain.bleconnect.usecase.ConnectToBleDeviceUseCase
import de.stuermerbenjamin.ble.domain.bleconnect.usecase.GetBleConnectionState
import de.stuermerbenjamin.ble.domain.scan.usecase.CancelScanUseCase
import de.stuermerbenjamin.ble.domain.scan.usecase.GetScanResultsUseCase
import de.stuermerbenjamin.ble.domain.scan.usecase.StartScanUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ScanViewModel(
    private val startScanUseCase: StartScanUseCase,
    private val cancelScanUseCase: CancelScanUseCase,
    private val getScanResultsUseCase: GetScanResultsUseCase,
    private val connectToBleDeviceUseCase: ConnectToBleDeviceUseCase,
    private val getBleConnectionState: GetBleConnectionState,
) : ViewModel() {
    val uiState = mutableStateOf(ScanUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getScanResultsUseCase().collect { foundDevices ->
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(foundDevices = foundDevices)
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            getBleConnectionState().collect { isBluetoothConnected ->
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(isBluetoothConnected = isBluetoothConnected)
                }
            }
        }
    }

    fun handleEvent(event: ScanEvent) {
        when (event) {
            ScanEvent.StartScan -> startScanning()
            ScanEvent.StopScan -> stopScanning()
            is ScanEvent.Connect -> connectToDevice(event.device)
        }
    }

    private fun connectToDevice(device: Device) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: show user facing error if device is not available
            device.device?.let {
                connectToBleDeviceUseCase(it)
            }
        }
    }

    private fun startScanning() {
        viewModelScope.launch(Dispatchers.IO) {
            startScanUseCase()
        }
    }

    private fun stopScanning() {
        viewModelScope.launch(Dispatchers.IO) {
            cancelScanUseCase()
        }
    }
}
