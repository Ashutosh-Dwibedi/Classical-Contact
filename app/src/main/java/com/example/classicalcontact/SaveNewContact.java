package com.example.classicalcontact;

import android.content.ContentProviderOperation;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class SaveNewContact extends AppCompatActivity implements View.OnClickListener{
    TextInputEditText new_contact_name,new_contact_number;
    AppCompatButton save_new_contact,cancel_adding_new_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_new_contact);
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_CONTACTS}, PackageManager.PERMISSION_GRANTED);
        new_contact_name=findViewById(R.id.new_contact_name_editText);
        new_contact_number=findViewById(R.id.new_contact_number_editText);
        save_new_contact=findViewById(R.id.new_contact_save_button);
        cancel_adding_new_contact=findViewById(R.id.new_contact_cancel_button);
        save_new_contact.setOnClickListener(this);
        cancel_adding_new_contact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int clicked_button_id=v.getId();
        if (clicked_button_id==save_new_contact.getId()){
            if (!Objects.requireNonNull(new_contact_name.getText()).toString().equals("")&&!Objects.requireNonNull(new_contact_number.getText()).toString().equals("")){
                ArrayList<ContentProviderOperation> contentProviderOperations=new ArrayList<>();
                contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,null).build());
                contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
                        .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,new_contact_name.getText().toString())
                        .build());

                contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,null).build());
                contentProviderOperations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
                        .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,new_contact_number.getText().toString())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());

                try {
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY,contentProviderOperations);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finish();
            }
            else {
                Toast toast=new Toast(this);
                View view=getLayoutInflater().inflate(R.layout.custom_vintage_toast_layout,findViewById(R.id.custom_vintage_toast));
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER,0,0);
                toast.show();
            }
        } else if (clicked_button_id==cancel_adding_new_contact.getId()) {
            finish();
        }
    }
}