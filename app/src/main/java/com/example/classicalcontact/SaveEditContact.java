package com.example.classicalcontact;

import android.content.ContentProviderOperation;
import android.content.Intent;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class SaveEditContact extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout name_textInputLayout,number_textInputLayout;
    AppCompatButton save_button,cancel_button;
    TextInputEditText entered_name,entered_number;
    String existing_name,existing_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_edit_contact);
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_CONTACTS}, PackageManager.PERMISSION_GRANTED);
        Intent intent=getIntent();
        existing_name=intent.getStringExtra("current_contact_name");
        existing_number=intent.getStringExtra("current_contact_number");
        entered_name=findViewById(R.id.name_editText);
        entered_number=findViewById(R.id.number_editText);
        name_textInputLayout=findViewById(R.id.name_edit_input_layout);
        number_textInputLayout=findViewById(R.id.number_edit_input_layout);
        save_button=findViewById(R.id.save_button);
        cancel_button=findViewById(R.id.cancel_button);
        entered_name.setText(existing_name);
        if (existing_number.charAt(0)!='+'){
            number_textInputLayout.setPrefixText("+91");
        }
        entered_number.setText(existing_number);
        save_button.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int clicked_button_id=v.getId();
        String new_contact_name= Objects.requireNonNull(entered_name.getText()).toString();
        String new_contact_number= Objects.requireNonNull(entered_number.getText()).toString();
        if (clicked_button_id == save_button.getId()) {
            if (!new_contact_name.equals("")&&!new_contact_number.equals("+91")&&!new_contact_number.equals("")) {
                ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<>();
                contentProviderOperations.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.Data.DISPLAY_NAME + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
                                new String[]{existing_name, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE})
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, new_contact_number)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
                contentProviderOperations.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(ContactsContract.Data.DISPLAY_NAME + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
                                new String[]{existing_name, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE})
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,new_contact_name)
                        .build());
                try {
                    getContentResolver().applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
                } catch (Exception e) {
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
        }
        else if (clicked_button_id == cancel_button.getId()) {
            finish();
        }
    }
}