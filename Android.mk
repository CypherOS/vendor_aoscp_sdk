# Copyright (C) 2016 CypherOS
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
LOCAL_PATH := $(call my-dir)

# We have a special case here where we build the library's resources
# independently from its code, so we need to find where the resource
# class source got placed in the course of building the resources.
# Thus, the magic here.
# Also, this module cannot depend directly on the R.java file; if it
# did, the PRIVATE_* vars for R.java wouldn't be guaranteed to be correct.
# Instead, it depends on the R.stamp file, which lists the corresponding
# R.java file as a prerequisite.
aoscp_software_res := APPS/org.aoscp.software-res_intermediates/src

# The CypherOS Software Framework Library
# ============================================================
include $(CLEAR_VARS)

aoscp_src := src/java/aoscp
aoscp_internal_src := src/java/org/aoscp/interno
library_src := aoscp/lib/main/java

LOCAL_MODULE := org.aoscp.software
LOCAL_MODULE_TAGS := optional

LOCAL_JAVA_LIBRARIES := \
    services

LOCAL_SRC_FILES := \
    $(call all-java-files-under, $(aoscp_src)) \
    $(call all-java-files-under, $(aoscp_interno_src)) \
    $(call all-java-files-under, $(library_src))

## READ ME: ########################################################
##
## When updating this list of aidl files, consider if that aidl is
## part of the SDK API.  If it is, also add it to the list below that
## is preprocessed and distributed with the SDK.  This list should
## not contain any aidl files for parcelables, but the one below should
## if you intend for 3rd parties to be able to send those objects
## across process boundaries.
##
## READ ME: ########################################################
LOCAL_SRC_FILES += \
    $(call all-Iaidl-files-under, $(aoscp_src)) \
    $(call all-Iaidl-files-under, $(aoscp_interno_src))

cosplat_LOCAL_INTERMEDIATE_SOURCES := \
    $(aoscp_software_res)/aoscp/software/R.java \
    $(aoscp_software_res)/aoscp/software/Manifest.java \
    $(aoscp_software_res)/org/aoscp/software/interno/R.java
	
LOCAL_INTERMEDIATE_SOURCES := \
    $(cosplat_LOCAL_INTERMEDIATE_SOURCES)

# Include aidl files from aoscp.app namespace as well as internal src aidl files
LOCAL_AIDL_INCLUDES := $(LOCAL_PATH)/src/java

include $(BUILD_JAVA_LIBRARY)
aoscp_framework_module := $(LOCAL_INSTALLED_MODULE)

# Make sure that R.java and Manifest.java are built before we build
# the source for this library.
aoscp_framework_res_R_stamp := \
    $(call intermediates-dir-for,APPS,org.aoscp.software-res,,COMMON)/src/R.stamp
$(full_classes_compiled_jar): $(aoscp_framework_res_R_stamp)

$(aoscp_framework_module): | $(dir $(aoscp_framework_module))org.aoscp.software-res.apk

aoscp_framework_built := $(call java-lib-deps, org.aoscp.software)

# ====  org.aoscp.software.xml lib def  ========================
include $(CLEAR_VARS)

LOCAL_MODULE := org.aoscp.software.xml
LOCAL_MODULE_TAGS := optional

LOCAL_MODULE_CLASS := ETC

# This will install the file in /system/etc/permissions
LOCAL_MODULE_PATH := $(TARGET_OUT_ETC)/permissions

LOCAL_SRC_FILES := $(LOCAL_MODULE)

include $(BUILD_PREBUILT)

# the sdk
# ============================================================
include $(CLEAR_VARS)

LOCAL_MODULE:= org.aoscp.software.sdk
LOCAL_MODULE_TAGS := optional
LOCAL_REQUIRED_MODULES := services

LOCAL_SRC_FILES := \
    $(call all-java-files-under, $(aoscp_src)) \
    $(call all-Iaidl-files-under, $(aoscp_src)) \
    $(call all-Iaidl-files-under, $(aoscp_interno_src))

# Included aidl files from aoscp.app namespace
LOCAL_AIDL_INCLUDES := $(LOCAL_PATH)/src/java

$(full_target): $(aoscp_framework_built) $(gen)
include $(BUILD_STATIC_JAVA_LIBRARY)

# ===========================================================
# Common Droiddoc vars
cosplat_docs_src_files := \
    $(call all-java-files-under, $(aoscp_src)) \
    $(call all-html-files-under, $(aoscp_src))

cosplat_docs_java_libraries := \
    org.aoscp.software.sdk
	
# SDK version as defined
cosplat_docs_SDK_VERSION := 3.5

# Release version
cosplat_docs_SDK_REL_ID := 0

cosplat_docs_LOCAL_MODULE_CLASS := JAVA_LIBRARIES

cosplat_docs_LOCAL_DROIDDOC_SOURCE_PATH := \
    $(cosplat_docs_src_files)

intermediates.COMMON := $(call intermediates-dir-for,$(LOCAL_MODULE_CLASS), org.aoscp.software.sdk,,COMMON)

# ====  The API stubs and current.xml ===========================
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= \
    $(cosplat_docs_src_files) \
    $(call all-java-files-under, $(library_src))
LOCAL_INTERMEDIATE_SOURCES:= $(cosplat_LOCAL_INTERMEDIATE_SOURCES)
LOCAL_JAVA_LIBRARIES:= $(cosplat_docs_java_libraries)
LOCAL_MODULE_CLASS:= $(cosplat_docs_LOCAL_MODULE_CLASS)
LOCAL_DROIDDOC_SOURCE_PATH:= $(cosplat_docs_LOCAL_DROIDDOC_SOURCE_PATH)
LOCAL_ADDITIONAL_JAVA_DIR:= $(intermediates.COMMON)/src
LOCAL_ADDITIONAL_DEPENDENCIES:= $(cosplat_docs_LOCAL_ADDITIONAL_DEPENDENCIES)

LOCAL_MODULE := aoscp-api-stubs

LOCAL_DROIDDOC_CUSTOM_TEMPLATE_DIR:= build/tools/droiddoc/templates-sdk

LOCAL_DROIDDOC_OPTIONS:= \
        -stubs $(TARGET_OUT_COMMON_INTERMEDIATES)/JAVA_LIBRARIES/aoscpsdk_stubs_current_intermediates/src \
		-stubpackages aoscp.app:aoscp.os:aoscp.software:org.aoscp.software \
        -api $(INTERNAL_AOSCP_PLATFORM_API_FILE) \
        -removedApi $(INTERNAL_AOSCP_PLATFORM_REMOVED_API_FILE) \
        -nodocs \
        -verbose

LOCAL_UNINSTALLABLE_MODULE := true

include $(BUILD_DROIDDOC)

$(full_target): $(aoscp_framework_built) $(gen)
$(INTERNAL_AOSCP_PLATFORM_API_FILE): $(full_target)

# ====  The system API stubs ===================================
include $(CLEAR_VARS)

LOCAL_SRC_FILES:= \
    $(cosplat_docs_src_files) \
    $(call all-java-files-under, $(library_src))
LOCAL_INTERMEDIATE_SOURCES:= $(cosplat_LOCAL_INTERMEDIATE_SOURCES)
LOCAL_JAVA_LIBRARIES:= $(cosplat_docs_java_libraries)
LOCAL_MODULE_CLASS:= $(cosplat_docs_LOCAL_MODULE_CLASS)
LOCAL_DROIDDOC_SOURCE_PATH:= $(cosplat_docs_LOCAL_DROIDDOC_SOURCE_PATH)
LOCAL_ADDITIONAL_JAVA_DIR:= $(intermediates.COMMON)/src

LOCAL_MODULE := aoscp-system-api-stubs

LOCAL_DROIDDOC_OPTIONS:=\
        -stubs $(TARGET_OUT_COMMON_INTERMEDIATES)/JAVA_LIBRARIES/aoscpsdk_system_stubs_current_intermediates/src \
		-stubpackages aoscp.app:aoscp.os:aoscp.software:org.aoscp.software \
        -showAnnotation android.annotation.SystemApi \
        -api $(INTERNAL_AOSCP_PLATFORM_SYSTEM_API_FILE) \
        -removedApi $(INTERNAL_AOSCP_PLATFORM_SYSTEM_REMOVED_API_FILE) \
        -nodocs \
        -verbose

LOCAL_DROIDDOC_CUSTOM_TEMPLATE_DIR:= build/tools/droiddoc/templates-sdk

LOCAL_UNINSTALLABLE_MODULE := true

include $(BUILD_DROIDDOC)

$(full_target): $(aoscp_framework_built) $(gen)
$(INTERNAL_AOSCP_PLATFORM_API_FILE): $(full_target)

# Documentation
# ===========================================================
include $(CLEAR_VARS)

LOCAL_MODULE := org.aoscp.software.sdk
LOCAL_INTERMEDIATE_SOURCES:= $(cosplat_LOCAL_INTERMEDIATE_SOURCES)
LOCAL_MODULE_CLASS := JAVA_LIBRARIES
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(cosplat_docs_src_files)
LOCAL_ADDITONAL_JAVA_DIR := $(intermediates.COMMON)/src

LOCAL_IS_HOST_MODULE := false
LOCAL_DROIDDOC_CUSTOM_TEMPLATE_DIR := vendor/aoscp/build/tools/droiddoc/templates-aoscpsdk
LOCAL_ADDITIONAL_DEPENDENCIES := \
    services

LOCAL_JAVA_LIBRARIES := $(cosplat_docs_java_libraries)

LOCAL_DROIDDOC_OPTIONS := \
    -offlinemode \
    -hidePackage org.aoscp.software.interno \
    -hdf android.whichdoc offline \
    -hdf sdk.version $(cosplat_docs_docs_SDK_VERSION) \
    -hdf sdk.rel.id $(cosplat_docs_docs_SDK_REL_ID) \
    -hdf sdk.preview 0 \
	-since $(AOSCP_SRC_API_DIR)/1.txt 1

$(full_target): $(aoscp_framework_built) $(gen)
include $(BUILD_DROIDDOC)

include $(call first-makefiles-under,$(LOCAL_PATH))

# Cleanup temp vars
# ===========================================================
cosplat.docs.src_files :=
cosplat.docs.java_libraries :=
intermediates.COMMON :=