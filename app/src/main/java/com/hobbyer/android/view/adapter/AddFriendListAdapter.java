package com.hobbyer.android.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.searchfriendlist.SearchFriendViewResponseContentList;
import com.hobbyer.android.interfaces.AddFriendItemClickListners;
import com.hobbyer.android.model.MyListData;
import com.hobbyer.android.widget.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AddFriendListAdapter extends RecyclerView.Adapter<AddFriendListAdapter.ViewHolder>/* implements Filterable*/ {
    private List<SearchFriendViewResponseContentList> myListData;
    private Activity activity;
    AddFriendItemClickListners listners;
    public int isFriend;

    private List<MyListData> filteredList;
//    private CustomFilter mFilter;

    // RecyclerView recyclerView;
    public AddFriendListAdapter(Activity activity, List<SearchFriendViewResponseContentList> myListData, AddFriendItemClickListners listners) {
        this.listners = listners;
        this.activity = activity;
        this.myListData = myListData;
//        mFilter = new CustomFilter(AddFriendListAdapter.this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.add_friend_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchFriendViewResponseContentList data = myListData.get(position);

        holder.mUserName.setText(data.getFirst_name()+" "+data.getLast_name());

        if (!TextUtils.isEmpty(data.getCity())) {
            holder.mUserLocation.setText(data.getCity());
        } else {
            holder.mUserLocation.setText("");
        }

        Picasso.with(activity).load(data.getImage()).placeholder(R.mipmap.placeholder).fit().into(holder.imUserImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });

        isFriend =  isSend(data);

        if (isFriend == 0){
            holder.mFriendsAdd.setBackgroundResource(R.color.colorPrimary);
            holder.mFriendsAdd.setText(activity.getString(R.string.txt_add));
        } else {
            holder.mFriendsAdd.setBackgroundResource(R.color.space);
            holder.mFriendsAdd.setText(activity.getString(R.string.txt_sent));
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+data.getName(),Toast.LENGTH_LONG).show();
            }
        });

        holder.mFriendsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                if (isFriend == 0) {
                    listners.onAddfriendClick(position, myListData);
                }
            }
        });
    }

    public int isSend(SearchFriendViewResponseContentList data){

        int isSendRequest = 0;

        if (data.isAlready_friend() == null) {
            isSendRequest = 0;
        } else {
            isSendRequest = Integer.valueOf(((Double)data.isAlready_friend()).intValue());
        }

        return isSendRequest;
    }


    @Override
    public int getItemCount() {
        return myListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView imUserImage;
        public ImageView imgMore;
        public TextView mUserName, mUserLocation, mFriendsAdd;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            this.imUserImage = (CircularImageView) itemView.findViewById(R.id.imUserImage);
            this.imgMore = (ImageView) itemView.findViewById(R.id.imgMore);
            this.mUserName = (TextView) itemView.findViewById(R.id.mUserName);
            this.mUserLocation = (TextView) itemView.findViewById(R.id.mUserLocation);
            this.mFriendsAdd = (TextView) itemView.findViewById(R.id.mFriendsAdd);
        }
    }

    /*@Override
    public Filter getFilter() {
        return mFilter;
    }

    public class CustomFilter extends Filter {
        private AddFriendListAdapter mAdapter;
        private CustomFilter(AddFriendListAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(myListData);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final MyListData mWords : myListData) {
                    if (mWords.getName().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + filteredList.size());
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("Count Number 2 " + ((List<MyListData>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void filterList(List<MyListData> listdata) {
        this.myListData = listdata;
        notifyDataSetChanged();
    }*/
}
