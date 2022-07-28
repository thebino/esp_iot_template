package de.stuermerbenjamin.ble.ui.connected

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.stuermerbenjamin.ble.domain.bleconnect.usecase.GetBleConnectionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConnectedViewModel(
    private val getBleConnectionState: GetBleConnectionState,
): ViewModel() {
    val uiState = mutableStateOf(ConnectedUiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getBleConnectionState().collect { isBluetoothConnected ->
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(isBluetoothConnected = isBluetoothConnected)
                }
            }
        }
    }

    fun handleEvent(event: ConnectEvent) {
        when (event) {
            ConnectEvent.ConnectToWifiEvent -> connectToWifi()
            ConnectEvent.CreateAccessPointEvent -> startAccessPoint()
            ConnectEvent.ReadEvent -> TODO()
            ConnectEvent.UpdateFirmwareEvent -> TODO()
            ConnectEvent.WriteEvent -> TODO()
        }
    }

    private fun connectToWifi() {
        // TODO: show wifi provisioning dialog
        // TODO: send wifi credentials to IoT device
    }

    private fun startAccessPoint() {
        // TODO: initiate station mode on IoT device
        // TODO: search for IoT Wifi
        // TODO: ask user to join the IoT Wifi
    }

    private fun readDataFromIoTDevice() {
        // TODO: read data characteristic
    }

    private fun writeDataToIoTDevice() {
        // TODO: show dialog to write data
    }
}
