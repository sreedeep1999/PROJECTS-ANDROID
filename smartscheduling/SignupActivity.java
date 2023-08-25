package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartscheduling.Utils.GlobalPreference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    CircleImageView userIV;
    EditText userNameET, userPhoneNumberET, userEmailET, userPasswordET;
    TextView submitButtonTV;
    private String ip;
    private int PICK_IMAGE_REQUEST = 100;
    private Bitmap bitmap;
    private String image;
    private boolean iset = false;
    private boolean val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        GlobalPreference globalPreference = new GlobalPreference(getApplicationContext());
        ip = globalPreference.RetriveIP();

        init();

        submitButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(iset)
                {
                    image = getStringImage(bitmap);
                    check();
                }
                else
                    Toast.makeText(SignupActivity.this, "Please Select a image", Toast.LENGTH_SHORT).show();
            }
        });

        userIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    private void check() {
        if (userNameET.getText().toString().equals("")) {
            userNameET.setError("Please Enter name");
        }
        else if (userPhoneNumberET.getText().equals("") || userPhoneNumberET.getText().length() > 10 || userPhoneNumberET.getText().length() < 10) {
            userPhoneNumberET.setError("Invalid Phone number ");
        }
        else if (userEmailET.getText().toString().equals("")) {
            userEmailET.setError("Please Enter Email");
        }
        else if (userPasswordET.getText().equals("") || userPasswordET.getText().length() < 5) {
            userPasswordET.setError("Password is Empty or It Does not contain 5 letters");
        }
        else if (userEmailET.getText().length()>0) {
            val =  validateEmail(userEmailET);
            if(val==true){
                signUp();
            }
            else{
                Toast.makeText(SignupActivity.this,"Please Check Your Email id",Toast.LENGTH_LONG).show();
            }
        }

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
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                userIV.setImageBitmap(bitmap);
                iset = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void signUp() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/SmartScheduling/API/register.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                if(response.contains("Failed")){
                    Toast.makeText(SignupActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error);
                Toast.makeText(SignupActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("name",userNameET.getText().toString());
                params.put("phone",userPhoneNumberET.getText().toString());
                params.put("email",userEmailET.getText().toString());
                params.put("password",userPasswordET.getText().toString());
                params.put("image",image);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
        requestQueue.add(stringRequest);

    }

    private void init() {

        userNameET = (EditText) findViewById(R.id.userNameTextView);
        userPhoneNumberET = (EditText) findViewById(R.id.userPhoneNumberTextView);
        userEmailET = (EditText) findViewById(R.id.userEmailTextView);
        userPasswordET = (EditText) findViewById(R.id.userPasswordTextView);
        submitButtonTV = (TextView) findViewById(R.id.submitButtonTextView);
        userIV = (CircleImageView) findViewById(R.id.userImageView);

    }

    private boolean validateEmail(EditText emailET) {
        String email = emailET.getText().toString();

        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // Toast.makeText(RegisterActivity.this,"Email Validated",Toast.LENGTH_SHORT).show();
            return true;
        }else {
            // Toast.makeText(RegisterActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;

        }
    }

}
