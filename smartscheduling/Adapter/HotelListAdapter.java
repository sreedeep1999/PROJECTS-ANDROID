package com.example.smartscheduling.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.smartscheduling.HotelDetailsActivity;
import com.example.smartscheduling.ModelClass.HotelListModelClass;
import com.example.smartscheduling.R;
import com.example.smartscheduling.Utils.GlobalPreference;
import java.util.List;

public class HotelListAdapter extends RecyclerView.Adapter<HotelListAdapter.MyViewHolder>  {

    private static final String TAG = "Adapter";

    private String checkInTime, checkOutTime;
    private final String ip;
    private Context mtx;
    private List<HotelListModelClass> hotelList;

    public HotelListAdapter(Context mtx, List<HotelListModelClass> hotelList, String checkInTime, String checkOutTime) {
        this.mtx = mtx;
        this.hotelList = hotelList;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        
        GlobalPreference globalPreference = new GlobalPreference(mtx);
        ip = globalPreference.RetriveIP();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_hotel_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final HotelListModelClass lists = hotelList.get(position);
        holder.hotelNameTV.setText(lists.getHotel_name());
        holder.hotelPlaceTV.setText(lists.getHotel_place());
        Glide.with(mtx)
                .load("http://"+ip+"/SmartScheduling/admin/tbl_hotel/uploads/"+lists.getHotel_image())
                .into(holder.hotelIV);

        holder.hotelListLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mtx, HotelDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("hotelId",lists.getId());
                intent.putExtra("checkInTime",checkInTime);
                intent.putExtra("checkOutTime",checkOutTime);
                mtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView hotelNameTV, hotelPlaceTV;
        private ImageView hotelIV;
        private LinearLayout hotelListLL;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelNameTV = (TextView) itemView.findViewById(R.id.hotelNameTextView);
            hotelPlaceTV = (TextView) itemView.findViewById(R.id.hotelPlaceTextView);
            hotelIV = (ImageView) itemView.findViewById(R.id.hotelImageView);
            hotelListLL = (LinearLayout) itemView.findViewById(R.id.hotelListLinearLayout);
        }
    }


}
