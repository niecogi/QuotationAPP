package com.example.practica2.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.practica2.databases.QuotationContract.QuotationSchema;
import com.example.practica2.quotation.Quotation;

import java.util.ArrayList;


public class QuotationSQLiteHelper extends SQLiteOpenHelper {
    private static final String databaseName= "quotation_database";
    private static QuotationSQLiteHelper instance;

    private QuotationSQLiteHelper(Context context){
        super(context,databaseName,null,1);
    }

    public boolean isInTheFavDatabase(Quotation quotation){
        boolean is_in_database;
        try( SQLiteDatabase database = getReadableDatabase()){
            Cursor cursor = database.query(QuotationSchema.TABLE_NAME, null,
                    String.format("%s=?", QuotationSchema.COL_QUOTE_TEXT), new String[]{quotation.getQuoteText()}, null,
                    null, null,null);

            if (cursor.getCount() > 0) {
                is_in_database = true;
            } else {
                is_in_database = false;
            }
            cursor.close();
        }

        return is_in_database;
    }

    public void addQuotationInDatabase(Quotation quotation){
        ContentValues content = new ContentValues();
        content.put(QuotationSchema.COL_QUOTE_TEXT, quotation.getQuoteText());
        content.put(QuotationSchema.COL_AUTHOR_NAME,quotation.getQuoteAuthor());

        try(SQLiteDatabase db = getWritableDatabase()){
            db.insert(QuotationSchema.TABLE_NAME,null,content);
        }catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void removeAllQuotationsInDatabase(){
        try (SQLiteDatabase db = getWritableDatabase()) {
            db.delete(QuotationSchema.TABLE_NAME, null, null);
        }catch (SQLiteException e) {
        e.printStackTrace();
        }
    }

    public void removeQuotationInDatabase(Quotation quotation){
        try (SQLiteDatabase db = getWritableDatabase()) {
            db.delete(QuotationSchema.TABLE_NAME, String.format("%s=?", QuotationSchema.COL_QUOTE_TEXT),
                    new String[]{quotation.getQuoteText()});
            db.close();
        }catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT);" ,
                QuotationSchema.TABLE_NAME,
                QuotationSchema._ID,
                QuotationSchema.COL_QUOTE_TEXT,
                QuotationSchema.COL_AUTHOR_NAME));
    }

    public ArrayList<Quotation> getListAllQuotations(){
        ArrayList<Quotation> qList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor qCursor = database.query(QuotationSchema.TABLE_NAME, new String[]{QuotationSchema.COL_QUOTE_TEXT,
                QuotationSchema.COL_AUTHOR_NAME}, null, null, null, null, null);
        while (qCursor.moveToNext()){
            String qText = qCursor.getString(0);
            String qAuthor = qCursor.getString(1);
            qList.add(new Quotation(qText,qAuthor));
        }
    return qList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public static synchronized QuotationSQLiteHelper getInstance(Context context){
        if (instance == null ){
            instance = new QuotationSQLiteHelper(context);
        }
        return instance;
    }
}


