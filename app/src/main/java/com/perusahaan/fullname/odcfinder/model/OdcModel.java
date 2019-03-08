package com.perusahaan.fullname.odcfinder.model;

import android.os.Parcelable;

/**
 * Created by Full Name on 3/8/2019.
 */

public class OdcModel {
    private String namaOdc;
    private String kapasitas;
    private String datel;
    private String witel;
    private Float latitude;
    private Float longitude;

    public String getNamaOdc() {
        return namaOdc;
    }
    public String getKapasitas() {
        return kapasitas;
    }
    public String getDatel() {
        return datel;
    }
    public String getWitel() {
        return witel;
    }
    public Float getLatitude() {
        return latitude;
    }
    public Float getLongitude() {
        return longitude;
    }

    public void setNamaOdc(String namaOdc) {
        this.namaOdc = namaOdc;
    }
    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }
    public void setDatel(String datel) {
        this.datel = datel;
    }
    public void setWitel(String witel) {
        this.witel = witel;
    }
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}
