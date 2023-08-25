package com.example.smartscheduling.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartscheduling.ModelClass.BookingListModelClass;
import com.example.smartscheduling.PaymentActivity;
import com.example.smartscheduling.R;
import com.example.smartscheduling.Utils.Constanturls;
import com.example.smartscheduling.Utils.GlobalPreference;

import java.text.DecimalFormat;
import java.util.List;

public class bookingHistoryListAdapter extends RecyclerView.Adapter<bookingHistoryListAdapter.MyViewHolder>  {

    private static final String TAG = "Adapter";

    private final String ip;
    private final String imageurl;
    private Context mtx;
    private List<BookingListModelClass> bookingList;

    public bookingHistoryListAdapter(Context mtx, List<BookingListModelClass> bookingList) {
        this.mtx = mtx;
        this.bookingList = bookingList;
        GlobalPreference globalPreference = new GlobalPreference(mtx);
        ip = globalPreference.RetriveIP();
        Constanturls constanturls = new Constanturls(mtx);
        imageurl = constanturls.getImageUrl();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_booking_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final BookingListModelClass lists = bookingList.get(position);

        final int price = Integer.parseInt(lists.getPrice());

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedPrice = formatter.format(price);

        holder.bookedhotelNameTV.setText(lists.getHotel_name());
        holder.bookedPlaceTV.setText(lists.getHotel_place());
        holder.bookedAddressTV.setText(lists.getHotel_address());
        holder.bookedRoomTypeTV.setText(lists.getRoom_type());
        holder.bookedRoomTV.setText(lists.getRooms());
        holder.bookedDaysTV.setText(lists.getDays());
        holder.bookedDateTV.setText(lists.getFrom_date());
        holder.bookedPriceTV.setText(formattedPrice);
        Glide.with(mtx)
                .load(imageurl+lists.getHotel_image())
                .into(holder.hotelImageView);

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        private final TextView bookedhotelNameTV, bookedPlaceTV, bookedAddressTV, bookedRoomTypeTV, bookedRoomTV, bookedDaysTV, bookedDateTV, bookedPriceTV;
        private final Button navigateBT;
        private final ImageView hotelImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookedhotelNameTV = (TextView) itemView.findViewById(R.id.bookedhotelNameTextView);
            hotelImageView = (ImageView) itemView.findViewById(R.id.hotelImageView);
            bookedPlaceTV = (TextView) itemView.findViewById(R.id.bookedPlaceTextView);
            bookedAddressTV = (TextView) itemView.findViewById(R.id.bookedAddressTextView);
            bookedRoomTypeTV = (TextView) itemView.findViewById(R.id.bookedRoomTypeTextView);
            bookedRoomTV = (TextView) itemView.findViewById(R.id.bookedRoomTextView);
            bookedDaysTV = (TextView) itemView.findViewById(R.id.bookedDaysTextView);
            bookedDateTV = (TextView) itemView.findViewById(R.id.bookedDateTextView);
            bookedPriceTV = (TextView) itemView.findViewById(R.id.bookedPriceTextView);
            navigateBT = (Button) itemView.findViewById(R.id.navigateButton);
        }
    }


}
