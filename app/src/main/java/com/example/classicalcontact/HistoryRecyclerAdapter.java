package com.example.classicalcontact;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder>{
    //Context context;
    ArrayList<CallHistory> receivedValue;
    private final OnNoteListener onNoteListener;
    @SuppressLint("NotifyDataSetChanged")
    HistoryRecyclerAdapter(ArrayList<CallHistory>receivedValue, OnNoteListener onNoteListener){
        //this.context=context;
        this.receivedValue=receivedValue;
        this.onNoteListener=onNoteListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_recyclerview_layout_custom,parent,false);
        return new ViewHolder(v, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.history_name.setText(receivedValue.get(position).name);
        holder.history_number.setText(receivedValue.get(position).number);
        holder.history_duration.setText(receivedValue.get(position).duration);
        holder.history_date.setText(receivedValue.get(position).date);
        holder.history_call_type.setText(receivedValue.get(position).type);

    }
    @Override
    public int getItemCount() {
        return receivedValue.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView history_name,history_number,history_duration,history_date,history_call_type;
        OnNoteListener onNoteListener;
        ImageButton call,message,info;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            history_name=itemView.findViewById(R.id.history_name);
            history_number=itemView.findViewById(R.id.history_contact_number);
            history_duration=itemView.findViewById(R.id.history_duration);
            history_date=itemView.findViewById(R.id.history_date);
            history_call_type=itemView.findViewById(R.id.history_call_type);
            this.onNoteListener=onNoteListener;
            call=itemView.findViewById(R.id.history_call_image_button);
            message=itemView.findViewById(R.id.history_message_image_button);
            info=itemView.findViewById(R.id.history_info_image_button);
            call.setOnClickListener(this);
            message.setOnClickListener(this);
            info.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clicked_button_id=v.getId();
            onNoteListener.onNoteClick(getAdapterPosition(),clicked_button_id,call.getId(),message.getId(),info.getId());
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position,int clicked_button_id,int call_button_id,int message_button_id,int info_button_id);
    }
}

