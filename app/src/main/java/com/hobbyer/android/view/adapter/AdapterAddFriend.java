package com.hobbyer.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobbyer.android.R;
import com.hobbyer.android.databinding.RowSearchByNameBinding;
import com.hobbyer.android.model.AddFriendModel;

import java.util.List;

public class AdapterAddFriend extends RecyclerView.Adapter<AdapterAddFriend.MyViewHolder> {

    private List<AddFriendModel> list;
    private Context context;

    public AdapterAddFriend(List<AddFriendModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view=layoutInflater.inflate(R.layout.row_search_by_name,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if(list!=null){
            myViewHolder.rowSearchByNameBinding.ivProfile.setImageResource(list.get(i).getProfileImage());
            myViewHolder.rowSearchByNameBinding.tvAProfileName.setText(list.get(i).getProfileName());
            myViewHolder.rowSearchByNameBinding.tvCountryName.setText(list.get(i).getCountryName());
            myViewHolder.rowSearchByNameBinding.tvAddButton.setText(list.get(i).getAddButton());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private RowSearchByNameBinding rowSearchByNameBinding;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rowSearchByNameBinding=DataBindingUtil.bind(itemView);

        }
    }
}
