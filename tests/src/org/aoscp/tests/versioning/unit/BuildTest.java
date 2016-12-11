/**
 * Copyright (c) 2016 CypherOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aoscp.tests.versioning.unit;

import android.test.suitebuilder.annotation.SmallTest;
import aoscp.os.Build;

import android.test.AndroidTestCase;

/**
 * Created by adnan on 7/14/15.
 */
public class BuildTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testFetchSdkApiLevelExists() {
        assertNotNull(Build.AOSCP_VERSION.SDK_INT);
    }

    @SmallTest
    public void testSdkApiLevelCurrent() {
        assertEquals(Build.AOSCP_VERSION_API.CHEESECAKE, Build.AOSCP_VERSION.SDK_INT);
    }

    @SmallTest
    public void testSdkApiLevelCanMatch() {
        String apiName = Build.getNameForSDKInt(Build.AOSCP_VERSION.SDK_INT);
        assertNotNull(apiName);
        assertEquals(Build.getNameForSDKInt(Build.AOSCP_VERSION_API.CHEESECAKE), apiName);
    }

    @SmallTest
    public void testSdkApiLevelSkippedIfGreaterThanAllowed() {
        int i = 0;
        if (Build.AOSCP_VERSION.SDK_INT > Build.AOSCP_VERSION_API.CHEESECAKE + 1) {
            i++;
        }
        assertEquals(0, i);
    }
	
	@SmallTest
    public void testSdkLevelRetrieveNameImpossible() {
        String name = Build.getNameForSDKInt(Integer.MAX_VALUE);
        assertNotNull(name);
        assertEquals(Build.UNKNOWN, name);
    }
}