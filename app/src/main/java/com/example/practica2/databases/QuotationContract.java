package com.example.practica2.databases;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.BaseColumns;

import androidx.preference.PreferenceManager;

import com.example.practica2.R;

public class QuotationContract {

    private QuotationContract() { }

     public static class QuotationSchema implements BaseColumns {
        public static final String TABLE_NAME ="quotation_table";
        public static final String COL_AUTHOR_NAME ="author";
        public static final String COL_QUOTE_TEXT ="quote";
    }

   public enum Database { Room , SQLite }
    public static Database getPreferenceDatabase(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedDatabase = preferences.getString(
                context.getString(R.string.preference_database),
                context.getString(R.string.a2_item_SQLiteOpenHelper));

        if (selectedDatabase.equals(context.getString(R.string.a1_item_Room))) {
            return Database.Room;
        }
        return Database.SQLite;
    }

}
