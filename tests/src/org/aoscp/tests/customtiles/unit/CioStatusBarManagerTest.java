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

package org.aoscp.tests.customtiles.unit;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import aoscp.app.CioStatusBarManager;
import aoscp.app.ICioStatusBarManager;

/**
 * Created by adnan on 7/14/15.
 */
public class CioStatusBarManagerTest extends AndroidTestCase {
    private CioStatusBarManager mCioStatusBarManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCioStatusBarManager = CioStatusBarManager.getInstance(mContext);
    }

    @SmallTest
    public void testManagerExists() {
        assertNotNull(mCioStatusBarManager);
    }

    @SmallTest
    public void testManagerServiceIsAvailable() {
        ICioStatusBarManager icioStatusBarManager = mCioStatusBarManager.getService();
        assertNotNull(icioStatusBarManager);
    }
}