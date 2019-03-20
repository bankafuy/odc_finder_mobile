package com.perusahaan.fullname.odcfinder.model;

/**
 * Created by Full Name on 3/20/2019.
 */

public class BaseRequestModel<C> {
    private String token;
    private C data;

    public String getToken() {
        return token;
    }
    public C getData() {
        return data;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public void setData(C data) {
        this.data = data;
    }
}
