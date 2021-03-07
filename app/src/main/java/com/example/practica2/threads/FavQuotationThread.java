package com.example.practica2.threads;

import android.content.Context;

import com.example.practica2.FavouriteActivity;
import com.example.practica2.databases.QuotationContract;
import com.example.practica2.databases.QuotationRoomDatabase;
import com.example.practica2.databases.QuotationSQLiteHelper;
import com.example.practica2.quotation.Quotation;

import java.lang.ref.WeakReference;
import java.util.List;

public class FavQuotationThread extends Thread{

    private WeakReference<FavouriteActivity> favActivity;
    private QuotationContract.Database db ;
    private List<Quotation> quotationList;

    public FavQuotationThread(FavouriteActivity favActivity, QuotationContract.Database db){
        this.favActivity = new WeakReference<>(favActivity);
        this.db = db;
    }
    @Override
    public void run() {
        Context context = this.favActivity.get();
        FavouriteActivity favouriteActivity = this.favActivity.get();
        if (context != null) {
            switch (db){
                case Room:
                    quotationList = QuotationSQLiteHelper.getInstance(context).getListAllQuotations();
                case SQLite:
                    quotationList = QuotationRoomDatabase.getInstance(context).getQuotationDAO().getAllQuotationsFromDatabase();
            }
            favActivity.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    favouriteActivity.onLoadQuotation(quotationList);
                }
            });
            //Nothing
        }
    }

}
