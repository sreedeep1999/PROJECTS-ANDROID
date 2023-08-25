package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartscheduling.Adapter.bookingListAdapter;
import com.example.smartscheduling.ModelClass.BookingListModelClass;
import com.example.smartscheduling.Utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingsActivity extends AppCompatActivity {


    private static final String TAG = "BookingHistoryActivity";

    private RecyclerView bookingListRV;
    private String ip;
    private String uid;
    private List<BookingListModelClass> bookingList;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);


        GlobalPreference globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();
        uid = globalPreference.RetriveUID();

        bookingListRV = (RecyclerView) findViewById(R.id.bookingsRecyclerView);
        bookingListRV.setLayoutManager(new LinearLayoutManager(this));
        bookingListRV.setItemAnimator(new DefaultItemAnimator());

        loadBookingsList();
    }

    private void loadBookingsList() {

        bookingList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/SmartScheduling/API/bookingsCurrent.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("data");
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String hotel_name = jsonObject.getString("hotel_name");
                        String hotel_place = jsonObject.getString("hotel_place");
                        String hotel_address = jsonObject.getString("hotel_address");
                        String hotel_latitude = jsonObject.getString("hotel_latitude");
                        String hotel_longitude = jsonObject.getString("hotel_longitude");
                        String room_type = jsonObject.getString("room_type");
                        String hotel_image = jsonObject.getString("hotel_image");
                        String rooms = jsonObject.getString("rooms");
                        String days = jsonObject.getString("days");
                        String from_date = jsonObject.getString("from_date");
                        String price = jsonObject.getString("price");

                        bookingList.add(new BookingListModelClass(id,hotel_name,hotel_place,hotel_address,hotel_latitude,hotel_longitude,room_type,rooms,days,from_date,price,hotel_image));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                bookingListAdapter adapter = new bookingListAdapter(BookingsActivity.this,bookingList);
                bookingListRV.setAdapter(adapter);

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
                params.put("uid",uid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(BookingsActivity.this);
        requestQueue.add(stringRequest);
    }
}
