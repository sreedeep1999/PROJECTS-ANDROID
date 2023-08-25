package com.example.smartscheduling.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.smartscheduling.MessageFriendActivity;
import com.example.smartscheduling.ModelClass.friendListModelClass;
import com.example.smartscheduling.R;
import com.example.smartscheduling.Utils.GlobalPreference;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {

    private final String ip;
    private List<friendListModelClass> friendList;
    private Context mctx;

    public FriendListAdapter(List<friendListModelClass> friendList, Context mctx) {
        this.friendList = friendList;
        this.mctx = mctx;

        GlobalPreference globalPreference = new GlobalPreference(mctx);
        ip = globalPreference.RetriveIP();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_friend_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final friendListModelClass lists = friendList.get(position);
        
        holder.nameTextView.setText(lists.getName());
       Glide.with(mctx)
                .load("http://"+ip+"/SmartScheduling/admin/tbl_customer/uploads/" + lists.getImage())
                .into(holder.friendImageView);

        holder.messageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mctx, MessageFriendActivity.class);
                intent.putExtra("id",lists.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView friendImageView,messageImageView;
        TextView nameTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            friendImageView = itemView.findViewById(R.id.friendImageView);
            messageImageView = itemView.findViewById(R.id.messageImageView);
            nameTextView = itemView.findViewById(R.id.friendNameTextView);

        }
    }
}
