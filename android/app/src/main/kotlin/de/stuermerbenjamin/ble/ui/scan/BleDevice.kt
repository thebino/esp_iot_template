package de.stuermerbenjamin.ble.ui.scan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.stuermerbenjamin.ble.R
import de.stuermerbenjamin.ble.data.bluetooth.model.Device
import java.time.Duration
import java.time.Instant

@Composable
fun BleDevice(
    modifier: Modifier = Modifier,
    device: Device,
    onDeviceClick: () -> Unit = {}
) {
    val secondsSinceLastSeen = Duration.between(
        device.lastSeen,
        Instant.now(),
    ).seconds

    val name = device.name
    val address = device.address

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onDeviceClick() },
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row {
                DeviceName(name)

                LastSeen(secondsSinceLastSeen)
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = address,
                style = MaterialTheme.typography.overline,
                color = MaterialTheme.colors.secondaryVariant,
            )
        }
    }
}

@Composable
private fun RowScope.DeviceName(name: String) {
    if (name.isEmpty()) {
        Text(
            modifier = Modifier.weight(1f),
            color = Color.Gray,
            text = stringResource(id = R.string.device_name_unknown),
            style = MaterialTheme.typography.h5
        )
    } else {
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
private fun LastSeen(secondsSinceLastSeen: Long) {
    val resources = LocalContext.current.resources

    val timeString = when {
        secondsSinceLastSeen.toInt() > 3600 -> {
            resources.getQuantityString(
                R.plurals.minutes,
                secondsSinceLastSeen.toInt() / 3600,
                secondsSinceLastSeen / 3600
            )
        }
        secondsSinceLastSeen.toInt() > 60 -> {
            resources.getQuantityString(
                R.plurals.minutes,
                secondsSinceLastSeen.toInt() / 60,
                secondsSinceLastSeen / 60
            )
        }
        else -> {
            resources.getQuantityString(
                R.plurals.seconds,
                secondsSinceLastSeen.toInt(),
                secondsSinceLastSeen
            )
        }
    }

    if (secondsSinceLastSeen == 0L) {
        Text(
            text = stringResource(id = R.string.device_last_seen_now),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.secondaryVariant,
        )
    } else {
        Text(
            text = timeString,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.secondaryVariant,
        )
    }
}

@Preview
@Composable
private fun BleDevicePreview() {
    MaterialTheme {
        BleDevice(
            device = Device(
                name = "Device",
                address = "00:ff:11:ee:22",
                device = null,
                lastSeen = Instant.now().minusSeconds(23),
            )
        )
    }
}
