package com.perusahaan.fullname.odcfinder.model;

/**
 * Created by Full Name on 3/6/2019.
 */

public class LocationModel {
    private String name;
    private float latitude;
    private float longitude;

    public LocationModel(String name, float latitude, float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

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
}