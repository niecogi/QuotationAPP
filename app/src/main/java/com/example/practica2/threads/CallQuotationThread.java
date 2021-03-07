package com.example.practica2.threads;

import android.content.Context;
import android.net.Uri;

import com.example.practica2.QuotationActivity;
import com.example.practica2.quotation.Quotation;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CallQuotationThread extends Thread {
    private static Language selectedLanguage = Language.EN;
    private static Method selectedMethod = Method.GET;
    private final WeakReference<QuotationActivity> quotationActivity;
    private Quotation quotation;
    private static final String SCHEME = "https";
    private static final String AUTHORITY = "api.forismatic.com";
    private static final String PATH = "/api/1.0/";
    private String url;

    //private Language selectedLanguage;


    public enum Language {EN,RU}
    public enum Method {GET,POST}


    public static void setLanguage(String lang){
        if(lang == "EN"){
            selectedLanguage = Language.EN;
        }else if (lang == "RU") {
            selectedLanguage = Language.RU;
        }
    }

    public static void setMethod(String m){
        if (m == "GET"){
            selectedMethod = Method.GET;
        }else if(m == "POST"){
            selectedMethod = Method.POST;
        }

    }



    public CallQuotationThread(QuotationActivity quotationActivity)
    {
        this.quotationActivity= new WeakReference<>(quotationActivity);
    }
    @Override
    public void run() {
        Method method = selectedMethod;
        Language language = selectedLanguage;
        Uri.Builder uri = new Uri.Builder()
                .scheme("https")
                .authority("api.forismatic.com")
                .appendPath("api")
                .appendPath("1.0")
                .appendPath("");

        if( method  ==  Method.GET){
            uri.appendQueryParameter("method","getQuote");
            uri.appendQueryParameter("format","json");
            uri.appendQueryParameter("lang",language.toString().toLowerCase());
            System.out.println(language.toString());
            try {
                URL url= new URL(uri.build().toString());
                System.out.println(url.toString());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    System.out.print("GET");
                    // Open an input channel to receive the response from the web service
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    // Create a Gson object through a GsonBuilder to process the response

                    Gson gson = new Gson();
                    System.out.println(gson);
                    // Deserializes the JSON response into a Quotation object
                    quotation = gson.fromJson(reader, Quotation.class);
                    System.out.println(quotation);
                    // Close the input channel
                    reader.close();
                }
                connection.disconnect();
                System.out.println("la conexion se ha cerrado");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(method == Method.POST){
            String body;
            body = "method=getQuote&format=json&lang=en";
            if(selectedLanguage == Language.EN ){
                body = "method=getQuote&format=json&lang=en";
            }else if(selectedLanguage == Language.RU){
                 body = "method=getQuote&format=json&lang=ru";
            }
            try {
                URL url = new URL(uri.build().toString());
                System.out.println(url.toString());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                System.out.println("POST");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader read = new InputStreamReader(connection.getInputStream());
                        // Create a Gson object through a GsonBuilder to process the response
                        Gson gson = new Gson();
                        System.out.println(gson);
                        System.out.println(read);
                        // Deserializes the JSON response into a Quotation object
                        quotation = gson.fromJson(read, Quotation.class);
                        System.out.println(quotation);
                        // Close the input channel
                        read.close();

                }
                connection.disconnect();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        Context context = this.quotationActivity.get();
        if (context != null){
            quotationActivity.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    quotationActivity.get().upgradeLabels(quotation);

                }
            });

        }

    }





    }

