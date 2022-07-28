#ifndef LIGHTING_H
#define LIGHTING_H

/**
 * @brief   set all pixels to given color
 * 
 * @param   red
 * @param   green
 * @param   blue
 * 
 * @return  ESP_OK: success
 *          ESP_ERR_INVALID_ARG: parameter error
 * 
 */
int lighting_fill(int red, int greed, int blue);

/**
 * @brief   set pixel with given number to white
 * 
 * @return  ESP_OK: success
 *          ESP_ERR_INVALID_ARG: parameter error
 */
int lighting_turn_on(int pixel_number);

/**
 * @brief   set pixel with given number to black
 * 
 * @return  ESP_OK: success
 *          ESP_ERR_INVALID_ARG: parameter error
 */
int lighting_turn_off(int pixel_number);

#endif /* LIGHTING_H */
