package com.roy.taxicharge;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by alfo06_25 on 2018-04-12.
 */

public class MenuAdapter extends RecyclerView.Adapter {

    Context context;
    Bitmap[] bitmaps;
    public MenuAdapter(Context context, Bitmap[] bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler_item.xml를 View객체로 만들어주는 작업
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView= inflater.inflate(R.layout.list_item, parent, false);
        VH holder = new VH(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH)holder;
        Glide.with(context).load(bitmaps[position]).into(vh.iv);

    }

    @Override
    public int getItemCount() {
        return bitmaps.length;
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView iv;

        public VH(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv);

            //아이템뷰를 클릭했을때..
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NaviActivity.class);
//
//                    //전환 효과
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation((Activity)context, new Pair<View, String>(ivIcon, "IMG"));
//                        context.startActivity(intent, options.toBundle());
//                    }else{
                    context.startActivity(intent);
//                    }
                }
            });
        }
    }
}
