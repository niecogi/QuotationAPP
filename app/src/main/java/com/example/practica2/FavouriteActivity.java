package com.example.practica2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.practica2.adapter.Adapter;
import com.example.practica2.quotation.Quotation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {
    private Adapter adapter;
    public ArrayList <Quotation> getMockQuotations() {
        ArrayList<Quotation> list = new ArrayList<>();
        list.add(new Quotation("example", "R.string.author1"));
        list.add(new Quotation("Beauty outside. Beast inside.", "Mac Pro"));
        list.add(new Quotation("American by birth. Rebel by choice", "Harley Davidson"));
        list.add(new Quotation("Don't be evil", "Google"));
        list.add(new Quotation("If you want to impress someone, put him on your Black list", "Johnnie Walker"));
        list.add(new Quotation("Live in your world. Play in ours", "PlayStation"));
        list.add(new Quotation("Impossible is nothing", "Adidas"));
        list.add(new Quotation("I am anonymous", "anom"));
        return list;
    }


    public void getAuthorInfo(Quotation q) throws UnsupportedEncodingException {
        if (q.getQuoteAuthor() == null || q.getQuoteAuthor().isEmpty()) {
            throw new IllegalArgumentException("The information cannot be obtained(author cannot be null or empty)");
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://en.wikipedia.org/wiki/Special:Search?search=" + URLEncoder.encode(q.getQuoteAuthor(), "UTF-8")));
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(manager);
        ArrayList<Quotation> list = getMockQuotations();
         adapter = new Adapter(list, new Adapter.OnItemClickListener(){
            @Override
            public void onItemClickListener(Adapter adapter, int position) {
                try {
                    getAuthorInfo(adapter.getPositionQuotation(position));
                } catch (IllegalArgumentException ex) {
                    Toast.makeText(FavouriteActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException ex) {
                    Toast.makeText(FavouriteActivity.this, "The information cannot be obtained",
                            Toast.LENGTH_SHORT).show();
                }
            }
        },new Adapter.OnItemLongClickListener(){
            public boolean onItemLongClickListener(Adapter adapter, int position) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FavouriteActivity.this);
                dialogBuilder.setMessage("¿Seguro que quieres borrar?"); //CAMBIAR
                dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final Quotation quotationToRemove = adapter.getPositionQuotation(position);
                    adapter.removeQuotationPosition(position);
                }


            });
                dialogBuilder.setNegativeButton(R.string.no, null);
                dialogBuilder.show();
                return true;
            }
        });

                recyclerView.setAdapter(adapter);
            }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav_menu,menu);
        menu.findItem(R.id.removeAllQuotesMenu).setVisible(adapter.getItemCount()>0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {
         if (item.getItemId() == R.id.removeAllQuotesMenu) {
             AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FavouriteActivity.this);
             dialogBuilder.setMessage(R.string.confirm_clear_all_fav);
             dialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     adapter.removeAllQuotations();
                     item.setVisible(false);
                 }
             });
             dialogBuilder.setNegativeButton(R.string.no, null);
             dialogBuilder.show();
             return true;
         }else{
                return super.onOptionsItemSelected(item);
            }
        }
    }








