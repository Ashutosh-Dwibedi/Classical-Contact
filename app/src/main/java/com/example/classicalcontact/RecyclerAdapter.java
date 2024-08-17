package com.example.classicalcontact;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    //Context context;
    ArrayList<ContactDetails> receivedValue;
    private final OnNoteListener onNoteListener;
    @SuppressLint("NotifyDataSetChanged")
    RecyclerAdapter(ArrayList<ContactDetails>receivedValue, OnNoteListener onNoteListener){
        //this.context=context;
        this.receivedValue=receivedValue;
        this.onNoteListener=onNoteListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_custom_view,parent,false);
        return new ViewHolder(v, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageResource(R.drawable.blank_contact_photo);
        holder.name.setText(receivedValue.get(position).contact_name);
        holder.number.setText(receivedValue.get(position).contact_number);

    }
    @Override
    public int getItemCount() {
        return receivedValue.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,number;
        ImageView image;
        OnNoteListener onNoteListener;
        ImageButton call,message,info;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            name=itemView.findViewById(R.id.card_textView);
            number=itemView.findViewById(R.id.card_contact_number);
            image=itemView.findViewById(R.id.ShapeableImageView);
            this.onNoteListener=onNoteListener;
            call=itemView.findViewById(R.id.call_image_button);
            message=itemView.findViewById(R.id.message_image_button);
            info=itemView.findViewById(R.id.info_image_button);
            call.setOnClickListener(this);
            message.setOnClickListener(this);
            info.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clicked_button_id=v.getId();
            onNoteListener.onNoteClick(getAdapterPosition(),clicked_button_id,call.getId(), message.getId(), info.getId());
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position,int clicked_button_id,int call_button_id,int message_button_id,int info_button_id);
    }
}
