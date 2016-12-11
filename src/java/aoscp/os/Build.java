/*
 * Copyright (C) 2016 CypherOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package aoscp.os;

import android.os.SystemProperties;

import android.util.SparseArray;

/**
 * Information about the current CypherOS build, extracted from system properties.
 */
public class Build {
    /** Value used for when a build property is unknown. */
    public static final String UNKNOWN = "unknown";

    private static final SparseArray<String> sdkMap;
    static
    {
        sdkMap = new SparseArray<String>();
        sdkMap.put(AOSCP_VERSION_API.CHEESECAKE, "Cheesecake");
    }

    /** Various version strings. */
    public static class AOSCP_VERSION {
        /**
         * The user-visible SDK version of the framework; its possible
         * values are defined in {@link Build.AOSCP_VERSION_API}.
         */
        public static final int SDK_INT = SystemProperties.getInt(
                "ro.aoscp.build.api", 0);
    }

    /**
     * Enumeration of the currently known SDK version codes.  These are the
     * values that can be found in {@link AOSCP_VERSION#SDK_INT}.  Version numbers
     * increment monotonically with each official platform release.
     */
    public static class AOSCP_VERSION_API {
        /**
         * October 2016: The first version of CypherOS Nougat
         */
        public static final int CHEESECAKE = 6;
    }

    /**
     * Retrieve the name for the SDK int
     * @param sdkInt
     * @return name of the SDK int
     */
    public static String getNameForSDKInt(int sdkInt) {
        return sdkMap.get(sdkInt);
    }
}