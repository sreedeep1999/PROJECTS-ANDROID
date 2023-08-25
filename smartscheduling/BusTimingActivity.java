package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartscheduling.Adapter.BusTimingAdapter;
import com.example.smartscheduling.Adapter.FriendListAdapter;
import com.example.smartscheduling.ModelClass.BusTimingModelClass;
import com.example.smartscheduling.ModelClass.friendListModelClass;
import com.example.smartscheduling.Utils.GlobalPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusTimingActivity extends AppCompatActivity {

    private static String TAG ="ChallengeActivity";

    RecyclerView timingRV;
    ArrayList<BusTimingModelClass> list;
    private GlobalPreference globalPreference;
    private String ip;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_timing);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();



        timingRV = findViewById(R.id.busTimingsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        timingRV.setLayoutManager(layoutManager);

        getbusTimings();
    }

    private void getbusTimings() {
        list = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+ ip +"/SmartScheduling/API/busTime.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Log.d(TAG, "onResponse: "+response);

                if (response.equals("failed")){
                    Toast.makeText(BusTimingActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                }
                else{
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0; i< jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String name = object.getString("name");
                            String route = object.getString("route");
                            String timings = object.getString("timings");

                            list.add(new BusTimingModelClass(id,name,route,timings));

                        }

                        BusTimingAdapter adapter = new BusTimingAdapter(list,BusTimingActivity.this);
                        timingRV.setAdapter(adapter);

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d(TAG, "onErrorResponse: "+error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}