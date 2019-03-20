package com.perusahaan.fullname.odcfinder.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Full Name on 3/6/2019.
 */

public class LocationModel implements Parcelable {

    private String name;
    private float latitude;
    private float longitude;

    public LocationModel(String name, float latitude, float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected LocationModel(Parcel in) {
        name = in.readString();
        latitude = in.readFloat();
        longitude = in.readFloat();
    }

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return name;// + ";" + latitude + ";" + longitude + ";";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeFloat(latitude);
        parcel.writeFloat(longitude);
    }
}