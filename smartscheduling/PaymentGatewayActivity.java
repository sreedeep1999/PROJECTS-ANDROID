package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.smartscheduling.Utils.GlobalPreference;

public class PaymentGatewayActivity extends AppCompatActivity {

    private WebView paymentGatewayWV;
    private static final String TAG = "PaymentGatewayActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String hotel_id = extras.getString("hotel_id");
        String room_type = extras.getString("room_type");
        String rooms = extras.getString("rooms");
        String price = extras.getString("price");
        String days = extras.getString("days");
        String checkInTime = extras.getString("checkInTime");
        String checkOutTime = extras.getString("checkOutTime");

        Log.d(TAG, "onCreate: "+hotel_id+"\n"+room_type+"\n"+rooms+"\n"+room_type+"\n"+price+"\n"+days+"\n"+checkInTime+"\n"+checkOutTime);


        GlobalPreference globalPreference = new GlobalPreference(this);
        String ip = globalPreference.RetriveIP();
        String uid = globalPreference.RetriveUID();
    //    String uid = "1";

        String passValuesUrl = "?uid="+uid+ "&amt="+price+ "&hotelId="+hotel_id  +"&room_type="+room_type  +"&rooms="+rooms  +"&days="+days +"&checkInTime="+checkInTime +"&checkOutTime="+checkOutTime;

        paymentGatewayWV = findViewById(R.id.paymentgatewayWebView);
        paymentGatewayWV.clearCache(true);
        paymentGatewayWV.clearHistory();
        paymentGatewayWV.setInitialScale(150);
        paymentGatewayWV.getSettings().setJavaScriptEnabled(true);
        paymentGatewayWV.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        paymentGatewayWV.loadUrl("http://"+ip+"/SmartScheduling/API/payment/sec.php" +passValuesUrl);

        paymentGatewayWV.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d("WebView", "your current url when webpage loading.." + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "your current url when webpage loading.. finish" + url);

                if(url.contains("complete.php"))
                {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(PaymentGatewayActivity.this,BookingHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1000);
                }

                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("when you click on any interlink on webview that time you got url :-" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });


    }
}
