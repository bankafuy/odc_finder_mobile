package com.perusahaan.fullname.odcfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
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

import com.perusahaan.fullname.odcfinder.databinding.ActivityProfileBinding;
import com.perusahaan.fullname.odcfinder.model.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private EditText txtName;
    private FloatingActionButton floatingActionButton;
    private ImageView imgProfile;

    private final int CHOOSE_IMAGE = 666;

    private final String KEYWORD_IMAGE_PROFILE = "imgProfile";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_edit:
                toggleEditButton();
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

        UserModel user = new UserModel(1, "admin", "admin", "khotibul", "ADMIN", "");

        binding.setUserModel(user);

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

                try  {
                    InputStream stream = getContentResolver().openInputStream(data.getData());

                    Bitmap realImage = BitmapFactory.decodeStream(stream);

                    Bitmap resizedBitmap = getResizedBitmap(realImage, 600, 1200);

                    imgProfile.setImageBitmap(resizedBitmap);

                    getPreferences(MODE_PRIVATE).edit()
                            .putString(KEYWORD_IMAGE_PROFILE, encodeToBase64(resizedBitmap))
                            .apply();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void loadImage() {
        String savedImage = getPreferences(MODE_PRIVATE).getString(KEYWORD_IMAGE_PROFILE, "");
        if(!savedImage.equals("")) {
            final Bitmap bitmap = decodeToBase64(savedImage);
            imgProfile.setImageBitmap(bitmap);
        }
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
}
