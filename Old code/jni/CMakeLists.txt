cmake_minimum_required(VERSION 3.4.1)

# Set the project name and version
project(media_jni)

# Add the library and specify that it is a shared library
add_library(
    media_jni
    SHARED
    media_jni.cpp
    MediaCodecJava.cpp
    ijksdl_mutex.c
)

# Find the log library from the NDK
find_library(
    log-lib
    log
)

# Specify additional libraries to link with your library
target_link_libraries(
    media_jni
    ${log-lib}
)