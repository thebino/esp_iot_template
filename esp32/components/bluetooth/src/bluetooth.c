#include "bluetooth.h"

uint16_t characteristic_attribute_handle_ota_control;
uint16_t characteristic_attribute_handle_ota_data;
uint16_t characteristic_attribute_handle_control_read;
uint16_t characteristic_attribute_handle_control_write;
uint16_t characteristic_attribute_handle_data_notify;
uint16_t characteristic_attribute_handle_data_write;
uint8_t gatt_svr_chr_ota_control_val;
uint8_t gatt_svr_chr_ota_data_val[512];

static const char *BLE_TAG = "Bluetooth";

// OTA
const esp_partition_t *update_partition;
esp_ota_handle_t update_handle;
bool updating = false;
uint16_t num_pkgs_received = 0;
uint16_t packet_size = 0;

static const struct ble_gatt_svc_def bluetooth_gatt_service_table[] = {
    {
        // service: OTA Service
        .type = BLE_GATT_SVC_TYPE_PRIMARY,
        .uuid = &s_gatt_service_uuid_ota.u,
        .characteristics = (struct ble_gatt_chr_def[]){
            {
                // characteristic: OTA control
                .uuid = &s_gatt_svr_chr_ota_control_uuid.u,
                .access_cb = gatt_characteristic_ota_control_callback,
                .flags = BLE_GATT_CHR_F_READ | BLE_GATT_CHR_F_WRITE | BLE_GATT_CHR_F_NOTIFY,
                .val_handle = &characteristic_attribute_handle_ota_control,
            },
            {
                // characteristic: OTA data
                .uuid = &s_gatt_svr_chr_ota_data_uuid.u,
                .access_cb = gatt_characteristic_ota_data_callback,
                .flags = BLE_GATT_CHR_F_WRITE,
                .val_handle = &characteristic_attribute_handle_ota_data,
            },
            {
                // No more characteristics in this array.
                0,
            }},
    },
    {
        // service: Control Service
        .type = BLE_GATT_SVC_TYPE_PRIMARY,
        .uuid = &s_gatt_service_uuid_control.u,
        .characteristics = (struct ble_gatt_chr_def[]){
            {
                // Characteristic: Control read
                .uuid = &s_gatt_svr_chr_control_read_uuid.u,
                .access_cb = gatt_characteristic_control_data_sent_callback,
                .flags = BLE_GATT_CHR_F_READ,
                .val_handle = &characteristic_attribute_handle_control_read,
            },
            {
                // Characteristic: Control write
                .uuid = &s_gatt_svr_chr_control_write_uuid.u,
                .access_cb = gatt_characteristic_control_data_receive_callback,
                .flags = BLE_GATT_CHR_F_WRITE_NO_RSP,
                .val_handle = &characteristic_attribute_handle_control_write,
            },
         {
             // No more characteristics in this array.
             0,
         },
     }
    },
    {
        // No more services in this array.
        0,
    },
};

static void update_ota_control(uint16_t conn_handle)
{
    struct os_mbuf *om;
    esp_err_t err;

    // check which value has been received
    switch (gatt_svr_chr_ota_control_val)
    {
    case SVR_CHR_OTA_CONTROL_REQUEST:
        // OTA request
        ESP_LOGI(BLE_TAG, "OTA has been requested via BLE.");
        // get the next free OTA partition
        update_partition = esp_ota_get_next_update_partition(NULL);
        // start the ota update
        err = esp_ota_begin(update_partition, OTA_WITH_SEQUENTIAL_WRITES,
                            &update_handle);
        if (err != ESP_OK)
        {
            ESP_LOGE(BLE_TAG, "esp_ota_begin failed (%s)",
                     esp_err_to_name(err));
            esp_ota_abort(update_handle);
            gatt_svr_chr_ota_control_val = SVR_CHR_OTA_CONTROL_REQUEST_NAK;
        }
        else
        {
            gatt_svr_chr_ota_control_val = SVR_CHR_OTA_CONTROL_REQUEST_ACK;
            updating = true;

            // retrieve the packet size from OTA data
            packet_size =
                (gatt_svr_chr_ota_data_val[1] << 8) + gatt_svr_chr_ota_data_val[0];
            ESP_LOGI(BLE_TAG, "Packet size is: %d", packet_size);

            num_pkgs_received = 0;
        }

        // notify the client via BLE that the OTA has been acknowledged (or not)
        om = ble_hs_mbuf_from_flat(&gatt_svr_chr_ota_control_val,
                                   sizeof(gatt_svr_chr_ota_control_val));
        ble_gattc_notify_custom(conn_handle, characteristic_attribute_handle_ota_control, om);
        ESP_LOGI(BLE_TAG, "OTA request acknowledgement has been sent.");

        break;

    case SVR_CHR_OTA_CONTROL_DONE:

        updating = false;

        // end the OTA and start validation
        err = esp_ota_end(update_handle);
        if (err != ESP_OK)
        {
            if (err == ESP_ERR_OTA_VALIDATE_FAILED)
            {
                ESP_LOGE(BLE_TAG,
                         "Image validation failed, image is corrupted!");
            }
            else
            {
                ESP_LOGE(BLE_TAG, "esp_ota_end failed (%s)!",
                         esp_err_to_name(err));
            }
        }
        else
        {
            // select the new partition for the next boot
            err = esp_ota_set_boot_partition(update_partition);
            if (err != ESP_OK)
            {
                ESP_LOGE(BLE_TAG, "esp_ota_set_boot_partition failed (%s)!",
                         esp_err_to_name(err));
            }
        }

        // set the control value
        if (err != ESP_OK)
        {
            gatt_svr_chr_ota_control_val = SVR_CHR_OTA_CONTROL_DONE_NAK;
        }
        else
        {
            gatt_svr_chr_ota_control_val = SVR_CHR_OTA_CONTROL_DONE_ACK;
        }

        // notify the client via BLE that DONE has been acknowledged
        om = ble_hs_mbuf_from_flat(&gatt_svr_chr_ota_control_val,
                                   sizeof(gatt_svr_chr_ota_control_val));
        ble_gattc_notify_custom(conn_handle, characteristic_attribute_handle_ota_control, om);
        ESP_LOGI(BLE_TAG, "OTA DONE acknowledgement has been sent.");

        // restart the ESP to finish the OTA
        if (err == ESP_OK)
        {
            ESP_LOGI(BLE_TAG, "Preparing to restart!");
            vTaskDelay(pdMS_TO_TICKS(REBOOT_DEEP_SLEEP_TIMEOUT));
            esp_restart();
        }

        break;

    default:
        break;
    }
}

esp_err_t gatt_characteristic_ota_control_callback(uint16_t conn_handle,
                                             uint16_t attr_handle,
                                             struct ble_gatt_access_ctxt *ctxt,
                                             void *arg)
{
    int rc;
    uint8_t length = sizeof(gatt_svr_chr_ota_control_val);

    switch (ctxt->op)
    {
    case BLE_GATT_ACCESS_OP_READ_CHR:
        // a client is reading the current value of ota control
        rc = os_mbuf_append(ctxt->om, &gatt_svr_chr_ota_control_val, length);
        return rc == 0 ? 0 : BLE_ATT_ERR_INSUFFICIENT_RES;
        break;

    case BLE_GATT_ACCESS_OP_WRITE_CHR:
        // a client is writing a value to ota control
        rc = gatt_svr_chr_write(ctxt->om, 1, length,
                                &gatt_svr_chr_ota_control_val, NULL);
        // update the OTA state with the new value
        update_ota_control(conn_handle);
        return rc;
        break;

    default:
        break;
    }

    // this shouldn't happen
    assert(0);
    return BLE_ATT_ERR_UNLIKELY;
}

esp_err_t gatt_characteristic_ota_data_callback(uint16_t conn_handle,
                                          uint16_t attr_handle,
                                          struct ble_gatt_access_ctxt *ctxt,
                                          void *arg)
{
    int rc;
    esp_err_t err;

    // store the received data into gatt_svr_chr_ota_data_val
    rc = gatt_svr_chr_write(ctxt->om, 1, sizeof(gatt_svr_chr_ota_data_val),
                            gatt_svr_chr_ota_data_val, NULL);

    // write the received packet to the partition
    if (updating)
    {
        err = esp_ota_write(update_handle, (const void *)gatt_svr_chr_ota_data_val,
                            packet_size);
        if (err != ESP_OK)
        {
            ESP_LOGE(BLE_TAG, "esp_ota_write failed (%s)!",
                     esp_err_to_name(err));
        }

        num_pkgs_received++;
        ESP_LOGI(BLE_TAG, "Received packet %d", num_pkgs_received);
    }

    return rc;
}

esp_err_t gatt_characteristic_control_data_receive_callback(uint16_t conn_handle,
                                                    uint16_t attr_handle,
                                                    struct ble_gatt_access_ctxt *ctxt,
                                                    void *arg)
{
    uint16_t len = ctxt->om->om_len;
    ESP_LOGW(BLE_TAG, "%s Recveived %d bytes from control data from client.", __func__, len);

    return ESP_OK;
};

esp_err_t gatt_characteristic_control_data_sent_callback(uint16_t conn_handle,
                                                         uint16_t attr_handle,
                                                         struct ble_gatt_access_ctxt *ctxt,
                                                         void *arg)
{
    uint16_t len = ctxt->om->om_len;

    ESP_LOGW(BLE_TAG, "%s Sent %d bytes to control data to client.", __func__, len);
    
    uint8_t gatt_svr_chr_ota_control_val = 0x1337;
    uint8_t length = sizeof(gatt_svr_chr_ota_control_val);
    os_mbuf_append(ctxt->om, &gatt_svr_chr_ota_control_val, length);
    
    return ESP_OK;
};





int gatt_svr_chr_write(struct os_mbuf *om, uint16_t min_len,
                       uint16_t max_len, void *dst, uint16_t *len)
{
    uint16_t om_len;
    int rc;

    om_len = OS_MBUF_PKTLEN(om);
    if (om_len < min_len || om_len > max_len)
    {
        return BLE_ATT_ERR_INVALID_ATTR_VALUE_LEN;
    }

    rc = ble_hs_mbuf_to_flat(om, dst, max_len, len);
    if (rc != 0)
    {
        return BLE_ATT_ERR_UNLIKELY;
    }

    return 0;
}

int gatt_characteristic_control_read_callback(uint16_t conn_handle,
                                          uint16_t attr_handle,
                                          struct ble_gatt_access_ctxt *ctxt,
                                          void *arg)
{
    ESP_LOGW(BLE_TAG, "characteristic read callback not implemented!");
    
    return ESP_OK;
}


/**
 * @brief   NimBLE host task to run
 */
void ble_host_task(void *param)
{
    // This function will return only when nimble_port_stop() is executed.
    nimble_port_run();
    nimble_port_freertos_deinit();
}

void bluetooth_setup(bluetooth_connection_callbacks connection_callbacks)
{
    esp_err_t ret;

    // register callbacks for GAP events
    bluetooth_register_gap_callbacks(connection_callbacks);

    // if (client_connected_callbacks == NULL)
    // {
        ESP_LOGI(BLE_TAG, "%s Set connected callback", __func__);
        client_connected_callbacks = connection_callbacks;
    // }

    ESP_LOGI(BLE_TAG, "%s Initialize the bluetooth controller", __func__);
    ret = esp_nimble_hci_and_controller_init();
    if (ret != ESP_OK)
    {
        ESP_LOGE(BLE_TAG, "Initialize the bluetooth controller failed with error: %d", ret);
        return;
    }

    ESP_LOGI(BLE_TAG, "%s Initialize NimBLE bluetooth stack.", __func__);
    nimble_port_init();

    ESP_LOGI(BLE_TAG, "%s Register sync and reset callbacks.", __func__);
    // following callbacks are placed in 'bluetooth_gap.c'
    ble_hs_cfg.sync_cb = bluetooth_host_sync_callback;
    ble_hs_cfg.reset_cb = bluetooth_host_reset_callback;

    ESP_LOGI(BLE_TAG, "%s Initialize service table.", __func__);
    ble_svc_gap_init();
    ble_svc_gatt_init();

    ESP_LOGI(BLE_TAG, "%s Register service table with NimBLE stack.", __func__);
    ble_gatts_count_cfg(bluetooth_gatt_service_table);
    ble_gatts_add_svcs(bluetooth_gatt_service_table);

    ESP_LOGI(BLE_TAG, "%s Set device name to '%s'.", __func__, DEVICE_NAME);
    ble_svc_gap_device_name_set(DEVICE_NAME);

    ESP_LOGI(BLE_TAG, "%s Start NimBLE Host in a separate task.", __func__);
    nimble_port_freertos_init(ble_host_task);
}

void bluetooth_disconnect(void)
{
    ESP_LOGE(BLE_TAG, "%s Disconnect bluetooth connections.\n", __func__);
    // TODO:
}
