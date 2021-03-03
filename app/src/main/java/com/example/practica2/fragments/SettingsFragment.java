package com.example.practica2.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.practica2.R;
import com.example.practica2.databases.QuotationRoomDatabase;

public class SettingsFragment extends PreferenceFragmentCompat  {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.preferences_settings, rootKey);
        findPreference(getString(R.string.preference_database)).setOnPreferenceChangeListener(((preference, newValue) -> {
            System.out.println(newValue);
            if (newValue.equals(1)){
                QuotationRoomDatabase.destroyInstance();
            }
            return true;
        }));
    }
    }

