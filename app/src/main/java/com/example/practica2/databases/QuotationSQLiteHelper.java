package com.example.practica2.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.practica2.databases.QuotationContract.QuotationBaseColumns;
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
        SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.query(QuotationBaseColumns.tableName, null,
                    String.format("%s=?", QuotationBaseColumns.colName_quoteText), new String[]{quotation.getQuoteText()}, null,
                    null, null,null);

            if (cursor.getCount() > 0) {
                is_in_database = true;
            } else {
                is_in_database = false;
            }
            cursor.close();
            database.close();
            return is_in_database;
    }

    public void addQuotationInDatabase(Quotation quotation){
        ContentValues content = new ContentValues();
        content.put(QuotationBaseColumns.colName_quoteText, quotation.getQuoteText());
        content.put(QuotationBaseColumns.colName_authorName,quotation.getQuoteAuthor());

        try(SQLiteDatabase db = getWritableDatabase()){
            db.insert(QuotationBaseColumns.tableName,null,content);
            db.close();
            //??? HACE FALTA?
        }catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void removeAllQuotationsInDatabase(){
        try (SQLiteDatabase db = getWritableDatabase()) {
            db.delete(QuotationBaseColumns.tableName, null, null);
            db.close();
        }catch (SQLiteException e) {
        e.printStackTrace();
        }

    }

    public void removeQuotationInDatabase(Quotation quotation){
        try (SQLiteDatabase db = getWritableDatabase()) {
            db.delete(QuotationBaseColumns.tableName, String.format("%s=?", QuotationBaseColumns.colName_quoteText),
                    new String[]{quotation.getQuoteText()});
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
                QuotationContract.QuotationBaseColumns.tableName,
                QuotationContract.QuotationBaseColumns._ID,
                QuotationContract.QuotationBaseColumns.colName_quoteText,
                QuotationContract.QuotationBaseColumns.colName_authorName));

    }
    public ArrayList<Quotation> getListAllQuotations(){
        ArrayList<Quotation> qList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor qCursor = database.query(QuotationBaseColumns.tableName, new String[]{QuotationBaseColumns.colName_quoteText,
                QuotationBaseColumns.colName_authorName}, null, null, null, null, null);
        while (qCursor.moveToNext()){
            String qText = qCursor.getString(0);
            String qAuthor = qCursor.getString(1);
            qList.add(new Quotation(qText,qAuthor));
        }
    return qList;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static synchronized QuotationSQLiteHelper getInstance(Context context){
        if (instance ==null ){
            instance = new QuotationSQLiteHelper(context);
        }
        return instance;
    }



}


