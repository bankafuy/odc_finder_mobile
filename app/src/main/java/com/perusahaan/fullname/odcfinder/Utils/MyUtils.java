package com.perusahaan.fullname.odcfinder.Utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Full Name on 3/8/2019.
 */

public class MyUtils {

    public static ProgressDialog progressDialog = null;

    public static void removeSimpleProgressDialog() {
        try {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg) {
        try {
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(context, title, msg);
                progressDialog.setCancelable(false);
            }

            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
