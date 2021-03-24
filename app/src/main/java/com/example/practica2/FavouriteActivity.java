package com.example.practica2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica2.adapter.Adapter;
import com.example.practica2.databases.QuotationContract;
import com.example.practica2.databases.QuotationRoomDatabase;
import com.example.practica2.databases.QuotationSQLiteHelper;
import com.example.practica2.quotation.Quotation;
import com.example.practica2.threads.FavQuotationThread;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity implements Adapter.OnItemLongClickListener, Adapter.OnItemClickListener {
    private static QuotationContract.Database prefDB;
    private Adapter adapter;
    private MenuItem menuRemoveAllQuotations;
    private List<Quotation> quotationList;
    private Boolean isVisible = true;
    private String WIKI_SEARCH = "https://en.wikipedia.org/wiki/Special:Search?search=";

    @Override
    protected void onResume() {
        super.onResume();
        prefDB = QuotationContract.getPreferenceDatabase(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        quotationList = new ArrayList<Quotation>();
        adapter = new Adapter(quotationList,this,this);

        prefDB = QuotationContract.getPreferenceDatabase(this);
        FavQuotationThread favQuotationThread = new FavQuotationThread(this,prefDB);
        favQuotationThread.start();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav_menu,menu);
        menuRemoveAllQuotations = menu.findItem(R.id.removeAllQuotesMenu);
        menuRemoveAllQuotations.setVisible(isVisible);
        return super.onCreateOptionsMenu(menu);
    }

    public void onLoadQuotation (List<Quotation> quotationList){
        this.quotationList.addAll(quotationList);
        adapter.notifyDataSetChanged();
        if(this.quotationList != null && this.quotationList.size() > 0){
            isVisible = true;
        }else { isVisible = false;}
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onItemLongClickListener(Adapter adapter, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FavouriteActivity.this);
        dialogBuilder.setMessage(R.string.confirm_clear_quotation);
        dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Quotation quotationToRemove = adapter.getPositionQuotation(position);
                removeQuotation(quotationToRemove);
                adapter.removeQuotationPosition(position);
            }

        });
        dialogBuilder.setNegativeButton(R.string.no, null);
        dialogBuilder.show();
        return true;
    }

    @Override
    public void onItemClickListener(Adapter adapter, int position) {
        try {
            getAuthorInfo(adapter.getPositionQuotation(position));
        } catch (IllegalArgumentException ex) {
            Toast.makeText(FavouriteActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException ex) {
            Toast.makeText(FavouriteActivity.this, R.string.cannot_bobtained,
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
        case R.id.removeAllQuotesMenu:
             AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FavouriteActivity.this);
             dialogBuilder.setMessage(R.string.confirm_clear_all_fav);
             dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     removeAllQuotations();
                     adapter.removeAllQuotations();
                     item.setVisible(false);
                     }
             });
             dialogBuilder.setNegativeButton(R.string.no, null);
             dialogBuilder.show();
             return true;

             default:
                return super.onOptionsItemSelected(item);
            }

        }

    private void removeQuotation (final Quotation quotation){
        new Thread(new Runnable() {
            @Override
            public void run() {

                switch (prefDB){
                    case Room:
                        QuotationRoomDatabase.getInstance(FavouriteActivity.this).getQuotationDAO().removeQuotation(quotation);
                        break;
                    case SQLite:
                        QuotationSQLiteHelper.getInstance(FavouriteActivity.this).removeQuotationInDatabase(quotation);
                        break;
                }

            }
        }).start();
    }

    private void removeAllQuotations (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (prefDB){
                    case Room:
                        QuotationRoomDatabase.getInstance(FavouriteActivity.this).getQuotationDAO().deleteAllQuotes();
                        break;
                    case SQLite:
                        QuotationSQLiteHelper.getInstance(FavouriteActivity.this).removeAllQuotationsInDatabase();
                        break;
                }
            }
        }).start();
    }

    public void getAuthorInfo(Quotation q) throws UnsupportedEncodingException {
        if (q.getQuoteAuthor() == null || q.getQuoteAuthor().isEmpty()) {
            throw new IllegalArgumentException(getString(R.string.cannot_bobtained_author));
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(WIKI_SEARCH + URLEncoder.encode(q.getQuoteAuthor(), "UTF-8")));
        startActivity(intent);
    }

    }








