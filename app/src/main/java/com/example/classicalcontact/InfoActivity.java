package com.example.classicalcontact;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {
    TextView info_name,info_number;
    String activity_reference;
    ShapeableImageView info_shapeable_image_view;
    ImageButton info_call,info_message,info_edit,info_delete,info_back;
    ArrayList<CallHistory> from_recent_activity=new ArrayList<>();
    ArrayList<ContactDetails> from_contacts_activity=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);
        info_name=findViewById(R.id.card_info_name);
        info_number=findViewById(R.id.card_info_contact_number);
        info_call=findViewById(R.id.info_call_image_button);
        info_message=findViewById(R.id.info_message_image_button);
        info_edit=findViewById(R.id.info_edit_image_button);
        info_delete=findViewById(R.id.info_delete_image_button);
        info_back=findViewById(R.id.info_back_image_button);
        info_call.setOnClickListener(this);
        info_message.setOnClickListener(this);
        info_edit.setOnClickListener(this);
        info_delete.setOnClickListener(this);
        info_back.setOnClickListener(this);
        info_shapeable_image_view=findViewById(R.id.info_ShapeableImageView);
        Intent intent=getIntent();
        activity_reference=intent.getStringExtra("activity_reference");
        if (activity_reference.equals("Recent_Fragment")){
            from_recent_activity.add(intent.getParcelableExtra("contact_data"));
            info_name.setText(from_recent_activity.get(0).name);
            info_number.setText(from_recent_activity.get(0).number);
        } else if (activity_reference.equals("Contacts_Fragment")) {
            from_contacts_activity.add(intent.getParcelableExtra("saved_contact_data"));
            info_name.setText(from_contacts_activity.get(0).contact_name);
            info_number.setText(from_contacts_activity.get(0).contact_number);
        } else {
            info_name.setText("");
            info_number.setText("");
        }
    }
    private void deleteSpecificContact(String phone_number, String contact_name) {
        Uri  contact_uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phone_number));
        try (Cursor cursorContacts = this.getContentResolver().query(contact_uri,
                null, null, null,null)) {
            if (cursorContacts.moveToFirst()){
                do{
                    if (cursorContacts.getString(cursorContacts.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(contact_name)){
                        String lookupKey=cursorContacts.getString(cursorContacts.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri=Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI,lookupKey);
                        this.getContentResolver().delete(uri,null,null);
                    }
                }while (cursorContacts.moveToNext());
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int clicked_Image_button_id=v.getId();
        if (activity_reference.equals("Recent_Fragment"))
        {
            if (clicked_Image_button_id==info_call.getId()){
                String dial="tel:"+from_recent_activity.get(0).number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else if (clicked_Image_button_id==info_message.getId()) {
                Intent message_intent=new Intent(Intent.ACTION_SENDTO);
                message_intent.setData(Uri.parse("smsto:"+Uri.encode(from_recent_activity.get(0).number)));
                startActivity(message_intent);
            } else if (clicked_Image_button_id==info_edit.getId()) {
                Intent info_intent=new Intent(this, SaveEditContact.class);
                info_intent.putExtra("current_contact_name",from_recent_activity.get(0).name);
                info_intent.putExtra("current_contact_number",from_recent_activity.get(0).number);
                info_intent.putExtra("current_contact_details",from_recent_activity);
                startActivity(info_intent);
            } else if (clicked_Image_button_id==info_delete.getId()) {
                deleteSpecificContact(from_recent_activity.get(0).number,from_recent_activity.get(0).name);
            } else if (clicked_Image_button_id==info_back.getId()) {
                finish();
            }
        } else if (activity_reference.equals("Contacts_Fragment")) {
            if (clicked_Image_button_id==info_call.getId()){
                String dial="tel:"+from_contacts_activity.get(0).contact_number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } else if (clicked_Image_button_id==info_message.getId()) {
                Intent message_intent=new Intent(Intent.ACTION_SENDTO);
                message_intent.setData(Uri.parse("smsto:"+Uri.encode(from_contacts_activity.get(0).contact_number)));
                startActivity(message_intent);
            } else if (clicked_Image_button_id==info_edit.getId()) {
                Intent info_intent=new Intent(this, SaveEditContact.class);
                info_intent.putExtra("current_contact_name",from_contacts_activity.get(0).contact_name);
                info_intent.putExtra("current_contact_number",from_contacts_activity.get(0).contact_number);
                startActivity(info_intent);
                finish();
            } else if (clicked_Image_button_id==info_delete.getId()) {
                Dialog dialog=new Dialog(this);
                dialog.setContentView(R.layout.custom_dialog_box_layout);
                dialog.setCancelable(false);
                Button dialog_delete_button,dialog_cancel_button;
                dialog_delete_button=dialog.findViewById(R.id.dialog_delete_button);
                dialog_cancel_button=dialog.findViewById(R.id.dialog_cancel_Button);
                dialog_delete_button.setOnClickListener(v12 -> {
                    deleteSpecificContact(from_contacts_activity.get(0).contact_number,from_contacts_activity.get(0).contact_name);
                    finish();
                });
                dialog_cancel_button.setOnClickListener(v1 -> dialog.dismiss());
                dialog.show();
            } else if (clicked_Image_button_id==info_back.getId()) {
                finish();
            }
        }
    }
}