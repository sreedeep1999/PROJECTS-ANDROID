package com.example.smartscheduling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartscheduling.Utils.GlobalPreference;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";

    private RadioButton cardRadioButton;
    private TextView paymentTVButton;
    EditText cardnoEditText,cvvEditText;
    TextView amountEditText;
    LinearLayout cardDetailsLL;

    private String uid;
    private String ip;

    List<String> month = new ArrayList<>();
    List<String> year = new ArrayList<>();
    private Spinner MMspin;
    private Spinner YYspin;
    private Bundle extras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        extras = intent.getExtras();
        String price = extras.getString("price");

        month.add("MM");
        for (int i = 1 ; i <= 31 ; i++)
        {
            month.add(String.valueOf(i));
        }

        year.add("YY");
        for(int i = 19 ; i<=30 ; i++ )
            year.add(String.valueOf(i));

        MMspin = (Spinner) findViewById(R.id.mmspinner);
        ArrayAdapter MM = new ArrayAdapter(this,android.R.layout.simple_spinner_item,month);
        MM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MMspin.setAdapter(MM);

        YYspin = (Spinner) findViewById(R.id.yyspinner);
        ArrayAdapter YY = new ArrayAdapter(this,android.R.layout.simple_spinner_item,year);
        YY.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        YYspin.setAdapter(YY);

        GlobalPreference globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();
        uid = globalPreference.RetriveUID();

        init();
/*
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedPrice = formatter.format(price);*/
        amountEditText.setText("Rs. "+price);

        cardRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardRadioButton.isChecked()) {
                    cardDetailsLL.setVisibility(View.VISIBLE);
                }
            }
        });

        paymentTVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!cardRadioButton.isChecked())
                {
                    Toast.makeText(PaymentActivity.this, "Please select a payment method ", Toast.LENGTH_SHORT).show();
                }
                else if(cardRadioButton.isChecked() && cardnoEditText.getText().toString().equals("") ||
                        cardRadioButton.isChecked() && cvvEditText.getText().toString().equals("") ||
                        cardRadioButton.isChecked() && MMspin.getSelectedItem().toString().equals("MM") ||
                        cardRadioButton.isChecked() && YYspin.getSelectedItem().toString().equals("YY"))
                {
                    Toast.makeText(PaymentActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    paynow();
                }

            }
        });

    }

    private void paynow() {

        Intent intent = new Intent(PaymentActivity.this, PaymentGatewayActivity.class);
        intent.putExtras(extras);
        startActivity(intent);

    }

    private void init() {
        cardRadioButton = findViewById(R.id.radioCard);
        paymentTVButton = findViewById(R.id.paymentTextViewButton);
        cardDetailsLL = findViewById(R.id.cardDetailsLinearLayout);
        cardnoEditText = findViewById(R.id.cardnoEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        amountEditText = findViewById(R.id.amountTextView);
    }

}
