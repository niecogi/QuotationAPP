package com.example.practica2.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.practica2.quotation.Quotation;

@Database(version = 1, entities = {Quotation.class}, exportSchema = false)
public abstract class QuotationRoomDatabase extends RoomDatabase
{

    private static QuotationRoomDatabase instance ;
    private static final String database_name = "quotation_database";
    public QuotationRoomDatabase(){}
    public abstract  QuotationRoomDAO getQuotationDAO();

    public static synchronized  QuotationRoomDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context,QuotationRoomDatabase.class,database_name).build();
        }
        return instance;
    }
    public static synchronized  void destroyInstance(){
        if (instance != null && instance.isOpen()){
            instance.close();
            instance = null;
        }
    }
}
