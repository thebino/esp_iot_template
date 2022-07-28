package de.stuermerbenjamin.ble.ui.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import de.stuermerbenjamin.ble.AppDestination
import de.stuermerbenjamin.ble.R
import de.stuermerbenjamin.ble.data.bluetooth.model.Device
import java.time.Instant
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

/**
 * Scan for nearby bluetooth LE devices and show a list.
 */
@Composable
fun ScanScreen(
    navController: NavHostController = rememberAnimatedNavController()
) {
    val viewModel: ScanViewModel = getViewModel()

    if (viewModel.uiState.value.isBluetoothConnected) {
        navController.navigate(AppDestination.Connected.route) {
            popUpTo(AppDestination.Connected.route)
        }
    }

    ScanScreen(
        uiState = viewModel.uiState.value,
        handleEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    uiState: ScanUiState,
    handleEvent: (event: ScanEvent) -> Unit
) {
    val permissionState = rememberPermissionState(
        android.Manifest.permission.BLUETOOTH_SCAN
    )
    LaunchedEffect(Unit) {
        Timber.e("LaunchedEffect")

        if (permissionState.hasPermission) {
            handleEvent(ScanEvent.StartScan)
        } else {
            // TODO: start scan after permission has been granted
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        onDispose {
            handleEvent(ScanEvent.StopScan)
        }
    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.scan_title))
                },
                backgroundColor = MaterialTheme.colors.surface
            )
        },
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(false),
            onRefresh = { handleEvent(ScanEvent.StartScan) },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    scale = true,
                    backgroundColor = MaterialTheme.colors.primary,
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.scan_description)
                )

                if (uiState.foundDevices.isNotEmpty()) {
                    LazyColumn {
                        items(
                            count = uiState.foundDevices.size,
                        ) { item ->
                            val device = uiState.foundDevices[item]

                            BleDevice(
                                device = device,
                                onDeviceClick = {
                                    handleEvent(ScanEvent.Connect(device))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScanScreenPreview() {
    MaterialTheme {
        ScanScreen(
            uiState = ScanUiState(
                foundDevices = listOf(
                    Device(
                        name = "IoT Device",
                        address = "aa:bb:cc:dd:ee:ff",
                        lastSeen = Instant.now().minusSeconds(1873),
                        device = null,
                    ),
                    Device(
                        name = "IoT Device",
                        address = "aa:bb:cc:dd:ee:ff",
                        lastSeen = Instant.now().minusSeconds(23),
                        device = null,
                    ),
                )
            ),
            handleEvent = {}
        )
    }
}
