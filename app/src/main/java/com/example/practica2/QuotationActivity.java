package com.example.practica2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class QuotationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = pref.getString("username",getString(R.string.nameless));
        TextView textView_quote = (TextView) findViewById(R.id.textViewName);
        textView_quote.setText(getString(R.string.t_hello).replace("%1s",username));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.quotes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addFavsQuote:
                return true;
            case R.id.getNewQuote:
                TextView t_autor = (TextView)findViewById(R.id.textViewAutor);
                t_autor.setText(R.string.t_sample_autor);
                TextView t_quotation = (TextView)findViewById(R.id.textViewName);
                t_quotation.setText(R.string.t_sample_quotation);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onClick(View v)
    {
        TextView t_autor = (TextView)findViewById(R.id.textViewAutor);
        t_autor.setText(R.string.t_sample_autor);
        TextView t_quotation = (TextView)findViewById(R.id.textViewName);
        t_quotation.setText(R.string.t_sample_quotation);
    }

    public void buttonClick(View v)
    {
        TextView tv = (TextView)findViewById(R.id.textViewName);
        tv.setText("Hello Nameless One! Press Refresh to get a new quotation");
    }

}