/**
 * File:     wifi.h
 *
 * Author:   Stürmer Benjamin (webmaster@stuermer-benjamin.de)
 * Date:     Spring 2022
 *
 * Summary of File:
 *
 *   This file contains code to setup and control all wifi communication.
 */
#ifndef WIFI_H
#define WIFI_H

#include <string.h>
#include "esp_mac.h"
#include "esp_wifi.h"
#include "esp_event.h"
#include "esp_log.h"
#include "esp_system.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/event_groups.h"
#include "lwip/err.h"
#include "lwip/sys.h"


/**
 * @brief   Start wifi soft-ap
 */
void wifi_start_access_point(void);

/**
 * @brief   Stop wifi soft-ap
 */
void wifi_stop_access_point(void);

/**
 * @brief   Connect to a nearby wifi
 * 
 * @param   wifi_config network configuration containing ssid, password and scan_mode.
 */
void wifi_connect_to_network(wifi_config_t* wifi_config);

/**
 * @brief   Disconnect from any wifi station
 */
void wifi_disconnect(void);

#endif /* WIFI_H */
