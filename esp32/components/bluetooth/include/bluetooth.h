/**
 * File:     bluetooth.h
 *
 * Author:   Stürmer Benjamin (webmaster@stuermer-benjamin.de)
 * Date:     Spring 2022
 *
 * Summary of File:
 *
 *   This file contains code to setup and control all bluetooth
 *   communication, except for GATT and GAP.
 */
#ifndef BLUETOOTH_H
#define BLUETOOTH_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "bluetooth_gatt.h"
#include "bluetooth_gap.h"

// OTA
#include "esp_ota_ops.h"
#define REBOOT_DEEP_SLEEP_TIMEOUT 500

// BLE
#include "esp_check.h"
#include "esp_bt.h"
#include "host/ble_hs.h"
#include "nimble/nimble_port.h"
#include "nimble/nimble_port_freertos.h"
#include "esp_nimble_hci.h"
#include "services/gap/ble_svc_gap.h"
#include "services/gatt/ble_svc_gatt.h"

typedef enum 
{
  SVR_CHR_OTA_CONTROL_NOP,
  SVR_CHR_OTA_CONTROL_REQUEST,
  SVR_CHR_OTA_CONTROL_REQUEST_ACK,
  SVR_CHR_OTA_CONTROL_REQUEST_NAK,
  SVR_CHR_OTA_CONTROL_DONE,
  SVR_CHR_OTA_CONTROL_DONE_ACK,
  SVR_CHR_OTA_CONTROL_DONE_NAK,
} svr_chr_ota_control_val_t;

bluetooth_connection_callbacks client_connected_callbacks;

/**
 * @brief   Initialize all bluetooth system components to start communication
 */
void bluetooth_setup(bluetooth_connection_callbacks connection_callbacks);

/**
 * @brief   handle over-the-air update control data
 */
esp_err_t gatt_characteristic_ota_control_callback(uint16_t conn_handle,
                                                   uint16_t attr_handle,
                                                   struct ble_gatt_access_ctxt *ctxt,
                                                   void *arg);

/**
 * @brief   handle over-the-air update binary data
 */
esp_err_t gatt_characteristic_ota_data_callback(uint16_t conn_handle,
                                                uint16_t attr_handle,
                                                struct ble_gatt_access_ctxt *ctxt,
                                                void *arg);


/**
 * @brief   read data received from client (Client > Server)
 *
 * @param   conn_handle
 * @param   attr_handle
 * @param   ctxt
 * @param   arg
 */
esp_err_t gatt_characteristic_control_data_receive_callback(uint16_t conn_handle,
                                                    uint16_t attr_handle,
                                                    struct ble_gatt_access_ctxt *ctxt,
                                                    void *arg);


esp_err_t gatt_characteristic_control_data_sent_callback(uint16_t conn_handle,
                                                uint16_t attr_handle,
                                                struct ble_gatt_access_ctxt *ctxt,
                                                void *arg);


int gatt_svr_chr_write(struct os_mbuf *om, uint16_t min_len,
                       uint16_t max_len, void *dst, uint16_t *len);

/**
 * @brief   close current connection
 */
void bluetooth_disconnect(void);

#endif /* BLUETOOTH_H */
