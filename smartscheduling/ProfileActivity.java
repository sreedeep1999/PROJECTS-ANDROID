package com.example.smartscheduling;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.smartscheduling.Utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private EditText userNameET, userEmailET, userMobileTV, userPasswordET;
    private TextView submitButtonTV;
    private CircleImageView userIV;
    private String url;
    private String uid;
    private String ip;

    private int PICK_IMAGE_REQUEST = 1;
    boolean IMAGE_CHANGE = false;
    private String image = "";
    private Bitmap bitmap;

    private GlobalPreference globalPreference;

    TextView logoutTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final GlobalPreference globalPreference = new GlobalPreference(getApplicationContext());
        uid = globalPreference.RetriveUID();
        ip = globalPreference.RetriveIP();
        url = "http://"+ ip + "/SmartScheduling/API/userDetails.php";

        init();

        loadData();

        submitButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        userIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new AlertDialog.Builder(ProfileActivity.this)
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                globalPreference.addLoginStatus(false);

                                Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                userIV.setImageBitmap(bitmap);
                IMAGE_CHANGE = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateData() {

        Log.d(TAG, "updateData: "+IMAGE_CHANGE);

        if(IMAGE_CHANGE) {
            image = getStringImage(bitmap);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+ "/SmartScheduling/API/updateProfile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("name",userNameET.getText().toString());
                params.put("mobile",userMobileTV.getText().toString());
                params.put("email",userEmailET.getText().toString());
                params.put("password",userPasswordET.getText().toString());
                params.put("uid",uid);
                if(IMAGE_CHANGE)
                    params.put("image",image);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void loadData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: "+response);
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");

                    JSONObject object = jsonArray.getJSONObject(0);
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String phone_no = object.getString("phone");
                    String password = object.getString("password");
                    String image = object.getString("image");

                    userNameET.setText(name);
                    userEmailET.setText(email);
                    userPasswordET.setText(password);
                    userMobileTV.setText(phone_no);

                    Log.d(TAG, "onResponse: "+"http://"+ ip + "/SmartScheduling/admin/tbl_customer/uploads/" +image);

                    Glide.with(getApplicationContext())
                            .load("http://"+ ip + "/SmartScheduling/admin/tbl_customer/uploads/" +image)
                            .into(userIV);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userId",uid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void init() {
        userNameET = (EditText) findViewById(R.id.userNameEditText);
        userEmailET = (EditText) findViewById(R.id.userEmailEditText);
        userPasswordET = (EditText) findViewById(R.id.userPasswordEditText);
        userMobileTV = (EditText) findViewById(R.id.userPhoneNumberEditText);
        submitButtonTV = (TextView) findViewById(R.id.submitButtonTextView);
        userIV = (CircleImageView) findViewById(R.id.userImageView);
        logoutTV = findViewById(R.id.logoutButtonTextView);
    }

}
