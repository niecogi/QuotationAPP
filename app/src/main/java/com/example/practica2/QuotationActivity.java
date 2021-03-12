package com.example.practica2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.practica2.databases.QuotationContract;
import com.example.practica2.databases.QuotationRoomDatabase;
import com.example.practica2.databases.QuotationSQLiteHelper;
import com.example.practica2.quotation.Quotation;
import com.example.practica2.threads.CallQuotationThread;

import java.lang.ref.WeakReference;

public class QuotationActivity extends AppCompatActivity {
    private int numQuotes = 0;
    private boolean isVisibleFavourites = false;
    private Quotation currentQuotation;
    private CallQuotationThread callQuotationThread;
    private WeakReference<QuotationActivity> weakReferenceQuotation;
    private QuotationActivity quotationActivity;
    private MenuItem menuAddFav;
    private MenuItem menuRefresh;
    private ProgressBar progressBar;
    private TextView textViewAuthor;
    private TextView textViewText;
    private Quotation quotation;
    private static QuotationContract.Database prefDB;
    private QuotationActivity qActivity;

    @Override
    protected void onResume() {
        super.onResume();
        prefDB = QuotationContract.getPreferenceDatabase(QuotationActivity.this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        weakReferenceQuotation = new WeakReference<>(quotationActivity);
        prefDB = QuotationContract.getPreferenceDatabase(QuotationActivity.this);
        System.out.print( prefDB);

        progressBar = findViewById(R.id.progressBar);
        textViewText = findViewById(R.id.textViewText);
        textViewAuthor = findViewById(R.id.textViewAutor);

        if (savedInstanceState == null) {
            supportInvalidateOptionsMenu();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String username = pref.getString("username", getString(R.string.nameless));
            TextView textView_quote = (TextView) findViewById(R.id.textViewText);
            textView_quote.setText(getString(R.string.t_hello).replace("%1s", username));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.quotes_menu, menu);
        menuAddFav = menu.findItem(R.id.addFavsQuote);
        menuAddFav.setVisible(isVisibleFavourites);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean isInternetAvailable (){
        Boolean isAvailable;
        ConnectivityManager  connectivityManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isAvailable = networkInfo.isConnected() && networkInfo !=null;
        return  isAvailable;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("t_sample_author",getString(R.string.t_sample_autor));
        outState.putString("t_sample_quotation",getString(R.string.t_sample_quotation));
        outState.putInt("num_quotes",numQuotes);
        //outState.putBoolean("add_visible",isVisibleFavourites);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFavsQuote:
                addQuotationsInDatabase(quotation);
                return true;
            case R.id.getNewQuote:
                seeTheAddButton(quotation);
               callQuotationThread = new CallQuotationThread(this);
               if (isInternetAvailable()){
                   callQuotationThread.start();
               }
            default:
                return super.onOptionsItemSelected(item);
        }
    }




        public void setVisibleProgressBar(boolean loading){
        textViewText.setVisibility(loading ? View.GONE : View.VISIBLE);
        textViewAuthor.setVisibility(loading ? View.GONE : View.VISIBLE);
        menuAddFav.setVisible(!loading);
        menuRefresh.setVisible(!loading);
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }


    private void seeTheAddButton (final Quotation quotation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Context context = weakReferenceQuotation.get();

                if(context != null) {
                    switch (prefDB) {
                        case Room:
                            boolean isInROOMDatabase = QuotationRoomDatabase.getInstance(QuotationActivity.this).getQuotationDAO().getQuotationByText((quotation.getQuoteText())) != null;
                            weakReferenceQuotation.get().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isVisibleFavourites = !isInROOMDatabase;
                                    supportInvalidateOptionsMenu();
                                }
                            });
                        case SQLite:
                            boolean isInSQLDatabase = QuotationSQLiteHelper.getInstance(QuotationActivity.this).isInTheFavDatabase(quotation);
                            weakReferenceQuotation.get().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isVisibleFavourites = !isInSQLDatabase;
                                    supportInvalidateOptionsMenu();
                                    //supportInvalidateOptionsMenu();
                                }
                            });
                    }
                }
            }
        }).start();

    }
        public void upgradeLabels(Quotation quote){
        quotation = quote;
        existsQuote(quotation,menuAddFav);
        textViewAuthor.setText(quote.getQuoteAuthor());
        textViewText.setText(quote.getQuoteText());
        progressBar.setVisibility(View.GONE);
        seeTheAddButton(quote);
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
        isVisibleFavourites = false;
        supportInvalidateOptionsMenu();
        }

    private void existsQuote(final Quotation quotation, final MenuItem addFavouritesItem)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                switch (prefDB)
                {
                    case SQLite:
                        final boolean existsSql = QuotationSQLiteHelper.getInstance(QuotationActivity.this).isInTheFavDatabase(quotation);
                        QuotationActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addFavouritesItem.setVisible(!existsSql);
                            }
                        });
                        break;

                    case Room:
                        final boolean existsRoom = QuotationRoomDatabase.getInstance(QuotationActivity.this).getQuotationDAO().getQuotationByText(quotation.getQuoteText()) != null;
                        QuotationActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addFavouritesItem.setVisible(!existsRoom);
                            }
                        });
                        break;

                    default: addFavouritesItem.setVisible(true);
                }
            }
        }).start();
    }

    }




