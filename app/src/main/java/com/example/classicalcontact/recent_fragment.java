package com.example.classicalcontact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.MessageFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.util.ArrayList;

public class recent_fragment extends Fragment implements HistoryRecyclerAdapter.OnNoteListener{
    ArrayList<CallHistory> recent_calls=new ArrayList<>();
    ArrayList<String> retrieved_numbers=new ArrayList<>();
    ArrayList<String> retrieved_names=new ArrayList<>();
    ArrayList<CallHistory> refined_history_list=new ArrayList<>();
    RecyclerView recyclerView;
    SearchView searchView;
    String call_number;
    int flag=0;
    public recent_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_recent_fragment, container, false);
        ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.READ_CALL_LOG,
                Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);
        recyclerView=view.findViewById(R.id.recent_recyclerView);
        searchView=view.findViewById(R.id.recent_searchView);
        searchView.clearFocus();
        callLog();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                disableSoftKeyboard();
                flag=1;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                flag=1;
                searchFilter(newText);
                return true;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new HistoryRecyclerAdapter(recent_calls,this));
        return view;
    }
    private void searchFilter(String newText){
        refined_history_list.clear();
        for(CallHistory item:recent_calls){
            for(int i=0;i<=recent_calls.size();i++){
                if (item.name!=null){
                    if (item.name.toLowerCase().contains(newText.toLowerCase())){
                        refined_history_list.add(item);
                        break;
                    }
                }
            }
        }
        if (!refined_history_list.isEmpty()){
            recyclerView.setAdapter(new HistoryRecyclerAdapter(refined_history_list,this));
        }
    }
    private void callLog(){
        String[] projection = new String[]{
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE
        };
        //Uri uri_Call_Logs = Uri.parse("content://call_log/calls");
        Cursor cursorCallLogs= requireActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI,projection,null,null,CallLog.Calls.DATE+" ASC");
        cursorCallLogs.moveToLast();
        int i=0;
        while (cursorCallLogs.moveToPrevious()) {
            i++;
            if (i<=100){
                call_number=cursorCallLogs.getString(cursorCallLogs.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                String call_name=cursorCallLogs.getString(cursorCallLogs.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                String call_duration=cursorCallLogs.getString(cursorCallLogs.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                call_duration=durationModification(call_duration);
                String call_type=cursorCallLogs.getString(cursorCallLogs.getColumnIndexOrThrow(CallLog.Calls.TYPE));
                String type=null;
                switch (call_type){
                    case "2":
                        type="OUTGOING";
                        break;
                    case "1":
                        type="INCOMING";
                        break;
                    case "3":
                        type="MISSED";
                        break;
                }
                String call_date=cursorCallLogs.getString(cursorCallLogs.getColumnIndexOrThrow(CallLog.Calls.DATE));
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy KK:mm:ss a");
                String dateString = formatter.format(new Date(Long.parseLong(call_date)));
                dateString=dateString.toUpperCase();

                CallHistory captured_history=new CallHistory(call_name,call_number,call_duration,type,dateString);
                recent_calls.add(captured_history);
                retrieved_names.add(call_name);
                retrieved_numbers.add(call_number);
            }

        }
        cursorCallLogs.close();
    }
    private String durationModification(String call_duration){
        int duration_in_seconds=Integer.parseInt(call_duration);
        int hour=duration_in_seconds/3600;
        int minute=(duration_in_seconds%3600)/60;
        int second=duration_in_seconds%60;
        String modified_duration;
        if (hour==0&&minute==0&&second==0)
            modified_duration= MessageFormat.format("{0}:{1}:{2}","00","00","00");
        else if(second==0&&hour!=0&&minute!=0)
            modified_duration= MessageFormat.format("{0}:{1}:{2}",hour,minute,"00");
            //modified_duration=String.format("%02d:%02d:%02d",hour,minute,"00");
        else if(second!=0&&hour!=0&&minute==0)
            modified_duration= MessageFormat.format("{0}:{1}:{2}",hour,"00",second);
        else if(hour==0&&minute!=0&&second!=0)
            modified_duration= MessageFormat.format("{0}:{1}:{2}","00",minute,second);
        else if(hour != 0 && minute == 0)
            modified_duration= MessageFormat.format("{0}:{1}:{2}",hour,"00","00");
        else if(hour == 0 && minute != 0)
            modified_duration= MessageFormat.format("{0}:{1}:{2}","00",minute,"00");
        else if(hour == 0)
            modified_duration= MessageFormat.format("{0}:{1}:{2}","00","00",second);
        else
            modified_duration= MessageFormat.format("{0}:{1}:{2}",hour,minute,second);

        return modified_duration;
    }
    @Override
    public void onNoteClick(int position, int clicked_button_id,int call_button_id,int message_button_id,int info_button_id) {
        Intent intent=new Intent(requireActivity(), InfoActivity.class);
        if (flag==0){
            if (clicked_button_id==call_button_id){
                String dial="tel:"+retrieved_numbers.get(position);
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else if (clicked_button_id==message_button_id) {
                Intent message_intent=new Intent(Intent.ACTION_SENDTO);
                message_intent.setData(Uri.parse("smsto:"+Uri.encode(recent_calls.get(position).number)));
                startActivity(message_intent);
            } else if (clicked_button_id==info_button_id) {
                intent.putExtra("activity_reference","Recent_Fragment");
                intent.putExtra("contact_data", recent_calls.get(position));
                startActivity(intent);
            }
        } else if (flag==1) {

            if (clicked_button_id==call_button_id){
                String dial="tel:"+refined_history_list.get(position).number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else if (clicked_button_id==message_button_id) {
                Intent message_intent=new Intent(Intent.ACTION_SENDTO);
                message_intent.setData(Uri.parse("smsto:"+Uri.encode(refined_history_list.get(position).number)));
                startActivity(message_intent);
            } else if (clicked_button_id==info_button_id) {
                intent.putExtra("activity_reference","Recent_Fragment");
                intent.putExtra("contact_data", refined_history_list.get(position));
                startActivity(intent);
            }
        }
    }
    private void disableSoftKeyboard(){
        InputMethodManager iManager=(InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view= requireActivity().getCurrentFocus();
        if (view==null)
            view=new View(getContext());
        iManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}