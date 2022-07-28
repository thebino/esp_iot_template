package de.stuermerbenjamin.ble.ui.provisioning

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ApModeScreen(
    isAccessPointRunning: Boolean = false,
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

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "Provide a WIFI network to connect your mobile device to your IoT Device."
        )

        if (isAccessPointRunning) {
            AccessPointStopButton()
        } else {
            AccessPointStartButton()
        }
    }
}

@Composable
private fun AccessPointStartButton(
    onStartClicked: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        onClick = { onStartClicked() }) {
        Text(text = "Start Access Point")
    }
}


@Composable
private fun AccessPointStopButton(
    onStopClicked: () -> Unit = {}
) {
    Button(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        onClick = { onStopClicked() }) {
        Text(text = "Stop Access Point")
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ApModeScreenPreview() {
    MaterialTheme {
        ApModeScreen()
    }
}
