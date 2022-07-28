#include "bluetooth_gap.h"

static const char *ADV_TAG = "Advertisement";

#define HS_NOTIFY_CHARACTERISTIC &lam_svc.attrs[1]
#define BP_NOTIFY_CHARACTERISTIC &lam_svc.attrs[6]

uint8_t addr_type;

/**
 * advertisement callback on NimBLE bluetooth stack
 * 
 * @param event advertisement events like dis/connect, mtu update
 * @param arg   event arguments like mtu size
 */
int gap_event_handler(struct ble_gap_event *event, void *arg);

void bluetooth_register_gap_callbacks(bluetooth_connection_callbacks bluetooth_callbacks)
{
    ESP_LOGI(BLE_TAG, "%s register callbacks for GAP events...", __func__);
}

void bluetooth_start_advertisement(void)
{
    ESP_LOGI(ADV_TAG, "GAP: Start bluetooth advertisement");

    struct ble_gap_adv_params adv_params;
    struct ble_hs_adv_fields fields;
    int rc;

    memset(&fields, 0, sizeof(fields));

    // flags: discoverability + BLE only
    fields.flags = BLE_HS_ADV_F_DISC_GEN | BLE_HS_ADV_F_BREDR_UNSUP;

    // include power levels
    fields.tx_pwr_lvl_is_present = 1;
    fields.tx_pwr_lvl = BLE_HS_ADV_TX_PWR_LVL_AUTO;

    // include device name
    fields.name = (uint8_t *)DEVICE_NAME;
    fields.name_len = strlen(DEVICE_NAME);
    fields.name_is_complete = 1;

    rc = ble_gap_adv_set_fields(&fields);
    if (rc != 0)
    {
        ESP_LOGE(ADV_TAG, "%s Error setting advertisement data: rc=%d", __func__, rc);
        return;
    }

    // start advertising
    memset(&adv_params, 0, sizeof(adv_params));
    adv_params.conn_mode = BLE_GAP_CONN_MODE_UND;
    adv_params.disc_mode = BLE_GAP_DISC_MODE_GEN;
    rc = ble_gap_adv_start(addr_type, NULL, BLE_HS_FOREVER, &adv_params, gap_event_handler, NULL);
    if (rc != 0)
    {
        ESP_LOGE(ADV_TAG, "%s Error enabling advertisement data: rc=%d", __func__, rc);
        return;
    }
}

void bluetooth_stop_advertisement(void)
{
    ESP_LOGD(ADV_TAG, "GAP: Stop bluetooth advertisement");
    ble_gap_adv_stop();
}

void bluetooth_host_reset_callback(int reason)
{
    if (reason == 531)
    {
        ESP_LOGI(ADV_TAG, "BLE host config reset callback: Client disconnected");
    } else {
        ESP_LOGE(ADV_TAG, "BLE host config reset callback: reason = %d", reason);
    }
}

void bluetooth_host_sync_callback(void)
{
    ESP_LOGI(ADV_TAG, "BLE host config sync callback");

    // determine best adress type
    ble_hs_id_infer_auto(0, &addr_type);

    // start avertising
    bluetooth_start_advertisement();
}

int gap_event_handler(struct ble_gap_event *event, void *arg)
{
    switch (event->type)
    {
    case BLE_GAP_EVENT_CONNECT:
        // A new connection was established or a connection attempt failed
        ESP_LOGI(ADV_TAG, "%s GAP: Connection %s: status=%d", __func__,
                 event->connect.status == 0 ? "established" : "failed",
                 event->connect.status);

        // Connection established; stop advertising
        if (event->connect.status == 0)
        {
            bluetooth_stop_advertisement();
        }
        break;

    case BLE_GAP_EVENT_DISCONNECT:
        if (event->disconnect.reason == 531)
        {
            ESP_LOGI(ADV_TAG, "%s GAP: Disconnect: Client disconnected", __func__);
        } else {
            ESP_LOGI(ADV_TAG, "%s GAP: Disconnect: reason=%d", __func__, event->disconnect.reason);
        }

        // Connection terminated; resume advertising
        bluetooth_start_advertisement();
        break;

    case BLE_GAP_EVENT_ADV_COMPLETE:
        ESP_LOGI(ADV_TAG, "GAP: adv complete");
        bluetooth_start_advertisement();
        break;

    case BLE_GAP_EVENT_SUBSCRIBE:
        ESP_LOGI(ADV_TAG, "GAP: Subscribe: conn_handle=%d", event->connect.conn_handle);
        break;

    case BLE_GAP_EVENT_MTU:
        ESP_LOGI(ADV_TAG, "GAP: MTU update: conn_handle=%d, mtu=%d", event->mtu.conn_handle, event->mtu.value);
        break;
    }

    return 0;
}
