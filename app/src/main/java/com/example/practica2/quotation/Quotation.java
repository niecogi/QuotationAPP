package com.example.practica2.quotation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "quotation_table")
public class Quotation
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id ;

    @NonNull
    @ColumnInfo(name = "quote")
    private String quoteText;

    @ColumnInfo(name = "author")
    private String quoteAuthor;

    public Quotation(){}

    @Ignore
    public Quotation(String quoteText, String quoteAuthor){
         this.quoteText = quoteText;
         this.quoteAuthor = quoteAuthor;
     }
    public void setQuoteText( String quoteText){
        this.quoteText = quoteText;
    }
    public void setQuoteAuthor( String quoteAuthor){
        this.quoteAuthor= quoteAuthor;
    }

    @NonNull
    public String getQuoteText() {
        return this.quoteText;
    }
    public String getQuoteAuthor() {
        return this.quoteAuthor;
    }
    public int getId(){
        return  id;
    }
    public void setId(int id){
        this.id=id;
    }

}
