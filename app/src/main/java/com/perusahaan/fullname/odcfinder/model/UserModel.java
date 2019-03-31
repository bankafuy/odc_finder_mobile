package com.perusahaan.fullname.odcfinder.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Full Name on 3/8/2019.
 */

public class UserModel implements Parcelable {
    private Integer id;
    private String username;
    private String nama;
    private String nik;
    private String noHp;
    private String level;
    private String photo;

    public UserModel(Integer id, String username, String nama, String nik, String noHp,String level, String photo) {
        this.id = id;
        this.username = username;
        this.nama = nama;
        this.nik = nik;
        this.noHp = noHp;
        this.level = level;
        this.photo = photo;
    }

    protected UserModel(Parcel in) {
        id = in.readInt();
        username = in.readString();
        nama = in.readString();
        nik = in.readString();
        noHp = in.readString();
        level = in.readString();
        photo = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public Integer getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getNama() {
        return nama;
    }
    public String getLevel() {
        return level;
    }
    public String getPhoto() {
        return photo;
    }
    public String getNik() {
        return nik;
    }
    public String getNoHp() {
        return noHp;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public void setNik(String nik) {
        this.nik = nik;
    }
    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(nama);
        parcel.writeString(nik);
        parcel.writeString(noHp);
        parcel.writeString(level);
        parcel.writeString(photo);
    }
}
