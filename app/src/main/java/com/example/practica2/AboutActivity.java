package com.example.practica2;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImageView iv1 = findViewById(R.id.imageView);
        ImageView iv2 = findViewById(R.id.imageView2);

        iv1.setImageResource(R.drawable.person1);
        iv2.setImageResource(R.drawable.person2);
    }
}