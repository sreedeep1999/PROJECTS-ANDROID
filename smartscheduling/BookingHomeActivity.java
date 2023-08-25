package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartscheduling.Utils.GPS_service;
import com.example.smartscheduling.Utils.GlobalPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookingHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BookingHomeActivity";

//    private Button audioFAB;
    private AutoCompleteTextView placeET;
    private EditText checkInET, checkOutET;
    private Button searchBT;
    private CardView myBookingsCV, bookingHistCV, profileCV, friendsCV;
    private CardView busCV;

    private List<String> places;
    private int f = 0;
    private String loadPlacesUrl;
    private String searchHotelUrl;

    // Get Current Date
    Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);

    Intent activityIntent;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private TextToSpeech t1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }

        GlobalPreference globalPreference = new GlobalPreference(this);
        String ip = globalPreference.RetriveIP();

        loadPlacesUrl = "http://"+ip+"/SmartScheduling/API/getPlaces.php";
        searchHotelUrl = "http://"+ip+"/SmartScheduling/API/searchHotels.php";

        Intent in=new Intent(getApplicationContext(), GPS_service.class);
        startService(in);

        init();

        checkInET.setHint(mYear + "-" +(mMonth + 1)  + "-" + mDay);
        checkOutET.setHint(mYear + "-" +(mMonth + 1)  + "-" + mDay);

        loadPlaces();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    t1.setSpeechRate(0);
                }
            }
        });


        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
//                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null) {

                    String text = matches.get(0);
                    Log.d(TAG, "onResults:"+text);

                    if(text.contains("places"))
                    {
                        t1.speak("Finding Places Near You", TextToSpeech.QUEUE_FLUSH, null);
                        String uri = "geo:0,0?q=places+near+me";
                        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                    }
                    if(text.contains("hotels"))
                    {
                        t1.speak("Finding Hotels Near You", TextToSpeech.QUEUE_FLUSH, null);
                        String uri = "geo:0,0?q=Hotels+near+me";
                        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                    }
                    if(text.contains("restaurant"))
                    {
                        t1.speak("Finding restaurants Near You", TextToSpeech.QUEUE_FLUSH, null);
                        String uri = "geo:0,0?q=restaurant+near+me";
                        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                    }
                    if(text.contains("coffee"))
                    {
                        t1.speak("Finding coffee shops Near You", TextToSpeech.QUEUE_FLUSH, null);
                        String uri = "geo:0,0?q=coffee+near+me";
                        startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                    }

                    mSpeechRecognizer.stopListening();
                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        FloatingActionButton listeningButton = findViewById(R.id.audioFloatingButton);
        listeningButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        Log.d(TAG, "onTouch: "+"Press On The Screen To Start Listening");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        Log.d(TAG, "onTouch: Listening...");

                        break;
                }
                return false;
            }
        });

    }

    private void loadPlaces() {

        places = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loadPlacesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d(TAG, "onResponse: "+response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonSourceArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonSourceArray.length(); i++)
                    {
                        JSONObject sobject = jsonSourceArray.getJSONObject(i);
                        String place = sobject.getString("hotel_place");

                        if ( places.size() != 0)
                        {
                            for (int j = 0; j < places.size(); j++) {
                                if (places.get(j).equals(place))
                                    f = 1;
                            }
                            if( f != 1) {
                                places.add(place);
                                f = 0;
                            }
                        }
                        else
                            places.add(place);

                        f = 0;
                    }

                    ArrayAdapter<String> placeAdapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.select_dialog_item, places);
                    placeET.setThreshold(2);
                    placeET.setAdapter(placeAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void searchAvailableHotels() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, searchHotelUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Intent intent = new Intent(BookingHomeActivity.this,HotelListActivity.class);
                intent.putExtra("response",response);
                intent.putExtra("checkInTime",checkInET.getText().toString());
                intent.putExtra("checkOutTime",checkOutET.getText().toString());
                startActivity(intent);

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
                params.put("place",placeET.getText().toString());
                params.put("checkInTime",checkInET.getText().toString());
                params.put("checkOutTime",checkOutET.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void init() {
//        audioFAB = (Button) findViewById(R.id.audioFloatingButton);

        placeET = (AutoCompleteTextView) findViewById(R.id.placeEditText);
        placeET.setOnClickListener(this);

        checkInET = (EditText) findViewById(R.id.checkInEditText);
        checkInET.setOnClickListener(this);

        checkOutET = (EditText) findViewById(R.id.checkOutEditText);
        checkOutET.setOnClickListener(this);

        searchBT = (Button) findViewById(R.id.searchButton);
        searchBT.setOnClickListener(this);

        myBookingsCV = (CardView) findViewById(R.id.myBookingsCardView);
        myBookingsCV.setOnClickListener(this);

        bookingHistCV = (CardView) findViewById(R.id.bookingHistCardView);
        bookingHistCV.setOnClickListener(this);

        profileCV = (CardView) findViewById(R.id.profileCardView);
        profileCV.setOnClickListener(this);

        friendsCV = (CardView) findViewById(R.id.friendsCV);
        friendsCV.setOnClickListener(this);

        busCV = findViewById(R.id.busTimingsCardView);
        busCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingHomeActivity.this,BusTimingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.checkInEditText:

                DatePickerDialog checkInDatePickerDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        checkInET.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                checkInDatePickerDialog.show();

                break;
            case R.id.checkOutEditText:

                DatePickerDialog checkOutDatePickerDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        checkOutET.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                checkOutDatePickerDialog.show();

                break;
            case R.id.searchButton:
                searchAvailableHotels();
                break;
            case R.id.myBookingsCardView:
                activityIntent = new Intent(this,BookingsActivity.class);
                startActivity(activityIntent);
                break;
            case R.id.bookingHistCardView:
                activityIntent = new Intent(this,BookingHistoryActivity.class);
                startActivity(activityIntent);
                break;
            case R.id.friendsCV:
                activityIntent = new Intent(this,FindFriendActivity.class);
                startActivity(activityIntent);
                break;
            case R.id.profileCardView:
                activityIntent = new Intent(this,ProfileActivity.class);
                startActivity(activityIntent);
                break;
        }
    }

}
