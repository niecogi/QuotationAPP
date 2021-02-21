package com.example.practica2.quotation;

public class Quotation {
    private String quoteText;
    private String quoteAuthor;

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
    public String getQuoteText() {
        return this.quoteText;
    }
    public String getQuoteAuthor() {
        return this.quoteAuthor;
    }

}
