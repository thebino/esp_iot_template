package de.stuermerbenjamin.ble.ui.provisioning

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StationModeScreen(
    isConnectedToNetwork: Boolean = false,
    ssid: String = "",
) {
    Column(Modifier.padding(8.dp)) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "Device Connection",
            style = TextStyle(
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
            )
        )
        if (isConnectedToNetwork) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "Disconnect the IoT device from a nearby WIFI network."
            )

            WifiSSID(
                enabled = false,
                ssid = ssid
            )

            WifiDisconnectButton()
        } else {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "Connect the IoT device to a nearby WIFI network."
            )

            WifiSecurityType()

            WifiSSID()

            WifiPassword()

            WifiConnectButton()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun StationModeScreenPreview() {
    MaterialTheme {
        StationModeScreen(
            isConnectedToNetwork = false,
            ssid = "GuestWifi"
        )
    }
}

@Composable
private fun WifiSecurityType() {
    val radioOptions = listOf("Open", "WEP", "WPA")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[2]) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        radioOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                        }
                    )
                    .padding(horizontal = 4.dp),
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) }
                )
                Text(
                    modifier = Modifier.align(alignment = Alignment.CenterVertically),
                    text = text,
                    style = MaterialTheme.typography.body1.merge(),
                )
            }
        }
    }
}

@Composable
private fun WifiSSID(
    enabled: Boolean = true,
    ssid: String = ""
) {
    var input by rememberSaveable { mutableStateOf(ssid) }
    TextField(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        enabled = enabled,
        label = {
            Text(text = "Enter SSID")
        },
        value = input,
        onValueChange = { newText ->
            input = newText.trimStart { it == '0' }
        }
    )
}

@Composable
private fun WifiPassword() {
    var password by rememberSaveable { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        value = password,
        onValueChange = { password = it },
        label = { Text("Enter password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
private fun WifiConnectButton(
    onConnectClicked: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        onClick = { onConnectClicked() }) {
        Text(text = "Connect to wifi")
    }
}

@Composable
private fun WifiDisconnectButton(
    onDisconnectClicked: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        onClick = { onDisconnectClicked() }) {
        Text(text = "Disconnect from wifi")
    }
}
