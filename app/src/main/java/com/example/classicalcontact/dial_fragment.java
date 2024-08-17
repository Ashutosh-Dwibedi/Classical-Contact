package com.example.classicalcontact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class dial_fragment extends Fragment implements View.OnClickListener{
    private static final int PERMISSION_STATUS=1;
    TextView dialed_input;
    Button dial_0,dial_1,dial_2,dial_3,dial_4,dial_5,dial_6,dial_7,dial_8,dial_9,dial_astrik,dial_hash;
    ImageButton call_button,backSpace;

    public dial_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dial_fragment, container, false);
        ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS}, PackageManager.PERMISSION_GRANTED);
        dialed_input=view.findViewById(R.id.dialed_number);
        dialed_input.setMovementMethod(new ScrollingMovementMethod());
        dialed_input.setHorizontallyScrolling(true);
        call_button=view.findViewById(R.id.call_button);
        backSpace=view.findViewById(R.id.backSpace);
        backSpaceVisibility();
        dial_0=view.findViewById(R.id.dial0);
        dial_1=view.findViewById(R.id.dial1);
        dial_2=view.findViewById(R.id.dial2);
        dial_3=view.findViewById(R.id.dial3);
        dial_4=view.findViewById(R.id.dial4);
        dial_5=view.findViewById(R.id.dial5);
        dial_6=view.findViewById(R.id.dial6);
        dial_7=view.findViewById(R.id.dial7);
        dial_8=view.findViewById(R.id.dial8);
        dial_9=view.findViewById(R.id.dial9);
        dial_astrik=view.findViewById(R.id.dial_astrik);
        dial_hash=view.findViewById(R.id.dial_hash);
        dial_0.setOnClickListener(this);
        dial_1.setOnClickListener(this);
        dial_2.setOnClickListener(this);
        dial_3.setOnClickListener(this);
        dial_4.setOnClickListener(this);
        dial_5.setOnClickListener(this);
        dial_6.setOnClickListener(this);
        dial_7.setOnClickListener(this);
        dial_8.setOnClickListener(this);
        dial_9.setOnClickListener(this);
        dial_astrik.setOnClickListener(this);
        dial_hash.setOnClickListener(this);
        call_button.setOnClickListener(v -> requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE));
        backSpace.setOnClickListener(v -> {
            StringBuilder stringBuilder=new StringBuilder(dialed_input.getText());
            stringBuilder.deleteCharAt(dialed_input.getText().length()-1);
            String newString=stringBuilder.toString();
            dialed_input.setText(newString);
            backSpaceVisibility();
        });
        // Inflate the layout for this fragment
        return view;
    }
    private void backSpaceVisibility(){
        if (dialed_input.getText().toString().isEmpty())
            backSpace.setVisibility(View.GONE);
        else
            backSpace.setVisibility(View.VISIBLE);
    }

    private void makePhoneCall(){
        String number=dialed_input.getText().toString();
        if(!number.isEmpty()){
            if(ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CALL_PHONE},PERMISSION_STATUS);
            }
            else {
                String dial="tel:"+number;
                startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
            }
        }
        else{
            Toast.makeText(getActivity(), "Please Allow permission to make call!", Toast.LENGTH_SHORT).show();
        }
    }
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    // PERMISSION GRANTED
                    makePhoneCall();
                } else {
                    // PERMISSION NOT GRANTED
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
    );


    @Override
    public void onClick(View v) {
        Button typed=(Button) v;
        switch(typed.getText().toString()){
            case "0":
                dialed_input.setText(String.format("%s0", dialed_input.getText().toString()));
                break;
            case "1":
                dialed_input.setText(String.format("%s1", dialed_input.getText().toString()));
                break;
            case "2":
                dialed_input.setText(String.format("%s2", dialed_input.getText().toString()));
                break;
            case "3":
                dialed_input.setText(String.format("%s3", dialed_input.getText().toString()));
                break;
            case "4":
                dialed_input.setText(String.format("%s4", dialed_input.getText().toString()));
                break;
            case "5":
                dialed_input.setText(String.format("%s5", dialed_input.getText().toString()));
                break;
            case "6":
                dialed_input.setText(String.format("%s6", dialed_input.getText().toString()));
                break;
            case "7":
                dialed_input.setText(String.format("%s7", dialed_input.getText().toString()));
                break;
            case "8":
                dialed_input.setText(String.format("%s8", dialed_input.getText().toString()));
                break;
            case "9":
                dialed_input.setText(String.format("%s9", dialed_input.getText().toString()));
                break;
            case "*":
                dialed_input.setText(String.format("%s*", dialed_input.getText().toString()));
                break;
            case "#":
                dialed_input.setText(String.format("%s#", dialed_input.getText().toString()));
                break;
        }
        backSpaceVisibility();
    }
}