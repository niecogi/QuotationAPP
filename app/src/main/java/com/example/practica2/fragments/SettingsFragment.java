package com.example.practica2.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.practica2.R;
import com.example.practica2.databases.QuotationRoomDatabase;
import com.example.practica2.threads.CallQuotationThread;

public class SettingsFragment extends PreferenceFragmentCompat  {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);
        findPreference("preference_database").setOnPreferenceChangeListener(((preference, newValue) -> {
            if (newValue.equals(getString(R.string.a2_item_SQLiteOpenHelper)) ){
                QuotationRoomDatabase.destroyInstance();
            }
            return true;
        }));

        findPreference("settings_languages").setOnPreferenceChangeListener(((preference, newValue) -> {
            if ( newValue.toString() == "English"){
                CallQuotationThread.setLanguage("EN");
            }
            else{
                CallQuotationThread.setLanguage("RU");
            }
            return true;
        }));

        findPreference("settings_methods").setOnPreferenceChangeListener(((preference, newValue) -> {
            if ( newValue.toString() == "GET"){
                CallQuotationThread.setMethod("GET");
            }
            else{
                CallQuotationThread.setMethod("POST");
            }
            return true;
        }));
    }
    }

