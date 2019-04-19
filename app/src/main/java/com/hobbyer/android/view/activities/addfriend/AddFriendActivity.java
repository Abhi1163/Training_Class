package com.hobbyer.android.view.activities.addfriend;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityAddFriendBinding;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddFriendBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_friend);
        context=AddFriendActivity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(AddFriendActivity.this, context.getResources().getColor(R.color.colorPrimary));
        }
        binding.rlAddByName.setOnClickListener(this);
        setToolbar();
        setOnClickListner();
    }


    private void setToolbar() {
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setText("Add friends");
        binding.toolbar.tvRight.setVisibility(View.VISIBLE);
        binding.toolbar.tvRight.setText("Done");
        binding.toolbar.ivBack.setOnClickListener(this);
    }

    private void setOnClickListner() {
        binding.rlAddByName.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rlAddByName:
                ActivityController.startActivity(context,AddByNameActivity.class);
                break;
            case R.id.ivBack:
                finish();
                break;

        }

    }
}
