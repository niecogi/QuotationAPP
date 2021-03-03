package com.example.practica2.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.practica2.quotation.Quotation;

import java.util.List;

@Dao
public interface QuotationRoomDAO
{
    @Insert
    void addQuotation(Quotation quotation);

    @Delete
    void removeQuotation(Quotation quotation);

    @Query("SELECT * FROM quotation_table")
    List<Quotation> getAllQuotationsFromDatabase();

    @Query("SELECT * FROM quotation_table WHERE quote = :text")
    Quotation getQuotationByText(String text);

    @Query("DELETE FROM quotation_table")
    void deleteAllQuotes();

}
