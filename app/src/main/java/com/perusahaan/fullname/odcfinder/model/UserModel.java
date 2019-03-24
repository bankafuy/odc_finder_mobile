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
    private String level;
    private String photo;

    public UserModel(Integer id, String username, String nama, String level, String photo) {
        this.id = id;
        this.username = username;
        this.nama = nama;
        this.level = level;
        this.photo = photo;
    }

    protected UserModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        username = in.readString();
        nama = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(nama);
        parcel.writeString(level);
        parcel.writeString(photo);
    }
}
