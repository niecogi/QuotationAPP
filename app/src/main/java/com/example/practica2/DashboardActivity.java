package com.example.practica2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }
    public void launchExplicitIntent(View view){
        switch (view.getId()) {
            case R.id.b_about:
            Intent intent_a = new Intent(this, AboutActivity.class);
            startActivity(intent_a);
            break;

            case R.id.b_settings:
                Intent intent_s = new Intent(this, SettingsActivity.class);
                startActivity(intent_s);
                break;
            case R.id.b_fav_quotes:
                //Intent intent_f = new Intent(this, SettingsActivity.class);
                // startActivity(intent_f);
                break;
            case R.id.b_get_quotes:
               // Intent intent_g = new Intent(this, Quotes.class);
                // startActivity(intent_g);
                break;
        }
    }

}