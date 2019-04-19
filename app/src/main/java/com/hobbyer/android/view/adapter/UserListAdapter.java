package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.acceptfriendrequest.ConfirmFriendViewResponseContentList;
import com.hobbyer.android.interfaces.OnConfirmFriendItemClickListners;
import com.hobbyer.android.widget.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    List<ConfirmFriendViewResponseContentList> myListDataList = new ArrayList<>();
    private Activity activity;
    OnConfirmFriendItemClickListners listners;

    // RecyclerView recyclerView;
    public UserListAdapter(Activity activity, List<ConfirmFriendViewResponseContentList> myListDataList, OnConfirmFriendItemClickListners listners) {
        this.activity = activity;
        this.myListDataList = myListDataList;
        this.listners = listners;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.user_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    //    public void addAll(ArrayList<MyListData> myListData){
//        try{
//            this.myListDataList.clear();
//            this.myListDataList.addAll(myListData);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        notifyDataSetChanged();
//    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ConfirmFriendViewResponseContentList data = myListDataList.get(position);

        holder.mUserName.setText(data.getFirst_name() + " " + data.getLast_name());
        holder.mUserLocation.setText(data.getCity());

        Picasso.with(activity).load(data.getImage()).placeholder(R.mipmap.placeholder).fit().into(holder.imUserImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+data.getName(),Toast.LENGTH_LONG).show();
            }
        });

        holder.mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listners.onAccept(position, data);

//                Toast.makeText(view.getContext(),"click on item: "+data.getName(),Toast.LENGTH_LONG).show();
            }
        });

        holder.mBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listners.onBlock(position, data);

//                Toast.makeText(view.getContext(),"click on item: "+data.getName(),Toast.LENGTH_LONG).show();
            }
        });

        holder.mIgonre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listners.onIgnore(position, data);

//                Toast.makeText(view.getContext(),"click on item: "+data.getName(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return myListDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView imUserImage;
        public TextView mUserName, mUserLocation, mAccept, mIgonre, mBlock;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            this.imUserImage = (CircularImageView) itemView.findViewById(R.id.imUserImage);
            this.mUserName = (TextView) itemView.findViewById(R.id.mUserName);
            this.mUserLocation = (TextView) itemView.findViewById(R.id.mUserLocation);
            this.mAccept = (TextView) itemView.findViewById(R.id.mAccept);
            this.mIgonre = (TextView) itemView.findViewById(R.id.mIgonre);
            this.mBlock = (TextView) itemView.findViewById(R.id.mBlock);
        }
    }
}
