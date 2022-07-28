set(srcs "src/bluetooth.c" "src/bluetooth_gap.c")

set(include_dirs "include")

idf_component_register(SRCS "${srcs}"
                       INCLUDE_DIRS "${include_dirs}"
                       REQUIRES bt app_update)
