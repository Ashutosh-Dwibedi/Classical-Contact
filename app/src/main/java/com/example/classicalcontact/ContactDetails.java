package com.example.classicalcontact;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ContactDetails implements Parcelable {
    int image;
    String contact_name,contact_number;
    public ContactDetails(int image, String contact_name, String contact_number){
        this.image=image;
        this.contact_name=contact_name;
        this.contact_number=contact_number;
    }
    public  ContactDetails(String contact_name,String contact_number){
        this.contact_number=contact_number;
        this.contact_name=contact_name;
    }

    protected ContactDetails(Parcel in) {
        image = in.readInt();
        contact_name = in.readString();
        contact_number = in.readString();
    }

    public static final Creator<ContactDetails> CREATOR = new Creator<ContactDetails>() {
        @Override
        public ContactDetails createFromParcel(Parcel in) {
            return new ContactDetails(in);
        }

        @Override
        public ContactDetails[] newArray(int size) {
            return new ContactDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeString(contact_name);
        dest.writeString(contact_number);
    }
}
