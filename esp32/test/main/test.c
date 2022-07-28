#include "stdio.h"
#include <unistd.h>
#include "unity.h"

/**
 * @brief   entry-point on an esp device
 */
void app_main() {
    printf("\n#### %s #####\n\n", "Running all the registered tests");
    UNITY_BEGIN();
    unity_run_all_tests();
    UNITY_END();
}
