package com.perusahaan.fullname.odcfinder.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Full Name on 3/8/2019.
 */

public class SampleObject implements Parcelable {

    private String title;
    private int userId;
    private int id;
    private boolean completed;

    public SampleObject() {
    }

    protected SampleObject(Parcel in) {
        title = in.readString();
        userId = in.readInt();
        id = in.readInt();
        completed = in.readByte() != 0;
    }

    public static final Creator<SampleObject> CREATOR = new Creator<SampleObject>() {
        @Override
        public SampleObject createFromParcel(Parcel in) {
            return new SampleObject(in);
        }

        @Override
        public SampleObject[] newArray(int size) {
            return new SampleObject[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(userId);
        parcel.writeInt(completed ? 1 : 0 );
        parcel.writeString(title);
    }
}
