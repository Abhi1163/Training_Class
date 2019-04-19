package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.getfriends.GetUserFriendViewResponseContentList;
import com.hobbyer.android.interfaces.OnFriendItemClickListners;
import com.hobbyer.android.widget.CircularImageView;
import com.hobbyer.android.widget.CustomDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private List<GetUserFriendViewResponseContentList> myListData = new ArrayList<>();
    private Activity activity;

    private CustomDialog dialog;
    private TextView btConfirm, btCancel,tvTitleSure;
    OnFriendItemClickListners listners;

    // RecyclerView recyclerView;
    public FriendListAdapter(Activity activity, List<GetUserFriendViewResponseContentList> myListData, OnFriendItemClickListners listners) {
        this.listners = listners;
        this.activity = activity;
        this.myListData = myListData;
    }

    //    public void addAll(ArrayList<MyListData> myListData){
//     try{
//         this.myListData.clear();
//         this.myListData.addAll(myListData);
//     }catch (Exception e){
//         e.printStackTrace();
//     }
//     notifyDataSetChanged();
//    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.friend_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetUserFriendViewResponseContentList data = myListData.get(position);

        holder.mUserName.setText(data.getFirst_name() + " " + data.getLast_name());
        holder.mUserLocation.setText(data.getCity());

        holder.mUserPastClass.setVisibility(View.GONE);
        holder.mUserUpcomingClass.setVisibility(View.GONE);
        holder.mUserFavorite.setVisibility(View.GONE);

//        holder.mUserPastClass.setText(activity.getString(R.string.txt_past_class)+"("+data.getPastClass()+")");
//        holder.mUserUpcomingClass.setText(activity.getString(R.string.txt_upconing_class)+"("+data.getUpconingClass()+")");
//        holder.mUserFavorite.setText(activity.getString(R.string.txt_favorite_class)+"("+data.getFavoriteClass()+")");

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

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertFindCity(position, data);
            }
        });
    }

    public void showAlertFindCity(int pos, GetUserFriendViewResponseContentList myListData) {

        dialog = new CustomDialog(activity, R.layout.friend_block_popup);
        dialog.setCancelable(true);
        btConfirm = (TextView) dialog.findViewById(R.id.btConfirm);
        btCancel = (TextView) dialog.findViewById(R.id.btCancel);
        tvTitleSure = (TextView) dialog.findViewById(R.id.tvTitleSure);


        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listners.onUnfriendClick(pos, myListData);

                dialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listners.onBlockClick(pos, myListData);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return myListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView imUserImage;
        public ImageView imgMore;
        public TextView mUserName, mUserLocation, mUserPastClass, mUserUpcomingClass, mUserFavorite;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            this.imUserImage = (CircularImageView) itemView.findViewById(R.id.imUserImage);
            this.imgMore = (ImageView) itemView.findViewById(R.id.imgMore);
            this.mUserName = (TextView) itemView.findViewById(R.id.mUserName);
            this.mUserLocation = (TextView) itemView.findViewById(R.id.mUserLocation);
            this.mUserPastClass = (TextView) itemView.findViewById(R.id.mUserPastClass);
            this.mUserUpcomingClass = (TextView) itemView.findViewById(R.id.mUserUpcomingClass);
            this.mUserFavorite = (TextView) itemView.findViewById(R.id.mUserFavorite);
        }
    }
}
