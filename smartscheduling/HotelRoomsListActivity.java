package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartscheduling.Adapter.HotelRoomListAdapter;
import com.example.smartscheduling.ModelClass.HotelRoomsListModelClass;
import com.example.smartscheduling.Utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HotelRoomsListActivity extends AppCompatActivity {

    private static final String TAG = "HotelRoomsListActivity";

    RecyclerView hotelRoomsListRV;
    private String hotelId, checkInTime, checkOutTime;
    private String url;
    private List<HotelRoomsListModelClass> hotelRoomList;
    private String days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_rooms_list);

        Intent intent = getIntent();
        hotelId = intent.getStringExtra("hotelId");
        checkInTime = intent.getStringExtra("checkInTime");
        checkOutTime = intent.getStringExtra("checkOutTime");

        GlobalPreference globalPreference = new GlobalPreference(this);
        String ip = globalPreference.RetriveIP();
        url = "http://"+ip+"/SmartScheduling/API/searchHotelsRooms.php";

        hotelRoomsListRV = (RecyclerView) findViewById(R.id.hotelRoomsListRecyclerView);
        hotelRoomsListRV.setLayoutManager(new LinearLayoutManager(this));
        hotelRoomsListRV.setItemAnimator(new DefaultItemAnimator());

        loadHotelRoomList();

        days = String.valueOf(Daybetween(checkInTime,checkOutTime));
        Log.d(TAG, hotelId+"   onCreate checkInTime:"+checkInTime+" - from checkOutTime:"+checkOutTime+" = "+days+" days");
    }



    public long Daybetween(String date1,String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long diff = 0;
        try {
            Date fromdate = sdf.parse(date1);
            Date todate = sdf.parse(date2);
            diff = (todate.getTime() - fromdate.getTime())  / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (diff);
    }



    private void loadHotelRoomList() {

        hotelRoomList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray  jsonArray = jsonObject.getJSONArray("room");
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String hotel_id = object.getString("hotel_id");
                        String room_type = object.getString("room_type");
                        String heads = object.getString("heads");
                        String bed = object.getString("bed");
                        String size = object.getString("size");
                        String price = object.getString("price");
                        String Available  = object.getString("Available");

                        hotelRoomList.add(new HotelRoomsListModelClass(id, hotel_id, room_type, heads, bed, size, price, Available, days));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HotelRoomListAdapter adapter = new HotelRoomListAdapter(HotelRoomsListActivity.this,hotelRoomList,checkInTime,checkOutTime);
                hotelRoomsListRV.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("hotelId",hotelId);
                params.put("checkInTime",checkInTime);
                params.put("checkOutTime",checkOutTime);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
