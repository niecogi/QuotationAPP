package com.example.practica2.databases;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.BaseColumns;

import androidx.preference.PreferenceManager;

import com.example.practica2.R;

public class QuotationContract {

    private QuotationContract() {
    }

    static class QuotationBaseColumns implements BaseColumns {
        static final String tableName = "quotation_table",
                colName_authorName = "author",
                colName_quoteText = "quote";
    }
   public enum Database { Room , SQLite }

    public static Database getPreferenceDatabase(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    String selectedDatabase = preferences.getString(
            context.getString(R.string.a1_item_Room),
            context.getString(R.string.a2_item_SQLiteOpenHelper));

        if(selectedDatabase.equals(context.getString(R.string.a1_item_Room))) {
            return Database.Room;
        }
        else if(selectedDatabase.equals(context.getString(R.string.a2_item_SQLiteOpenHelper))) {
            return Database.SQLite;
        }
        else
                return null;
}




}
