package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartscheduling.Adapter.MessageAdapter;
import com.example.smartscheduling.ModelClass.MessageModelClass;
import com.example.smartscheduling.Utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageFriendActivity extends AppCompatActivity {
    String uid="",ip="",nam="",url="",fid="",type="",s_id="",r_id="",sender="",recipient="",message="",url1="";
    ListView listt;
    EditText emsg;
    private RecyclerView RvItem;
    MessageAdapter adapter=null;
    private RecyclerView.LayoutManager mLayoutManager;
    ScrollView sc;
    Handler m_Handler;
    Runnable mRunnable;
    ArrayList<MessageModelClass> list;
    ImageView img;
    ArrayList<HashMap<String, String>> pdtlist;
    GlobalPreference mGlobalPreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_friend);

        mGlobalPreference= new GlobalPreference(getApplicationContext());
        ip=mGlobalPreference.RetriveIP();
        uid=mGlobalPreference.RetriveUID();

        sc=findViewById(R.id.scroll);
        fid=getIntent().getStringExtra("id");

        url = "http://" + ip +"/SmartScheduling/API/getmsg.php";
        url1 = "http://" + ip +"/SmartScheduling/API/sendmsg.php";

        emsg=(EditText) findViewById(R.id.edit_text_out);
        //  img=(ImageView) findViewById(R.id.imgsnd);

        Button bimg=(Button) findViewById(R.id.button_send);
        bimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emsg.getText().toString().isEmpty()){

                }
                else{
                    SendMsg();
                }


            }
        });

        pdtlist=new ArrayList<HashMap<String,String>>();
        // getTime();
        sc.postDelayed(new Runnable() {
            @Override
            public void run() {
                sc.fullScroll(ScrollView.FOCUS_UP);
            }
        },1000);

        m_Handler = new Handler();
        mRunnable = new Runnable(){
            @Override
            public void run() {

                getMsg();

                m_Handler.postDelayed(mRunnable, 5000);// move this inside the run method
            }
        };
        mRunnable.run();

    }

    public  void showlist(String s) {

        try {
            list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                nam = jsonObject.getString("name");
                s_id = jsonObject.getString("sender_id");
                r_id = jsonObject.getString("recipient_id");
                message = jsonObject.getString("message");

                MessageModelClass item = new MessageModelClass(message,nam,Integer.parseInt(uid),Integer.parseInt(s_id),Integer.parseInt(r_id));


                /*item.setName(nam);
                item.setSid(Integer.parseInt(s_id));
                item.setUid(Integer.parseInt(uid));
                item.setRid(Integer.parseInt(r_id));
                item.setMsg(message);*/


                list.add(item);


                RvItem = (RecyclerView) findViewById(R.id.recycler_msg);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                RvItem.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mLayoutManager.scrollToPosition(list.size() - 1);
                RvItem.setLayoutManager(mLayoutManager);

                adapter = new MessageAdapter(MessageFriendActivity.this, list);

                RvItem.setAdapter(adapter);

            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        m_Handler.removeCallbacks(mRunnable);
    }

    public void getMsg() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                if(response.equals("failed")){
                    // Toast.makeText(BustimeActivity.this, "NO BUS AVAILABLE", Toast.LENGTH_SHORT).show();
                }
                else{
                    // pdtlist.clear();
                    showlist(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("res", "onErrorResponse: "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid",uid);
                params.put("rid",fid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MessageFriendActivity.this);
        requestQueue.add(stringRequest);
    }

    public void SendMsg() {

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                emsg.setText("");
                if(response.equals("failed")){
                    // Toast.makeText(BustimeActivity.this, "NO BUS AVAILABLE", Toast.LENGTH_SHORT).show();
                }
                else{
                    pdtlist.clear();
                    getMsg();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("res", "onErrorResponse:  "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sid",uid);
                params.put("msg",emsg.getText().toString());
                params.put("rid",fid);
                return params;
            }

        };

        RequestQueue requestQueue1 = Volley.newRequestQueue(MessageFriendActivity.this);
        requestQueue1.add(stringRequest1);
    }
}
