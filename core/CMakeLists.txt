cmake_minimum_required(VERSION 3.4.1)

add_library(secret_lib SHARED src/main/cpp/secret_lib.cpp)

target_link_libraries(secret_lib
                      android
                      log)