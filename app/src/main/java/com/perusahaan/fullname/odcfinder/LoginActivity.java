package com.perusahaan.fullname.odcfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perusahaan.fullname.odcfinder.Utils.Constant;
import com.perusahaan.fullname.odcfinder.Utils.MyUtils;
import com.perusahaan.fullname.odcfinder.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        msgYesNo(this);
    }

    private static final String LOGIN_URL = "http://khotibul.herokuapp.com/login";
    private AutoCompleteTextView txtUsername;
    private EditText txtPassword;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = this.getSharedPreferences("com.perusahaan.fullname.odcfinder", Context.MODE_PRIVATE);

        txtUsername = findViewById(R.id.txtUsername);

        txtPassword = findViewById(R.id.txtPassword);
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    try {
                        attemptLogin();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        Button btnLogin = findViewById(R.id.email_sign_in_button);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void attemptLogin() throws JSONException {
        txtUsername.setError(null);
        txtPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            txtUsername.setError(getString(R.string.error_field_required));
            focusView = txtUsername;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError(getString(R.string.error_field_required));
            focusView = txtPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            MyUtils.showSimpleProgressDialog(this, "Mencoba login...", "Harap bersabar...");

            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", txtUsername.getText().toString());
            jsonBody.put("password", txtPassword.getText().toString());
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            MyUtils.removeSimpleProgressDialog();
                            if(response == null || response.contains("invalid credential")) {
                                onFailed();
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Integer id = jsonObject.getInt("id");
                                    String username = jsonObject.getString("username");
                                    String nama = jsonObject.getString("nama_lengkap");
                                    String nik = jsonObject.getString("nik");
                                    String no_hp = jsonObject.getString("no_hp");
                                    String level = jsonObject.getString("level");
                                    String photo = jsonObject.getString("photo");


                                    UserModel userModel = new UserModel(id, username, nama, nik, no_hp, level, photo);

                                    onSuccess(userModel);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MyUtils.removeSimpleProgressDialog();
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void onSuccess(UserModel userModel) {
        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
        prefs.edit().putBoolean(Constant.PREF_LOGIN, true).apply();

        if(userModel != null) {
            prefs.edit().putInt(Constant.PREF_ID, userModel.getId()).apply();
            prefs.edit().putString(Constant.PREF_USERNAME, userModel.getUsername()).apply();
            prefs.edit().putString(Constant.PREF_NAME, userModel.getNama()).apply();
            prefs.edit().putString(Constant.PREF_NIK, userModel.getNik()).apply();
            prefs.edit().putString(Constant.PREF_NO_HP, userModel.getNoHp()).apply();
            prefs.edit().putString(Constant.PREF_LEVEL, userModel.getLevel()).apply();

            try {
              if(userModel.getPhoto() != null) {
                  FileOutputStream outputStream = getApplicationContext().openFileOutput("profile.jpg", Context.MODE_PRIVATE);
                  final byte[] bytes = Base64.decode(userModel.getPhoto(), Base64.DEFAULT);
                  outputStream.write(bytes);
                  outputStream.flush();
                  outputStream.close();
              }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void onFailed() {
        Toast.makeText(LoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
    }

    private void msgYesNo(Context context) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Yakin ingin keluar dari aplikasi?")
                .setMessage("Yakin ingin keluar ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.edit().putBoolean(Constant.PREF_LOGIN, false).apply();
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

}

