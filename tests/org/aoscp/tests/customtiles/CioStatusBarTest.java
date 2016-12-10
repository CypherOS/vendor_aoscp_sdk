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

package org.aoscp.tests.customtiles;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;

import aoscp.app.CustomTile;
import aoscp.app.CioStatusBarManager;

import org.aoscp.tests.R;

import org.aoscp.tests.TestActivity;

public class CioStatusBarTest extends TestActivity {

    private static final int CUSTOM_TILE_ID = 1337;
    private static final int CUSTOM_TILE_SETTINGS_ID = 1336;
    private CustomTile mCustomTile;
    private CioStatusBarManager mCioStatusBarManager;

    Handler mHandler = new Handler();

    @Override
    protected String tag() {
        return null;
    }

    @Override
    protected Test[] tests() {
        mCioStatusBarManager = CioStatusBarManager.getInstance(this);
        return mTests;
    }

    private Test[] mTests = new Test[] {
            new Test("test publish tile") {
                public void run() {
                    PendingIntent intent = PendingIntent.getActivity(CioStatusBarTest.this, 0,
                            new Intent(CioStatusBarTest.this, CioStatusBarTest.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
                    mCustomTile = new CustomTile.Builder(CioStatusBarTest.this)
                            .setLabel("Test From SDK")
                            .setIcon(R.drawable.ic_launcher)
                            .setOnClickIntent(intent)
                            .setContentDescription("Content description")
                            .build();
                    mCioStatusBarManager.publishTile(CUSTOM_TILE_ID, mCustomTile);
                }
            },

            new Test("test publish tile in 3 seconds") {
                public void run() {
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            PendingIntent intent = PendingIntent.getActivity(CioStatusBarTest.this, 0,
                                    new Intent(CioStatusBarTest.this, CioStatusBarTest.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
                            mCustomTile = new CustomTile.Builder(CioStatusBarTest.this)
                                    .setLabel("Test 3 seconds")
                                    .setIcon(R.drawable.ic_launcher)
                                    .setOnClickIntent(intent)
                                    .setContentDescription("Content description")
                                    .build();
                            mCioStatusBarManager.publishTile(CUSTOM_TILE_ID, mCustomTile);
                        }
                    }, 3000);
                }
            },

            new Test("test update tile") {
                public void run() {
                    if (mCustomTile != null) {
                        mCustomTile.label = "Update From SDK";
                        mCioStatusBarManager.publishTile(CUSTOM_TILE_ID, mCustomTile);
                    }
                }
            },

            new Test("test remove tile") {
                public void run() {
                    mCioStatusBarManager.removeTile(CUSTOM_TILE_ID);
                }
            },

            new Test("test remove tile in 3 seconds") {
                public void run() {
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            mCioStatusBarManager.removeTile(CUSTOM_TILE_ID);
                        }
                    }, 3000);
                }
            },

            new Test("test publish tile with settings") {
                public void run() {
                    CustomTile customTile = new CustomTile.Builder(CioStatusBarTest.this)
                            .setLabel("Test Settings From SDK")
                            .setIcon(R.drawable.ic_launcher)
                            .setOnSettingsClickIntent(new Intent(CioStatusBarTest.this,
                                    DummySettings.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            .setContentDescription("Content description")
                            .build();
                    CioStatusBarManager.getInstance(CioStatusBarTest.this)
                            .publishTile(CUSTOM_TILE_SETTINGS_ID, customTile);
                }
            },
    };
}