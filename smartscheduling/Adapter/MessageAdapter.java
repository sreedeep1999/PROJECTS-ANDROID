package com.example.smartscheduling.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartscheduling.ModelClass.MessageModelClass;
import com.example.smartscheduling.R;

import java.util.ArrayList;

public class MessageAdapter extends  RecyclerView.Adapter<MessageAdapter.ItemViewHolder> {

    private Context mcontext;
    private ArrayList<MessageModelClass> arrayList;
    public static final String TAG="msglist";

    public MessageAdapter(Context mcontext, ArrayList<MessageModelClass> arrayList) {
        this.mcontext = mcontext;
        this.arrayList = arrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view=layoutInflater.from(parent.getContext()).inflate(R.layout.raw_item_msglist,parent,false);
        ItemViewHolder itemViewHolder= new ItemViewHolder(view);
        return  itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        final MessageModelClass item=arrayList.get(position);

        String sid= String.valueOf(item.getSid());
        String uid= String.valueOf(item.getUid());

        if(sid.equals(uid)){
            holder.cright.setVisibility(View.VISIBLE);
            holder.cleft.setVisibility(View.GONE);
            holder.cright.setCardBackgroundColor(Color.parseColor("#32b1a4"));
            holder.text_right.setText(item.getName()+"  :  "+item.getMsg());
        }
        else{
            holder.cleft.setVisibility(View.VISIBLE);
            holder.cright.setVisibility(View.GONE);
            holder.cleft.setCardBackgroundColor(Color.parseColor("#fd11c4ce"));
            holder.text_left.setText(item.getName()+"  :  "+item.getMsg());
        }
        Log.d(TAG, "onBindViewHolder: "+item.getName());
    }

    @Override
    public int getItemCount() {

        if (arrayList != null) {
            return arrayList.size();

        }
        return 0;
}
    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public TextView text_right,text_left;
        CardView cleft,cright;

        @SuppressLint("CutPasteId")
        public ItemViewHolder(View itemView) {
            super(itemView);

            text_right=(TextView) itemView.findViewById(R.id.txtright);
            text_left=(TextView) itemView.findViewById(R.id.txtleft);
            cleft=itemView.findViewById(R.id.card_view_left);
            cright=itemView.findViewById(R.id.card_view_right);
        }
    }
}
