package com.example.classicalcontact;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;

public class contacts_fragment extends Fragment implements RecyclerAdapter.OnNoteListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView_contacts;
    ArrayList<ContactDetails> contactsList=new ArrayList<>();
    ArrayList<ContactDetails> refinedContactsList=new ArrayList<>();
    androidx.appcompat.widget.SearchView searchView_contacts;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton floatingActionButton,contacts_report_generation;
    int flag=0;
    public contacts_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contacts_fragment, container, false);
        ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);
        recyclerView_contacts=view.findViewById(R.id.contacts_recyclerView);
        swipeRefreshLayout=view.findViewById(R.id.contacts_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        floatingActionButton=view.findViewById(R.id.add_new_contacts_button);
        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(requireActivity(), SaveNewContact.class)));
        contacts_report_generation=view.findViewById(R.id.report_contacts_button);
        contacts_report_generation.setOnClickListener(v -> {
            Intent report_intent=new Intent(requireActivity(),ContactReport.class);
            report_intent.putExtra("Contacts Data",contactsList);
            startActivity(report_intent);
        });
        searchView_contacts=view.findViewById(R.id.contacts_searchView);
        searchView_contacts.clearFocus();
        contactsListRetrieve();
        searchView_contacts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                disableSoftKeyboard();
                flag=1;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                flag=1;
                searchFilterContacts(newText);
                return true;
            }
        });
        recyclerView_contacts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_contacts.setAdapter(new RecyclerAdapter(contactsList,this));
        return view;
    }

    private void searchFilterContacts(String newText){
        refinedContactsList.clear();
        for (ContactDetails item:contactsList){
            for (int i=0;i<=contactsList.size();i++){
                if (item.contact_name!=null){
                    if (item.contact_name.toLowerCase().contains(newText.toLowerCase())){
                        refinedContactsList.add(item);
                        break;
                    }
                }
            }
        }
        if (!refinedContactsList.isEmpty()){
            recyclerView_contacts.setAdapter(new RecyclerAdapter(refinedContactsList,this));
        }
    }

    private void contactsListRetrieve() {
        String[] projection=new String[]{
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
        };
        try (Cursor cursorContacts = requireActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" COLLATE LOCALIZED ASC")) {
            HashSet<String> foundNormalizedNumbers=new HashSet<>();
            assert cursorContacts != null;
            if (cursorContacts.getCount() > 0) {
                int indexOfNormalizedNumber = cursorContacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                while (cursorContacts.moveToNext()) {
                    String normalizedNumber=cursorContacts.getString(indexOfNormalizedNumber);
                    if (foundNormalizedNumbers.add(normalizedNumber)){
                        String contactName = cursorContacts.getString(cursorContacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String contactNumber = cursorContacts.getString(cursorContacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        ContactDetails retrievedContacts=new ContactDetails(contactName,contactNumber);
                        contactsList.add(retrievedContacts);
                    }
                }
            }
        } catch (Exception e) {
            Log.d("RetrieveError", "contactsListRetrieve: ",e);
        }
    }
    private void disableSoftKeyboard(){
        InputMethodManager iManager=(InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view= requireActivity().getCurrentFocus();
        if (view==null)
            view=new View(getContext());
        iManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    @Override
    public void onNoteClick(int position, int clicked_button_id, int call_button_id, int message_button_id, int info_button_id) {
        Intent intent=new Intent(requireActivity(), InfoActivity.class);
        if (flag==0){
            if (clicked_button_id==call_button_id){
                String dial="tel:"+contactsList.get(position).contact_number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else if (clicked_button_id==message_button_id) {
                Intent message_intent=new Intent(Intent.ACTION_SENDTO);
                message_intent.setData(Uri.parse("smsto:"+Uri.encode(contactsList.get(position).contact_number)));
                startActivity(message_intent);
            } else if (clicked_button_id==info_button_id) {
                intent.putExtra("activity_reference","Contacts_Fragment");
                intent.putExtra("saved_contact_data", contactsList.get(position));
                startActivity(intent);
            }
        } else if (flag==1) {
            if (clicked_button_id==call_button_id){
                String dial="tel:"+refinedContactsList.get(position).contact_number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else if (clicked_button_id==message_button_id) {
                Intent message_intent=new Intent(Intent.ACTION_SENDTO);
                message_intent.setData(Uri.parse("smsto:"+Uri.encode(refinedContactsList.get(position).contact_number)));
                startActivity(message_intent);
            } else if (clicked_button_id==info_button_id) {
                intent.putExtra("activity_reference","Contacts_Fragment");
                intent.putExtra("saved_contact_data", refinedContactsList.get(position));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRefresh() {
        contactsList.clear();
        contactsListRetrieve();
        recyclerView_contacts.setAdapter(new RecyclerAdapter(contactsList,this));
        swipeRefreshLayout.setRefreshing(false);
    }
}