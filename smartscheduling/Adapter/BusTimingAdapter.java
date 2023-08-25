package com.example.smartscheduling.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartscheduling.ModelClass.BusTimingModelClass;
import com.example.smartscheduling.R;
import com.example.smartscheduling.Utils.GlobalPreference;

import java.util.ArrayList;

public class BusTimingAdapter extends RecyclerView.Adapter<BusTimingAdapter.MyViewHolder> {

    ArrayList<BusTimingModelClass> list;
    Context context;
    String ip;

    public BusTimingAdapter(ArrayList<BusTimingModelClass> list, Context context) {
        this.list = list;
        this.context = context;
        GlobalPreference globalPreference = new GlobalPreference(context);
        ip = globalPreference.RetriveIP();

    }


    @NonNull
    @Override
    public BusTimingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_bu_timings,parent,false);
        return new BusTimingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusTimingAdapter.MyViewHolder holder, int position) {

        BusTimingModelClass busList = list.get(position);

        holder.busnameTV.setText(busList.getName());
        holder.busrouteTV.setText(busList.getRoute());
        holder.busTimingTV.setText(busList.getTimings());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView busnameTV;
        TextView busrouteTV;
        TextView busTimingTV;

        public MyViewHolder(@NonNull View itemview) {
            super(itemview);

           busnameTV = itemview.findViewById(R.id.tBusnameTextView);
           busrouteTV = itemview.findViewById(R.id.tBusrouteTextView);
           busTimingTV = itemview.findViewById(R.id.tBusTimingTextView);
        }
    }
}
