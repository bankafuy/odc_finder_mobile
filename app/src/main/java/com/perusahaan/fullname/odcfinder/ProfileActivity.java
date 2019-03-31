package com.perusahaan.fullname.odcfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perusahaan.fullname.odcfinder.Utils.Constant;
import com.perusahaan.fullname.odcfinder.Utils.MyUtils;
import com.perusahaan.fullname.odcfinder.databinding.ActivityProfileBinding;
import com.perusahaan.fullname.odcfinder.model.OdcModel;
import com.perusahaan.fullname.odcfinder.model.UserModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class ProfileActivity extends AppCompatActivity {

    private UserModel user;
    private EditText txtName;
    private FloatingActionButton floatingActionButton;
    private ImageView imgProfile;

    private boolean editMode;

    private final int CHOOSE_IMAGE = 666;

    private final String KEYWORD_IMAGE_PROFILE = "imgProfile";

    private SharedPreferences prefs;

    private final String UPDATE_PROFILE_URL = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem menuSave = menu.findItem(R.id.toolbar_save);
        menuSave.setVisible(editMode);

        MenuItem menuEdit = menu.findItem(R.id.toolbar_edit);
        menuEdit.setVisible(!editMode);

        return true;
//        return super.onPrepareOptionsMenu(menu);
    }

    private void toggleEditButton() {
        txtName.setEnabled(!txtName.isEnabled());

        if(txtName.isEnabled()) {
            txtName.requestFocus();
            floatingActionButton.setVisibility(View.VISIBLE);
        } else {
            txtName.clearFocus();
            floatingActionButton.setVisibility(View.GONE);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.showSoftInput(txtName, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.swipe_right, R.anim.swipe_right_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_edit:
                toggleEditButton();
                editMode = true;
                invalidateOptionsMenu();
                return true;

            case R.id.toolbar_save:
                saveEdit();
                toggleEditButton();
                editMode = false;
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);

        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        prefs = this.getSharedPreferences("com.perusahaan.fullname.odcfinder", Context.MODE_PRIVATE);

        if(user == null) {
            final String username = prefs.getString(Constant.PREF_USERNAME, "-");
            final String nama = prefs.getString(Constant.PREF_NAME, "-");
            final String profile = prefs.getString(Constant.PREF_PROFILE, "-");
            final String level = prefs.getString(Constant.PREF_LEVEL, "-");

            user = new UserModel(null, username, nama, null, profile, level, null);
            binding.setUserModel(user);
        }

        Toolbar toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        txtName = findViewById(R.id.txtProfileName);
        imgProfile = findViewById(R.id.imgProfile);

        loadImage();

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == CHOOSE_IMAGE) {

                final Uri uri = data.getData();
                Picasso.get()
                        .load(uri)
                        .resize(1366, 768)
                        .into(imgProfile);
            }
        }
    }

    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void loadImage() {
        try {
            final FileInputStream fileInputStream = getApplicationContext().openFileInput("profile.jpg");
            if(fileInputStream != null) {
                final Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                imgProfile.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String savedImage = getPreferences(MODE_PRIVATE).getString(KEYWORD_IMAGE_PROFILE, "");
//        if(!savedImage.equals("")) {
//            final Bitmap bitmap = decodeToBase64(savedImage);
//            imgProfile.setImageBitmap(bitmap);
//        }
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }


    private void saveEdit() {
        // save to shared preferences
        try {
            FileOutputStream outputStream = getApplicationContext().openFileOutput("profile.jpg", Context.MODE_PRIVATE);

            BitmapDrawable drawable = (BitmapDrawable) imgProfile.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
//            prefs.edit().putString(Constant.PREF_PROFILE, "profile.jpg").apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // save to database

    }
}
