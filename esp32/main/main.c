#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/event_groups.h"
#include "freertos/semphr.h"
#include "nvs.h"
#include "nvs_flash.h"

#include "esp_system.h"
#include "esp_log.h"
#include "esp_timer.h"
#include "sdkconfig.h"

#include "bluetooth.h"
#include "wifi.h"

static const char *MAIN_TAG = "Main";

void run_post_ota_diagnosis(void);

esp_err_t verify_ota(void);

int bluetooth_command_received_callback(void);

void _bluetooth_client_connected_callback(void)
{
    ESP_LOGI(MAIN_TAG, "BLE client connected.");
};

void client_disconnected_callback(void)
{
    ESP_LOGI(MAIN_TAG, "BLE client disconnected.");
};

/**
 * @brief   entry-point on an esp device
 * 
 */
void app_main() {
    esp_err_t ret;
    const esp_app_desc_t *desc = esp_ota_get_app_description();
    ESP_LOGI(MAIN_TAG, "Starting IoT Skeleton. Version: %s", desc->version);

    run_post_ota_diagnosis();

    ESP_LOGI(MAIN_TAG, "Initialize non-volatile storage");
    ret = nvs_flash_init();
    if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND)
    {
        ESP_ERROR_CHECK(nvs_flash_erase());
        ret = nvs_flash_init();
    }
    ESP_ERROR_CHECK(ret);

    bluetooth_connection_callbacks connection_callbacks = {
        .client_connected_callback = _bluetooth_client_connected_callback,
        .client_disconnected_callback = client_disconnected_callback,
    };

    // Initialize Bluetooth
    bluetooth_setup(connection_callbacks);
};

/**
 * @brief   entry-point on linux systems for host based unit testing
 * 
 * @param   argc
 * @param   argv
 * 
 */
int main(int argc, char **argv)
{
    printf("main() entry-point from production app. Please check 'test/' directory.\n");

    return 1;
};


/**
 * @brief   handle bluetooth commands
 */
int bluetooth_command_received_callback(void)
{
    ESP_LOGI(MAIN_TAG, "Received bluetooth cmd");
    
    return 0;
};

/**
 * @brief   check if OTA has successfully been applied
 *
 * @return  ESP_OK: OTA have been appliead successfully
 *          ESP_FAIL: OTA could not been verified
 */
esp_err_t verify_ota(void)
{
    // TODO: check if all files are applied correctly
    ESP_LOGW(MAIN_TAG, "%s Verify OTA image is not implemented in this template! Please add it yourself.", __func__);

    return ESP_OK;
};

/**
 * @brief   check OTA partition state and verify image if OTA has been applied
 */
void run_post_ota_diagnosis(void)
{
    ESP_LOGI(MAIN_TAG, "Check OTA partition state and verify image if OTA has been applied...");

    // check currently running partition info
    const esp_partition_t *partition = esp_ota_get_running_partition();

    switch (partition->address)
    {
    case 0x00010000:
        ESP_LOGI(MAIN_TAG, "Running partition: factory");
        break;
    case 0x00110000:
        ESP_LOGI(MAIN_TAG, "Running partition: ota_0");
        break;
    case 0x00210000:
        ESP_LOGI(MAIN_TAG, "Running partition: ota_1");
        break;
    default:
        ESP_LOGE(MAIN_TAG, "Running partition: unknown!");
        break;
    }

    esp_err_t ret;
    esp_ota_img_states_t ota_state;

    if (esp_ota_get_state_partition(partition, &ota_state) == ESP_OK)
    {
        if (ota_state == ESP_OTA_IMG_PENDING_VERIFY)
        {
            ESP_LOGI(MAIN_TAG, "An OTA update has been detected.");

            ret = verify_ota();
            if (ret == ESP_OK)
            {
                ESP_LOGI(MAIN_TAG, "OTA check completed successfully.");
                esp_ota_mark_app_valid_cancel_rollback();
            }
            else
            {
                ESP_LOGE(MAIN_TAG, "OTA check failed! Start rollback to the previous version.");
                esp_ota_mark_app_invalid_rollback_and_reboot();
            }
        }
    }
};
