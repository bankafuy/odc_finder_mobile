package com.perusahaan.fullname.odcfinder.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Full Name on 3/8/2019.
 */

public class OdcModel implements Parcelable {
    private String namaOdc;
    private String kapasitas;
    private String datel;
    private String witel;
    private Float latitude;
    private Float longitude;

    protected OdcModel(Parcel in) {
        namaOdc = in.readString();
        kapasitas = in.readString();
        datel = in.readString();
        witel = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readFloat();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readFloat();
        }
    }

    public static final Creator<OdcModel> CREATOR = new Creator<OdcModel>() {
        @Override
        public OdcModel createFromParcel(Parcel in) {
            return new OdcModel(in);
        }

        @Override
        public OdcModel[] newArray(int size) {
            return new OdcModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(namaOdc);
        parcel.writeString(kapasitas);
        parcel.writeString(datel);
        parcel.writeString(witel);
        parcel.writeFloat(latitude);
        parcel.writeFloat(longitude);
    }
}
