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
import android.net.Uri;
import android.os.Handler;

import aoscp.app.CustomTile;
import aoscp.app.CioStatusBarManager;

import org.aoscp.tests.R;

import org.aoscp.tests.TestActivity;

import java.util.ArrayList;

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
			
			new Test("test publish tile with custom uri") {
                public void run() {
                    CustomTile customTile = new CustomTile.Builder(CioStatusBarTest.this)
                            .setIcon(R.drawable.ic_launcher)
                            .setOnClickUri(Uri.parse("http://tasker.dinglisch.net"))
                            .build();
                    CioStatusBarManager.getInstance(CioStatusBarTest.this)
                            .publishTile(CUSTOM_TILE_SETTINGS_ID, customTile);
                }
            },
			
			new Test("test publish tile with expanded list") {
                public void run() {
                    PendingIntent intent = PendingIntent.getActivity(CioStatusBarTest.this, 0,
                            new Intent(CioStatusBarTest.this, CioStatusBarTest.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
                    ArrayList<CustomTile.ExpandedListItem> expandedListItems =
                            new ArrayList<CustomTile.ExpandedListItem>();
                    for (int i = 0; i < 100; i++) {
                        CustomTile.ExpandedListItem expandedListItem =
                                new CustomTile.ExpandedListItem();
                        expandedListItem.setExpandedListItemDrawable(R.drawable.ic_launcher);
                        expandedListItem.setExpandedListItemTitle("Test: " + i);
                        expandedListItem.setExpandedListItemSummary("Test item summary " + i);
                        expandedListItem.setExpandedListItemOnClickIntent(intent);
                        expandedListItems.add(expandedListItem);
                    }

                    CustomTile.ListExpandedStyle listExpandedStyle =
                            new CustomTile.ListExpandedStyle();
                    listExpandedStyle.setListItems(expandedListItems);
                    CustomTile customTile = new CustomTile.Builder(CioStatusBarTest.this)
                            .setLabel("Test Expanded List Style From SDK")
                            .setIcon(R.drawable.ic_launcher)
                            .setExpandedStyle(listExpandedStyle)
                            .setOnSettingsClickIntent(new Intent(CioStatusBarTest.this,
                                    DummySettings.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            .setContentDescription("Content description")
                            .build();
                    CioStatusBarManager.getInstance(CioStatusBarTest.this)
                            .publishTile(CUSTOM_TILE_SETTINGS_ID, customTile);
                }
            },

            new Test("test publish tile with expanded grid") {
                public void run() {
                    PendingIntent intent = PendingIntent.getActivity(CioStatusBarTest.this, 0,
                            new Intent(CioStatusBarTest.this, CioStatusBarTest.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
                    ArrayList<CustomTile.ExpandedGridItem> expandedGridItems =
                            new ArrayList<CustomTile.ExpandedGridItem>();
                    for (int i = 0; i < 8; i++) {
                        CustomTile.ExpandedGridItem expandedGridItem =
                                new CustomTile.ExpandedGridItem();
                        expandedGridItem.setExpandedGridItemDrawable(R.drawable.ic_launcher);
                        expandedGridItem.setExpandedGridItemTitle("Test: " + i);
                        expandedGridItem.setExpandedGridItemOnClickIntent(intent);
                        expandedGridItems.add(expandedGridItem);
                    }

                    CustomTile.GridExpandedStyle gridExpandedStyle =
                            new CustomTile.GridExpandedStyle();
                    gridExpandedStyle.setGridItems(expandedGridItems);
                    CustomTile customTile = new CustomTile.Builder(CioStatusBarTest.this)
                            .setLabel("Test Expanded Grid Style From SDK")
                            .setIcon(R.drawable.ic_launcher)
                            .setExpandedStyle(gridExpandedStyle)
                            .setOnSettingsClickIntent(new Intent(CioStatusBarTest.this,
                                    DummySettings.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            .setContentDescription("Content description")
                            .build();
                    CioStatusBarManager.getInstance(CioStatusBarTest.this)
                            .publishTile(CUSTOM_TILE_SETTINGS_ID, customTile);
                }
            }
    };
}