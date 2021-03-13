package com.example.practica2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practica2.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    //private EditTextPreference username;

    private String user;
    private String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fcvSettingsLayoutActivity, SettingsFragment.class, null)
                .commit();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        user = pref.getString("username","");

    }



    @Override
    protected void onPause() {
        super.onPause();
        //username = (EditText) findViewById(R.id.editTextUsername_Settings);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor  editor = preferences.edit();

        if(preferences.getString("username","")!= null){
            editor.putString("username", user);

        }else{
            editor.remove(user);
        }

    }

}