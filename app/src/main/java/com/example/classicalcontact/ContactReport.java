package com.example.classicalcontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class ContactReport extends AppCompatActivity {
    RecyclerView report_recyclerview;
    ArrayList<ContactDetails> report_list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_report);
        Intent received_list=getIntent();
        report_list=received_list.getParcelableArrayListExtra("Contacts Data");
        report_recyclerview=findViewById(R.id.report_recyclerview);
        report_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        report_recyclerview.setAdapter(new GenerateRecyclerAdapter(report_list));
    }
}