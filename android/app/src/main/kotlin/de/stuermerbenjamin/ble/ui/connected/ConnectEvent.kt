package de.stuermerbenjamin.ble.ui.connected

sealed interface ConnectEvent {
    object CreateAccessPointEvent : ConnectEvent
    object ConnectToWifiEvent : ConnectEvent
    object UpdateFirmwareEvent : ConnectEvent
    object ReadEvent : ConnectEvent
    object WriteEvent : ConnectEvent
}
