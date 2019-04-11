package com.perusahaan.fullname.odcfinder.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perusahaan.fullname.odcfinder.ProfileActivity;
import com.perusahaan.fullname.odcfinder.R;
import com.perusahaan.fullname.odcfinder.Utils.Constant;
import com.perusahaan.fullname.odcfinder.Utils.MyUtils;
import com.perusahaan.fullname.odcfinder.databinding.ActivityProfileBinding;
import com.perusahaan.fullname.odcfinder.databinding.FragmentProfileBinding;
import com.perusahaan.fullname.odcfinder.model.UserModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private UserModel user;
    private SharedPreferences prefs;

    private final String UPDATE_PROFILE_URL = "http://khotibul.herokuapp.com/users/";

    private boolean editMode = false;
    private FloatingActionButton floatingActionButton;
    private EditText txtName, txtNik, txtNoHp, txtUsername;
    private final int CHOOSE_IMAGE = 666;
    private ImageView imgProfile;

    public ProfileFragment() {
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem menuSave = menu.findItem(R.id.toolbar_save);
        menuSave.setVisible(editMode);

        MenuItem menuEdit = menu.findItem(R.id.toolbar_edit);
        menuEdit.setVisible(!editMode);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        getActivity().getMenuInflater().inflate(R.menu.toolbar_menu_save, menu);
        getActivity().getMenuInflater().inflate(R.menu.menu_profile, menu);

        final MenuItem menuSave = menu.findItem(R.id.toolbar_save);
        menuSave.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                saveEdit();
                updateData(getContext());
                editMode = false;

                Toast.makeText(getContext(), "Save !", Toast.LENGTH_SHORT).show();
                editMode = false;
                getActivity().invalidateOptionsMenu();
                floatingActionButton.setVisibility(View.GONE);
                return true;
            }
        });

        final MenuItem menuEdit = menu.findItem(R.id.toolbar_edit);
        menuEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getContext(), "Edit !", Toast.LENGTH_SHORT).show();
                editMode = true;
                getActivity().invalidateOptionsMenu();
                floatingActionButton.setVisibility(View.VISIBLE);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        FragmentProfileBinding binding = DataBindingUtil.setContentView(container, R.layout.fragment_profile);

        final FragmentProfileBinding viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        final View view = viewDataBinding.getRoot();
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Your Profile");
        }

        txtName = view.findViewById(R.id.txtProfileName);
        txtNik = view.findViewById(R.id.txtNik);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtNoHp = view.findViewById(R.id.txtNoHp);
        imgProfile = view.findViewById(R.id.imgProfile);

        prefs = getActivity().getSharedPreferences("com.perusahaan.fullname.odcfinder", Context.MODE_PRIVATE);

        if(user == null) {
            final Integer id = prefs.getInt(Constant.PREF_ID, 0);
            final String username = prefs.getString(Constant.PREF_USERNAME, "-");
            final String nama = prefs.getString(Constant.PREF_NAME, "-");
            final String nik = prefs.getString(Constant.PREF_NIK, "-");
            final String noHp = prefs.getString(Constant.PREF_NO_HP, "-");
            final String profile = prefs.getString(Constant.PREF_PROFILE, "-");
            final String level = prefs.getString(Constant.PREF_LEVEL, "-");

            user = new UserModel(id, username, nama, nik, noHp, level, profile);
        }

        viewDataBinding.setUserModel(user);

        loadImage();

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        return view;
    }

    private void loadImage() {
        try {
            final FileInputStream fileInputStream = getContext().openFileInput("profile.jpg");
            if(fileInputStream != null) {
                final Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                imgProfile.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveEdit() {
        // save to shared preferences
        try {
            FileOutputStream outputStream = getContext().openFileOutput("profile.jpg", Context.MODE_PRIVATE);

            BitmapDrawable drawable = (BitmapDrawable) imgProfile.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // set to user model
            final String profileBase64 = encodeToBase64(bitmap);
            user.setPhoto(profileBase64);
            user.setNoHp(txtNoHp.getText().toString());
            user.setNama(txtName.getText().toString());
//            prefs.edit().putString(Constant.PREF_PROFILE, "profile.jpg").apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        this.editMode = false;
        super.onResume();
    }

    private void updateData(final Context context) {
        try {
            MyUtils.showSimpleProgressDialog(getContext(), "Saving data...", "Harap bersabar...");

            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", user.getId());
//            jsonBody.put("username", user.getUsername());
            jsonBody.put("photo", user.getPhoto());
//            jsonBody.put("level", user.getLevel());
            jsonBody.put("nama_lengkap", user.getNama());
//            jsonBody.put("nik", user.getNik());
            jsonBody.put("no_hp", user.getNoHp());

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, UPDATE_PROFILE_URL + user.getId(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            MyUtils.removeSimpleProgressDialog();
                            if (response == null) {
                                Toast.makeText(context, "Update data Failed.", Toast.LENGTH_SHORT).show();
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

                                    user = new UserModel(id, username, nama, nik, no_hp, level, photo);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MyUtils.removeSimpleProgressDialog();
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
