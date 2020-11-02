/*
 * Copyright (C) 2018 Google Inc.
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

package com.si_ware.neospectra.Activities;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.si_ware.neospectra.Activities.MainFragments.SettingsFragment;
import com.si_ware.neospectra.R;


/**
 * This Activity is based on the Empty Activity template.
 * It displays the fragment for the setting.
 */
public class ConfigureActivity extends AppCompatActivity  {
    public static final String
            KEY_PREF_EXAMPLE_SWITCH = "example_switch";

    /**
     * Replaces the content with the Fragment to display it.
     *
     * @param savedInstanceState Instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        // Sets default values only once, first time this is called.
        // The third argument is a boolean that indicates whether
        // the default values should be set more than once. When false,
        // the system sets the default values only if this method has never
        // been called in the past.
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                        new SettingsFragment()).commit();

    }

}
