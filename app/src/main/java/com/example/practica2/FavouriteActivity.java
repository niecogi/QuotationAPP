package com.example.practica2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
    }
    public void launchImplicitIntent(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String authorName = "Albert Einstein";
        intent.setData(Uri.parse("https://en.wikipedia.org/wiki/Special:Search?search=" + authorName));
        startActivity(intent);
    }
}