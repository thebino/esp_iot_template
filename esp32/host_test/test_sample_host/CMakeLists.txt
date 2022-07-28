cmake_minimum_required(VERSION 3.16)

include($ENV{IDF_PATH}/tools/cmake/project.cmake)

set(COMPONENTS main)

list(APPEND EXTRA_COMPONENT_DIRS "$ENV{IDF_PATH}/tools/mocks/driver/")
list(APPEND EXTRA_COMPONENT_DIRS "$ENV{IDF_PATH}/tools/mocks/freertos/")
list(APPEND EXTRA_COMPONENT_DIRS "$ENV{IDF_PATH}/tools/mocks/spi_flash/")

project(test_sample_host)
