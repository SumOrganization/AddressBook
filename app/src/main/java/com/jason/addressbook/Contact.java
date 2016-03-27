package com.jason.addressbook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jason on 2016/3/12.
 */
public class Contact implements Parcelable {
    private String id = null;
    private String name = null;
    private String phoneNumber = null;
    private String password = null;
    private String sex = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(phoneNumber);
        parcel.writeString(password);
        parcel.writeString(sex);
    }
    public static final Parcelable.Creator<Contact> CREATOR = new Creator<Contact>()
    {
        @Override
        public Contact[] newArray(int size)
        {
            return new Contact[size];
        }

        @Override
        public Contact createFromParcel(Parcel in)
        {
            return new Contact(in);
        }
    };
    public Contact()
    {}
    public Contact(Parcel in)
    {
        id = in.readString();
        name = in.readString();
        phoneNumber = in.readString();
        password = in.readString();
        sex = in.readString();
    }
}
