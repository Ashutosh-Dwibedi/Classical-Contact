package com.example.classicalcontact;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CallHistory implements Parcelable {
    String name,number,duration,type,date;
    CallHistory(String name, String number, String duration,String type,String date){
        this.name=name;
        this.number=number;
        this.duration=duration;
        this.type=type;
        this.date=date;
    }

    protected CallHistory(Parcel in) {
        name = in.readString();
        number = in.readString();
        duration = in.readString();
        type = in.readString();
        date = in.readString();
    }

    public static final Creator<CallHistory> CREATOR = new Creator<CallHistory>() {
        @Override
        public CallHistory createFromParcel(Parcel in) {
            return new CallHistory(in);
        }

        @Override
        public CallHistory[] newArray(int size) {
            return new CallHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(duration);
        dest.writeString(type);
        dest.writeString(date);
    }
}
