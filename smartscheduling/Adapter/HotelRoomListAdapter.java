package com.example.smartscheduling.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartscheduling.LoginActivity;
import com.example.smartscheduling.MainActivity;
import com.example.smartscheduling.ModelClass.HotelRoomsListModelClass;
import com.example.smartscheduling.PaymentActivity;
import com.example.smartscheduling.R;
import com.example.smartscheduling.Utils.GlobalPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelRoomListAdapter extends RecyclerView.Adapter<HotelRoomListAdapter.MyViewHolder>  {

    private static final String TAG = "Adapter";

    private String checkInTime, checkOutTime;
    private final String ip;
    private Context mtx;
    private List<HotelRoomsListModelClass> hotelRoomsList;

    public HotelRoomListAdapter(Context mtx, List<HotelRoomsListModelClass> hotelRoomsList, String checkInTime, String checkOutTime) {
        this.mtx = mtx;
        this.hotelRoomsList = hotelRoomsList;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        GlobalPreference globalPreference = new GlobalPreference(mtx);
        ip = globalPreference.RetriveIP();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_hotel_room_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final HotelRoomsListModelClass lists = hotelRoomsList.get(position);

        final int price =  Integer.parseInt(lists.getDays()) * Integer.parseInt(lists.getPrice());

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedPrice = formatter.format(price);

        holder.roomTypeTV.setText(lists.getRoom_type());
        holder.roomHeadTV.setText(lists.getHeads());
        holder.roomBedTV.setText(lists.getBed());
        holder.roomSizeTV.setText(lists.getSize());
        holder.roomAvailableTV.setText(lists.getAvailable());
        holder.roombookDateTV.setText(lists.getDays());
        holder.roomPriceTV.setText(formattedPrice);
        holder.itemminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.numberTV.getText().toString());
                if(count > 1) {
                    count--;
                    holder.numberTV.setText(String.valueOf(count));
                }
            }
        });

        holder.itemplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.numberTV.getText().toString());
                int quantity = Integer.parseInt(lists.getAvailable());
                if(count < quantity) {
                    count++;
                    holder.numberTV.setText(String.valueOf(count));
                }
            }
        });

        holder.bookNowBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = Integer.parseInt(holder.numberTV.getText().toString());

                Bundle args = new Bundle();
                args.putString("hotel_id",lists.getHotel_id());
                args.putString("room_type",lists.getId());
                args.putString("rooms",holder.numberTV.getText().toString());
                args.putString("price", String.valueOf(price*count));
                args.putString("days",lists.getDays());
                args.putString("checkInTime",checkInTime);
                args.putString("checkOutTime",checkOutTime);

                Intent intent = new Intent(mtx,PaymentActivity.class);
                intent.putExtras(args);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mtx.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return hotelRoomsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView roomTypeTV, roomHeadTV,roomBedTV,roomSizeTV,roomAvailableTV,roombookDateTV,roomPriceTV,numberTV;
        private ImageView itemminus, itemplus;
        private Button bookNowBT;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            roomTypeTV = (TextView) itemView.findViewById(R.id.roomTypeTextView);
            roomHeadTV = (TextView) itemView.findViewById(R.id.roomHeadTextView);
            roomBedTV = (TextView) itemView.findViewById(R.id.roomBedTextView);
            roomSizeTV = (TextView) itemView.findViewById(R.id.roomSizeTextView);
            roomAvailableTV = (TextView) itemView.findViewById(R.id.roomAvailableTextView);
            roombookDateTV = (TextView) itemView.findViewById(R.id.roombookDateTextView);
            roomPriceTV = (TextView) itemView.findViewById(R.id.roomPriceTextView);
            numberTV = (TextView) itemView.findViewById(R.id.number);
            itemminus = (ImageView) itemView.findViewById(R.id.itemminus);
            itemplus = (ImageView) itemView.findViewById(R.id.itemplus);
            bookNowBT = (Button) itemView.findViewById(R.id.bookNowButton);
        }
    }


}
