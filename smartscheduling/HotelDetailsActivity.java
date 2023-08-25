package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.smartscheduling.Utils.Constanturls;
import com.example.smartscheduling.Utils.GlobalPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HotelDetailsActivity extends AppCompatActivity {

    private static final String TAG = "HotelDetailsActivity";

    private ImageView hotelIV;
    private Button hotelRoomsBT;
    private TextView hotelNameTV, hotelAddressTV, hotelPlaceTV, hotelDescTV;
    private String url, hotelId, checkInTime, checkOutTime;
    private String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_details);

        init();

        Intent intent = getIntent();
        hotelId = intent.getStringExtra("hotelId");
        checkInTime = intent.getStringExtra("checkInTime");
        checkOutTime = intent.getStringExtra("checkOutTime");

        Log.d(TAG, "onCreate: "+hotelId);

        GlobalPreference globalPreference = new GlobalPreference(this);
        String ip = globalPreference.RetriveIP();

        Constanturls imurl = new Constanturls(this);
        imageurl = imurl.getImageUrl();

        url = "http://"+ip+"/SmartScheduling/API/getHotelDetails.php";

        getHotelDetails();

        hotelRoomsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelDetailsActivity.this, HotelRoomsListActivity.class);
                intent.putExtra("hotelId",hotelId);
                intent.putExtra("checkInTime",checkInTime);
                intent.putExtra("checkOutTime",checkOutTime);
                startActivity(intent);
            }
        });

        final ImageButton show = (ImageButton) findViewById(R.id.show);
        final ImageButton hide = (ImageButton) findViewById(R.id.hide);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Show button");
                show.setVisibility(View.INVISIBLE);
                hide.setVisibility(View.VISIBLE);
                hotelDescTV.setMaxLines(Integer.MAX_VALUE);

            }
        });

        hide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Hide button");
                hide.setVisibility(View.INVISIBLE);
                show.setVisibility(View.VISIBLE);
                hotelDescTV.setMaxLines(5);

            }
        });

    }

    private void getHotelDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                if(response.contains("Failed"))
                    Toast.makeText(HotelDetailsActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                else {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String hotel_name = jsonObject.getString("hotel_name");
                        String hotel_desc = jsonObject.getString("hotel_desc");
                        String hotel_address = jsonObject.getString("hotel_address");
                        String hotel_place = jsonObject.getString("hotel_place");
                        String hotel_image = jsonObject.getString("hotel_image");

                        hotelNameTV.setText(hotel_name);
                        hotelDescTV.setText(hotel_desc);
                        hotelAddressTV.setText(hotel_address);
                        hotelPlaceTV.setText(hotel_place);

                        Glide.with(HotelDetailsActivity.this)
                                .load(imageurl+hotel_image)
                                .into(hotelIV);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("hotelId",hotelId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HotelDetailsActivity.this);
        requestQueue.add(stringRequest);

    }

    private void init() {
        hotelIV = (ImageView) findViewById(R.id.hotelImageView);
        hotelNameTV = (TextView) findViewById(R.id.hotelNameTextView);
        hotelAddressTV = (TextView) findViewById(R.id.hotelAddressTextView);
        hotelPlaceTV = (TextView) findViewById(R.id.hotelPlaceTextView);
        hotelDescTV = (TextView) findViewById(R.id.hotelDescTextView);
        hotelRoomsBT = (Button) findViewById(R.id.hotelRoomsButton);
    }
}
