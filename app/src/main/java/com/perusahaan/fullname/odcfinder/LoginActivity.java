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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perusahaan.fullname.odcfinder.Utils.Constant;
import com.perusahaan.fullname.odcfinder.Utils.MyUtils;
import com.perusahaan.fullname.odcfinder.model.SampleObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        msgYesNo(this, "Yakin ingin keluar ?");
    }

    private static final String LOCATION_URL = "https://jsonplaceholder.typicode.com/todos";

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
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button btnLogin = findViewById(R.id.email_sign_in_button);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void attemptLogin()  {
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
            MyUtils.showSimpleProgressDialog(this, "Judul Login", "Sabar 3 detik.", false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyUtils.removeSimpleProgressDialog();
                    Random random = new Random();
                    int i = random.nextInt();

                    if(i % 2 == 0) {
                        onSuccess();
                    } else {
                        onFailed();
                    }

                }
            }, 3000);


//            prefs.edit().putBoolean(Constant.PREF_LOGIN, true).apply();

//            Intent intent = getIntent();

//            StringRequest stringRequest = new StringRequest(Request.Method.GET, LOCATION_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.d("L", response.toString());
//                        for(int i = 0 ; i<10000; i++) {
//                            Log.d("", String.valueOf(i));
//                        }
//
//                        MyUtils.removeSimpleProgressDialog();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//
//            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
//            requestQueue.add(stringRequest);
        }
    }

    private void onSuccess() {
        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
        prefs.edit().putBoolean(Constant.PREF_LOGIN, true).apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void onFailed() {
        Toast.makeText(LoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
    }

    private void msgYesNo(Context context, String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Yakin ingin keluar dari aplikasi?")
                .setMessage(message)
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

