package com.example.practica2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.practica2.R;
import com.example.practica2.quotation.Quotation;

import java.util.List;

//Adapter
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
private List<Quotation> qList;
private OnItemClickListener itemClickListener;
private OnItemLongClickListener itemLongClickListener;

    public Adapter(List<Quotation> list, OnItemClickListener itemClickListener,OnItemLongClickListener itemLongClickListener){
        this.qList = list;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
    }


    //Methods//
    public Quotation getPositionQuotation(int pos){
        return qList.get(pos);
    }

    public void removeQuotationPosition (int pos){
        qList.remove(pos);
        notifyItemRemoved(pos);

    }
    public void removeAllQuotations(){
        qList.clear();
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quotation_list_row, parent, false);
        Adapter.ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewQuote.setText(qList.get(position).getQuoteText());
        holder.textViewQuoteAuthor.setText(qList.get(position).getQuoteAuthor());
    }
    @Override
    public int getItemCount() { return qList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewQuote;
        public TextView textViewQuoteAuthor;
        public ViewHolder(View view) {
            super(view);
            textViewQuote = (TextView) view.findViewById(R.id.textQuote);
            textViewQuoteAuthor = (TextView) view.findViewById(R.id.textAuthor);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClickListener(Adapter.this, getAdapterPosition());
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    return itemLongClickListener.onItemLongClickListener(Adapter.this,getAdapterPosition());
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClickListener(Adapter adapter, int position);
    }
    public interface OnItemLongClickListener {
        boolean onItemLongClickListener(Adapter adapter, int position);
    }
}

