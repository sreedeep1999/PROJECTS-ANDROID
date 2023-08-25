package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.smartscheduling.Adapter.HotelListAdapter;
import com.example.smartscheduling.ModelClass.HotelListModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HotelListActivity extends AppCompatActivity {

    private static final String TAG = "HotelListActivity";

    RecyclerView hotelRV;
    List<HotelListModelClass> hotelList = new ArrayList<>();
    private String checkInTime, checkOutTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);

        Intent intent = getIntent();
        String response = intent.getStringExtra("response");
        checkInTime = intent.getStringExtra("checkInTime");
        checkOutTime = intent.getStringExtra("checkOutTime");

        Log.d(TAG, "onCreate: "+response);

        hotelRV = (RecyclerView) findViewById(R.id.hotelRecyclerView);
        hotelRV.setLayoutManager(new LinearLayoutManager(this));
        hotelRV.setItemAnimator(new DefaultItemAnimator());

        loadList(response);

    }

    private void loadList(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for(int i=0; i < jsonArray.length(); i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("hotel_id");
                String hotel_name = object.getString("hotel_name");
                String hotel_place = object.getString("hotel_place");
                String hotel_image = object.getString("hotel_image");

                hotelList.add(new HotelListModelClass(id,hotel_name,hotel_place,hotel_image));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HotelListAdapter adapter = new HotelListAdapter(HotelListActivity.this,hotelList, checkInTime, checkOutTime);
        hotelRV.setAdapter(adapter);

    }
}
