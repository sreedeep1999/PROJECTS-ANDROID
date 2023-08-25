package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartscheduling.Adapter.FriendListAdapter;
import com.example.smartscheduling.ModelClass.friendListModelClass;
import com.example.smartscheduling.Utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFriendActivity extends AppCompatActivity {

    private static final String TAG = "SearchFriendActivity";

    private RecyclerView nearByFriendsListRV;
    private List<friendListModelClass> friendList;
    private String uid;
    private String ip;
    private FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        GlobalPreference globalPreference = new GlobalPreference(this);
        uid = globalPreference.RetriveUID();
        ip = globalPreference.RetriveIP();

        nearByFriendsListRV = (RecyclerView) findViewById(R.id.nearByFriendRecyclerView);
        nearByFriendsListRV.setLayoutManager(new LinearLayoutManager(this));
        nearByFriendsListRV.setNestedScrollingEnabled(true);
        nearByFriendsListRV.setItemAnimator(new DefaultItemAnimator());

        searchFriends();
    }

    private void searchFriends() {

        friendList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/SmartScheduling/API/findNearByFriends.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                if(response.contains("Failed"))
                    Toast.makeText(SearchFriendActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                else {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0 ; i < jsonArray.length() ; i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String name = object.getString("name");
                            String image = object.getString("image");

                            friendList.add(new friendListModelClass(id,name,image));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter = new FriendListAdapter(friendList,getApplicationContext());
                    nearByFriendsListRV.setAdapter(adapter);

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
                params.put("uid",uid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SearchFriendActivity.this);
        requestQueue.add(stringRequest);

    }

}
