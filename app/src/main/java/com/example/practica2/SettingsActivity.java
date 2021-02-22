package com.example.practica2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private EditText username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        username = (EditText) findViewById(R.id.editTextUsername_Settings);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        username.setText(pref.getString("username",""));



    }

    @Override
    protected void onPause() {
        super.onPause();
        username = (EditText) findViewById(R.id.editTextUsername_Settings);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor  editor = preferences.edit();

        if (username.getText() != null){
            editor.putString("username",username.getText().toString());

        }else {
            editor.remove(username.getText().toString());
        }
        editor.apply();

    }
}