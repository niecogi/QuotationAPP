package com.example.practica2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practica2.databases.QuotationContract;
import com.example.practica2.databases.QuotationRoomDatabase;
import com.example.practica2.databases.QuotationSQLiteHelper;
import com.example.practica2.quotation.Quotation;

public class QuotationActivity extends AppCompatActivity {
    private int numQuotes = 0;
    private boolean isVisibleFavourites=true;
    private Quotation currentQuotation;
    private MenuItem  menuFav ;
    private MenuItem menuRefresh;
    private TextView textViewAuthor;
    private static QuotationContract.Database prefDB;
    private TextView textViewText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        prefDB = QuotationContract.getPreferenceDatabase(this);

        if (savedInstanceState == null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String username = pref.getString("username", getString(R.string.nameless));
            TextView textView_quote = (TextView) findViewById(R.id.textViewText);
            textView_quote.setText(getString(R.string.t_hello).replace("%1s", username));

        }else{

            savedInstanceState.getBoolean("add_visible",isVisibleFavourites);
            textViewAuthor = findViewById(R.id.textViewAutor);
            textViewText =  findViewById(R.id.textViewText);
            textViewAuthor.setText(savedInstanceState.getString("t_sample_author",getString(R.string.t_sample_autor)).replace("%1$d", String.valueOf(numQuotes)));
            textViewText.setText(savedInstanceState.getString("t_sample_quotation",getString(R.string.t_sample_quotation)).replace("%1$d", String.valueOf(numQuotes)));
            numQuotes++;
            currentQuotation = new Quotation(textViewText.toString(),textViewAuthor.toString());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.quotes_menu, menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.quotes_menu, menu);
        menu.findItem(R.id.addFavsQuote).setVisible(isVisibleFavourites);
        return super.onCreateOptionsMenu(menu);

        /*
        menuFav = menu.getItem(0);
        menuRefresh = menu.getItem(1);
          return true;
         */


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("t_sample_author",getString(R.string.t_sample_autor));
        outState.putString("t_sample_quotation",getString(R.string.t_sample_quotation));
        outState.putInt("num_quotes",numQuotes);
        outState.putBoolean("add_visible",isVisibleFavourites);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Quotation quotation = new Quotation(getString(R.string.t_sample_quotation).replace("%1$d", String.valueOf(numQuotes)),getString(R.string.t_sample_autor).replace("%1$d", String.valueOf(numQuotes)));
        switch (item.getItemId()) {
            case R.id.addFavsQuote:
                addQuotationsInDatabase(quotation);
                seeTheAddButton(quotation);
                //isVisibleFavourites = false;
                supportInvalidateOptionsMenu();
                return true;
            case R.id.getNewQuote:
                seeTheAddButton(quotation);
                textViewAuthor = findViewById(R.id.textViewAutor);
                textViewText = findViewById(R.id.textViewText);

                textViewAuthor.setText(getString(R.string.t_sample_autor).replace("%1$d", String.valueOf(numQuotes)));
                textViewText.setText(getString(R.string.t_sample_quotation).replace("%1$d", String.valueOf(numQuotes)));
                numQuotes++;
                currentQuotation = new Quotation(textViewText.toString(),textViewAuthor.toString());

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void seeTheAddButton (final Quotation quotation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (prefDB) {
                    case Room:
                        boolean isInROOMDatabase = QuotationRoomDatabase.getInstance(QuotationActivity.this).getQuotationDAO().getQuotationByText((quotation.getQuoteText())) != null;
                        isVisibleFavourites= !isInROOMDatabase;

                        break;
                    case SQLite:
                        boolean isInSQLDatabase = QuotationSQLiteHelper.getInstance(QuotationActivity.this).isInTheFavDatabase(quotation);
                        isVisibleFavourites= !isInSQLDatabase;

                        break;

                    default:
                        isVisibleFavourites =true;
                }
            }
        }).start();

    }


        private void addQuotationsInDatabase (final Quotation quotation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (prefDB) {
                    case Room:
                        QuotationRoomDatabase.getInstance(QuotationActivity.this).getQuotationDAO().addQuotation(quotation);
                        break;
                    case SQLite:
                        QuotationSQLiteHelper.getInstance(QuotationActivity.this).addQuotationInDatabase(quotation);
                        break;
                }
            }
        }).start();
        isVisibleFavourites = true;
        }

    }




