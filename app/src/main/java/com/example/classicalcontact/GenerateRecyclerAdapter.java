package com.example.classicalcontact;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GenerateRecyclerAdapter extends RecyclerView.Adapter<GenerateRecyclerAdapter.ViewHolder> {
    ArrayList<ContactDetails> receivedValue;
    @SuppressLint("NotifyDataSetChanged")
    GenerateRecyclerAdapter(ArrayList<ContactDetails>receivedValue){
        //this.context=context;
        this.receivedValue=receivedValue;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenerateRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_retrieval,parent,false);
        return new GenerateRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenerateRecyclerAdapter.ViewHolder holder, int position) {
        holder.name.setText(receivedValue.get(position).contact_name);
        holder.number.setText(receivedValue.get(position).contact_number);

    }
    @Override
    public int getItemCount() {
        return receivedValue.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.card_textView);
            number=itemView.findViewById(R.id.card_contact_number);
        }
    }
}
