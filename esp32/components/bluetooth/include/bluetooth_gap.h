/**
 * File:     bluetooth_gap.h
 *
 * Author:   Stürmer Benjamin (webmaster@stuermer-benjamin.de)
 * Date:     Spring 2022
 *
 * Summary of File:
 *
 *   This file contains code to advertising a device’s services and managing connections
 *   between devices.
 */
#ifndef BLUETOOTH_GAP_H
#define BLUETOOTH_GAP_H

#include <stdio.h>
#include <stdlib.h>
#include "esp_log.h"

#include "esp_nimble_hci.h"
#include "nimble/nimble_port.h"
#include "nimble/nimble_port_freertos.h"
#include "host/ble_hs.h"
#include "host/util/util.h"
#include "console/console.h"
#include "services/gap/ble_svc_gap.h"

static const char DEVICE_NAME[] = "IoT Device";

typedef void (* bluetooth_client_connected_callback)();
typedef void (* bluetooth_client_disconnected_callback)();

typedef struct
{
  bluetooth_client_connected_callback client_connected_callback;
  bluetooth_client_disconnected_callback client_disconnected_callback;
} bluetooth_connection_callbacks;

/**
 * @brief   register callbacks
 * 
 * @param   
 */
void bluetooth_register_gap_callbacks(bluetooth_connection_callbacks bluetooth_callbacks);

/**
 * @brief   start bluetooth advertisement
 *
 * @param   advertisementData configuration for setting up the advertisement
 */
void bluetooth_start_advertisement(void);

/**
 * @brief   stop bluetooth advertisement
 */
void bluetooth_stop_advertisement(void);

/**
 * @brief   Callback when the NimBLE stack resets itself
 *
 * @param   reason indicate what happened before reset
 */
void bluetooth_host_reset_callback(int reason);

/**
 * @brief   Callback handled when the bluetooth host and the controller are synchronized
 */
void bluetooth_host_sync_callback(void);

#endif /* BLUETOOTH_GAP_H */
