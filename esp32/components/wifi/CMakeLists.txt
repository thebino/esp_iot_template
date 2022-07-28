set(srcs "src/wifi.c")

set(include_dirs "include")

idf_component_register(SRCS "${srcs}"
                       INCLUDE_DIRS "${include_dirs}")
