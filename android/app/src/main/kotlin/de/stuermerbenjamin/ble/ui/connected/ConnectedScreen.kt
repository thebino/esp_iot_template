package de.stuermerbenjamin.ble.ui.connected

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import de.stuermerbenjamin.ble.AppDestination
import de.stuermerbenjamin.ble.ui.theme.AppTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun ConnectedScreen(
    navController: NavHostController = rememberAnimatedNavController()
) {
    val viewModel: ConnectedViewModel = getViewModel()

    if (!viewModel.uiState.value.isBluetoothConnected) {
        navController.navigate(AppDestination.Scan.route)
    }

    ConnectedScreen(
        uiState = viewModel.uiState.value,
        handleEvent = viewModel::handleEvent,
    )
}

@Composable
fun ConnectedScreen(
    uiState: ConnectedUiState, handleEvent: (event: ConnectEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = if (uiState.isBluetoothConnected) "Connected" else "Disconnected")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.isBluetoothConnected,
            onClick = {
                handleEvent(ConnectEvent.CreateAccessPointEvent)
            }) {
            Text(text = "Create access point")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.isBluetoothConnected,
            onClick = {
                handleEvent(ConnectEvent.ConnectToWifiEvent)
            }) {
            Text(text = "Connect to Wifi")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.isBluetoothConnected,
            onClick = {
                handleEvent(ConnectEvent.UpdateFirmwareEvent)
            }) {
            Text(text = "Update firmware")
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                enabled = uiState.isBluetoothConnected,
                onClick = {
                    handleEvent(ConnectEvent.ReadEvent)
                }) {
                Text(text = "Read")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier.weight(1f),
                enabled = uiState.isBluetoothConnected,
                onClick = {
                    handleEvent(ConnectEvent.WriteEvent)
                }) {
                Text(text = "Write")
            }
        }
    }
}

@Preview
@Composable
private fun ConnectedScreenPreview() {
    AppTheme {
        ConnectedScreen(uiState = ConnectedUiState(true), handleEvent = {})
    }
}
